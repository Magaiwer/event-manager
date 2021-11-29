package dev.magaiver.subscription.api.model.input;

import dev.magaiver.subscription.domain.model.Event;
import dev.magaiver.subscription.domain.model.Subscription;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionInput {

    private String id;
    private String eventId;
    private String userEmail;
    private boolean enabled;

    public Subscription assembler() {
        return new Subscription(this.getId(), new Event(this.getEventId()), this.getUserEmail(), this.isEnabled());
    }
}
