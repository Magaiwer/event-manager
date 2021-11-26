package dev.magaiver.core.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.*;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Magaiver Santos
 */
@JsonInclude(Include.NON_NULL)
@Builder
@Getter
public class Error {

    private final Integer status;
    private final OffsetDateTime timestamp;
    private final String type;
    private final String title;
    private final String userMessage;
    private final String detail;
    private final List<FieldError> fieldErrorList;

    @Getter
    @Builder
    public static class FieldError {
        private final String name;
        private final String userMessage;
    }
}