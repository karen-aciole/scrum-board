package com.psoft.scrumboard.exception;

public class UsuarioNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
