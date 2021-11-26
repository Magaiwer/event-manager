package dev.magaiver.core.exception;

/**
 * @author Magaiver Santos
 */
public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}