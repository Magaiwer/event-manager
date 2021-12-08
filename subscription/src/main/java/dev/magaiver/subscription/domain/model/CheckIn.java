package dev.magaiver.subscription.domain.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author Magaiver Santos
 */
@Data
@Document
public class CheckIn implements AbstractEntity {

    @Id
    private String id;
    private Event event;
    private String userEmail;
    @CreationTimestamp
    private LocalDateTime dateTime;

    public CheckIn(Event event, String userEmail) {
        this.event = event;
        this.userEmail = userEmail;
        this.dateTime = LocalDateTime.now();
    }
}
