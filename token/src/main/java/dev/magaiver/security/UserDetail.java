package dev.magaiver.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Magaiver Santos
 */
@Builder
@Getter
@Setter
public class UserDetail {
    private String id;
    private String username;
    private String role;
}
