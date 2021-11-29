package dev.magaiver.subscription.api.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.magaiver.subscription.domain.model.Event;
import dev.magaiver.subscription.domain.model.Subscription;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Magaiver Santos
 */
@Getter
@Setter
public class SubscriptionOutput {

    private String id;
    @JsonProperty("event")
    private EventOutput eventOutput;
    private String userEmail;
    private boolean enabled;

    public SubscriptionOutput disassembler(Subscription subscription) {
        this.setId(subscription.getId());
        this.setEventOutput(new EventOutput().disassembler(subscription.getEvent()));
        this.setUserEmail(subscription.getUserEmail());
        this.setEnabled(subscription.isEnabled());
        return this;
    }

    public List<SubscriptionOutput> collectionDisassembler(List<Subscription> subscriptionList) {
        List<SubscriptionOutput> subscriptionOutputList = new ArrayList<>();
        subscriptionList.forEach(s -> subscriptionOutputList.add(new SubscriptionOutput().disassembler(s)));
        return subscriptionOutputList;
    }
}
