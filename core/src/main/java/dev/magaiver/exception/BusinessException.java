package dev.magaiver.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Magaiver Santos
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessException extends RuntimeException {
    private final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}