package dev.magaiver.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "dev.magaiver.*")
public class SubscriptionApp {
    public static void main(String[] args) {
        SpringApplication.run(SubscriptionApp.class, args);
    }
}
