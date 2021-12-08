package dev.magaiver.mail;

import dev.magaiver.user.UserApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Magaiver Santos
 */
@SpringBootApplication
@ComponentScan(basePackages = "dev.magaiver.*")
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
