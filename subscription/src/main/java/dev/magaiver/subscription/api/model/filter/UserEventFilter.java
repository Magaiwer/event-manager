package dev.magaiver.subscription.api.model.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Magaiver Santos
 */

@Getter
@Setter
public class UserEventFilter {
    private String eventId;
    private String userEmail;
}
