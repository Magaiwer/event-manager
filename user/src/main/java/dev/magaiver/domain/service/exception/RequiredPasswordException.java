package dev.magaiver.domain.service.exception;

import dev.magaiver.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class RequiredPasswordException extends BusinessException {
    public RequiredPasswordException() {
        super("Senha é obrigatória para novo usuário");
    }
}