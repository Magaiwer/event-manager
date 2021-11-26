package dev.magaiver.user.domain.service.exception;

import dev.magaiver.core.exception.BusinessException;

/**
 * @author Magaiver Santos
 */
public class RequiredPasswordException extends BusinessException {
    public RequiredPasswordException() {
        super("Senha é obrigatória para novo usuário");
    }
}