package dev.magaiver.subscription.api.model.input;

import dev.magaiver.subscription.domain.model.CheckIn;
import dev.magaiver.subscription.domain.model.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Magaiver Santos
 */
@Getter
@Setter
public class CheckInInput {

    private String eventId;
    private String userEmail;

    public CheckIn assembler() {
        return new CheckIn(new Event(this.getEventId()), this.getUserEmail());
    }
}
