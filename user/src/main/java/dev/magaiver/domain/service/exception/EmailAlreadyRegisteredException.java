package dev.magaiver.domain.service.exception;

import dev.magaiver.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class EmailAlreadyRegisteredException extends BusinessException {

    public EmailAlreadyRegisteredException(String email) {
        super(String.format("Email %s já está cadastrado no sistema", email));
    }
}