package com.example.security.handler.customexception;

import java.util.function.Supplier;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }

}
