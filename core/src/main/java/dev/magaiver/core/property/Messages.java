package dev.magaiver.core.property;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import javax.annotation.PostConstruct;

/**
 * @author Magaiver Santos
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class Messages {
    private final MessageSource messageSource;
    private MessageSourceAccessor messageSourceAccessor;

    @PostConstruct
    private void init() {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
    }

    public String getMessage(String key) {
        return this.messageSourceAccessor.getMessage(key);
    }
    public String getMessage(FieldError fieldError) {
        return this.messageSourceAccessor.getMessage(fieldError);
    }
}