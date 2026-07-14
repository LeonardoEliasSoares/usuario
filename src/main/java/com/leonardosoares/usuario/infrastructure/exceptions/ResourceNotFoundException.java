package com.leonardosoares.usuario.infrastructure.exceptions;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
    public ResourceNotFoundException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }


}
