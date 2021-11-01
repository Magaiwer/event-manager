package dev.magaiver.exception;

/**
 * @author Magaiver Santos
 */
public class EntityAlreadyInUseException extends BusinessException{

    public EntityAlreadyInUseException(String message) {
        super(message);
    }
}