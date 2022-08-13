package com.psoft.scrumboard.exception;

public class UserStoryAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserStoryAlreadyExistsException(String message) {
        super(message);
    }
}
