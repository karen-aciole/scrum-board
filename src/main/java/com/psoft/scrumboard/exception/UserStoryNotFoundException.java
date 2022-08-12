package com.psoft.scrumboard.exception;

public class UserStoryNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserStoryNotFoundException(String message) {
        super(message);
    }
}
