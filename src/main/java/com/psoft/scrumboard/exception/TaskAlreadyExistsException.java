package com.psoft.scrumboard.exception;

public class TaskAlreadyExistsException extends Exception{
    private static final long serialVersionUID = 1L;

    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
