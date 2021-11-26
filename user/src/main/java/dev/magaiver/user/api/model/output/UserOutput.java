package dev.magaiver.user.api.model.output;

import dev.magaiver.user.domain.model.User;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * @author Magaiver Santos
 */
@Data
public class UserOutput {
    private Long id;
    private String name;
    private String email;
    private OffsetDateTime createdAt;


    public UserOutput disassembler(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.createdAt = user.getCreatedAt();
        this.email = user.getEmail();
        return this;
    }
}