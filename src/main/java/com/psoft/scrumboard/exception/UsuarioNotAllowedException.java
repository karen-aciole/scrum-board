package com.psoft.scrumboard.exception;

public class UsuarioNotAllowedException extends Exception {
    private static final long serialVersionUID = 1L;

    public UsuarioNotAllowedException(String message) {
        super(message);
    }
}
