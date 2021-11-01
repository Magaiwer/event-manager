package dev.magaiver.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import dev.magaiver.exception.BusinessException;
import dev.magaiver.exception.EntityAlreadyInUseException;
import dev.magaiver.exception.EntityNotFoundException;
import dev.magaiver.property.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author Magaiver Santos
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final Messages messages;
    private static final String MSG_GENERIC_END_USER = "message.generic.end.user";
    private static final String MSG_PROPERTY_NOT_FOUND = "property.not.found";
    private static final String MSG_INVALID_FORMAT = "invalid.format";
    private static final String MSG_INVALID_PARAMETER = "invalid.parameter";

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorType errorType = ErrorType.ENTITY_NOT_FOUND;
        String detail = ex.getMessage();
        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorType errorType = ErrorType.BUSINESS_EXCEPTION;
        String detail = ex.getMessage();
        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }
/*    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorType errorType = ErrorType.ACCESS_DENIED;
        String detail = ex.getMessage();
        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }*/

    @ExceptionHandler(EntityAlreadyInUseException.class)
    ResponseEntity<Object> handleEntityAlreadyInUseException(EntityAlreadyInUseException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorType errorType = ErrorType.ENTITY_ALREADY_IN_USE;
        String detail = ex.getMessage();
        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = messages.getMessage(MSG_GENERIC_END_USER);
        Error error = Error.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .userMessage(messages.getMessage(MSG_GENERIC_END_USER))
                .build();
        // TODO RETURN JUST TITLE
        if (body == null) {
            body = error.builder().title(status.getReasonPhrase()).build();
        } else if (body instanceof String) {
            body = error.builder().title((String) body).build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers,
                                                                    HttpStatus status, WebRequest request) {

        ErrorType errorType = ErrorType.INVALID_PARAMETER;

        String detail = format(messages.getMessage(MSG_INVALID_PARAMETER),
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(messages.getMessage(MSG_GENERIC_END_USER))
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {
        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request, BindingResult bindingResult) {
        ErrorType errorType = ErrorType.INVALID_DATA;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<Error.FieldError> fieldErrorList = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    String message = messages.getMessage(fieldError);
                    return Error.FieldError.builder()
                            .name(fieldError.getField())
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(detail)
                .fieldErrorList(fieldErrorList)
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
        String detail = format(MSG_PROPERTY_NOT_FOUND, path);
        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(messages.getMessage(MSG_GENERIC_END_USER))
                .build();
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
        String detail = format(MSG_INVALID_FORMAT, path, ex.getValue(), ex.getTargetType().getSimpleName());

        Error error = createErrorBuilder(status, errorType, detail)
                .userMessage(messages.getMessage(MSG_GENERIC_END_USER))
                .build();
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    private Error.ErrorBuilder createErrorBuilder(HttpStatus status, ErrorType errorType, String detail) {
        return Error.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .title(errorType.getTitle())
                .detail(detail);
    }

    private String joinPath(List<Reference> references) {
        return references.stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
    }
}