package com.example.springdemo2;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // reason = "balbla"
public class UserNotFoundException extends RuntimeException {

    public  UserNotFoundException(Long id){
        super("user not found: " + id);
    }
}
