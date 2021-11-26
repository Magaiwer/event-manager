package dev.magaiver.domain.model;

import dev.magaiver.user.domain.model.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
public class Certificate {

    @Id
    private String id;

    @Column
    @CreationTimestamp
    private LocalDateTime issueDate;

    @ManyToOne
    private User user;
}
