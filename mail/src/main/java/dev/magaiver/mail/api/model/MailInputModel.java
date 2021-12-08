package dev.magaiver.mail.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Magaiver Santos
 */
@Getter
@Setter
public class MailInputModel {

    private String emailTo;
    private String message;
    private String subject;

}
