package com.example.springdemo2;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String emptyPage() {
        return "Hello World";
    }

    @GetMapping("/users/{id}")
    public Resource<User> getUser(@PathVariable Long id) {
         User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

         return new Resource<>(user,
                 linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
                 linkTo(methodOn(UserController.class).getAllUsers()).withRel("getAllUsers"));
    }

    @GetMapping("/users")
    public Resources<Resource<User>> getAllUsers(){
        List<Resource<User>> allUsers =  repository.findAll().stream()
                .map(user -> new Resource<>(user,
                        linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAllUsers()).withRel("getAllUsers")))
                .collect(Collectors.toList());
        return new Resources<>(allUsers,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }


    @PostMapping("/users")
    public Resource<User> addUser(@RequestBody User user) {
        User newUser = repository.save(user);
        return new Resource<>(newUser,
                linkTo(methodOn(UserController.class).addUser(user)).withSelfRel());
    }

    @PutMapping("/users/{id}")
    public User changeUser(@RequestBody User user, @PathVariable Long id) {
        return repository.findById(id).map(changedUser -> {
            changedUser.setName(user.getName());
            changedUser.setAccount(user.getAccount());
            return repository.save(changedUser);
        })
                .orElseGet(() -> {
                    user.setId(id);
                    return repository.save(user);
                });

        // anstatt map:
        /*
        * userToSave = repo.findById(...)
        * repo.save(userToSave)
        */
    }


    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        repository.deleteById(id);
        return "successfully deleted";
    }
}
