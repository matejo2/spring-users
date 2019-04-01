package com.example.springdemo2;

import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

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
                 linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return repository.findAll();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return repository.save(user);
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
    }


    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        repository.deleteById(id);
        return "successfully deleted";
    }
}
