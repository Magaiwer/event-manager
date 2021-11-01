package dev.magaiver.api.model.input;

import dev.magaiver.domain.model.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author Magaiver Santos
 */
@Data
public class UserInput {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

    private String password;

    public User assembler() {
        return new User(this.getId(), this.getName(), this.getEmail(), this.getPassword());
    }
}