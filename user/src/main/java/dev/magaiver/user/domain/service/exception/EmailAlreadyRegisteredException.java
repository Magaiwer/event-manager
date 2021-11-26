package dev.magaiver.user.domain.service.exception;

import dev.magaiver.core.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class EmailAlreadyRegisteredException extends BusinessException {

    public EmailAlreadyRegisteredException(String email) {
        super(String.format("Email %s já está cadastrado no sistema", email));
    }
}