package dev.magaiver.mail.api;

import dev.magaiver.mail.api.model.MailInputModel;
import dev.magaiver.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Magaiver Santos
 */
@RestController
@RequestMapping(value = "/v1/mail")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailResource {

    private final MailService mailService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void send(@RequestBody final MailInputModel mailInputModel) {
        mailService.sendMail(mailInputModel.getEmailTo(), mailInputModel.getMessage(), mailInputModel.getSubject());
    }
}
