package dev.magaiver.user.api.model.input;

import dev.magaiver.user.domain.model.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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