package dev.magaiver.domain.service.exception;

import dev.magaiver.exception.EntityNotFoundException;

/**
 * @author Magaiver Santos
 */
public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        this(String.format("Usuário de código %d não encontrado", id));
    }


}