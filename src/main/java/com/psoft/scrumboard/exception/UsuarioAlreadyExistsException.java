package com.psoft.scrumboard.exception;

public class UsuarioAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    public UsuarioAlreadyExistsException(String message) {
        super(message);
    }
}
