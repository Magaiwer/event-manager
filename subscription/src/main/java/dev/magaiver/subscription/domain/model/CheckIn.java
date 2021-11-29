package dev.magaiver.subscription.domain.model;

import dev.magaiver.user.domain.model.User;
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
    private User user;
    @CreationTimestamp
    private LocalDateTime dateTime;




}
