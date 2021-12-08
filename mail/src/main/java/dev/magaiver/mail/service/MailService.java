package dev.magaiver.mail.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class MailService {
    public void sendMail(String emailTo, String message, String subject ) {
        Email from = new Email("magaiwer@hotmail.com");
        Email to = new Email(emailTo);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("API_KEY_SENDGRID"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            log.info("send mail request");
            log.info("mail status code: {}", response.getStatusCode());
            log.info("mail response body: {}", response.getBody());
            log.info("mail response headers: {}", response.getHeaders());

        } catch (IOException ex) {
            log.error("error sending email: {}", ex.getMessage());
        }
    }
}
