package dev.magaiver.token.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@ConfigurationProperties(prefix = "spring.jwt.config")
@Getter
@Setter
@ToString
@Configuration
public class JwtConfiguration implements Serializable {
    private String loginUrl = "/login/**";
    private int expirationTimeMS = 3600;
    private String privateKey = "qxBEEQv7E8aviX1KUcdOiF5ve5COUPAr";
    private String type = "signed"; //"encrypted";
    private String headerName = "Authorization";
    private String headerValue =  "Bearer ";

}
