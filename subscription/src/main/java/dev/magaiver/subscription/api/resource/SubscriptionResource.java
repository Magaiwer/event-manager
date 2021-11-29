package dev.magaiver.subscription.api.resource;

import dev.magaiver.subscription.api.model.filter.UserEventFilter;
import dev.magaiver.subscription.api.model.input.SubscriptionInput;
import dev.magaiver.subscription.api.model.output.SubscriptionOutput;
import dev.magaiver.subscription.domain.model.Subscription;
import dev.magaiver.subscription.domain.service.SubscriptionService;
import dev.magaiver.user.api.model.input.UserPasswordInput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/subscription")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionResource {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionOutput register(@Valid @RequestBody final SubscriptionInput subscriptionInput) {
        final Subscription subscriptionSaved = subscriptionService.save(subscriptionInput.assembler());
        return new SubscriptionOutput().disassembler(subscriptionSaved);
    }

    @GetMapping("/user/all/{userEmail}")
    @ResponseStatus(HttpStatus.OK)
    public List<SubscriptionOutput> findAll(@PathVariable String userEmail) {
        return new SubscriptionOutput()
                .collectionDisassembler(subscriptionService.findAllByUserEmailOrElseThrow(userEmail));
    }

    @GetMapping("/user/event")
    @ResponseStatus(HttpStatus.OK)
    public SubscriptionOutput findByUserEmailAndEvent(@Valid @RequestBody final UserEventFilter userEventFilter) {
        Subscription subscription = subscriptionService
                .findByUserEmailAndEventIdOrElseThrow(userEventFilter.getUserEmail(), userEventFilter.getEventId());
        return new SubscriptionOutput().disassembler(subscription);
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancel(@PathVariable String id) {
        Subscription subscription = subscriptionService.findByIdOrElseThrow(id);
        subscriptionService.cancel(subscription);
    }
}
