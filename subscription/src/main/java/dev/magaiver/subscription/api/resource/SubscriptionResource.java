package dev.magaiver.subscription.api.resource;

import dev.magaiver.subscription.api.model.filter.UserEventFilter;
import dev.magaiver.subscription.api.model.input.SubscriptionInput;
import dev.magaiver.subscription.api.model.output.SubscriptionOutput;
import dev.magaiver.subscription.domain.model.Certificate;
import dev.magaiver.subscription.domain.model.Subscription;
import dev.magaiver.subscription.domain.service.CertificateService;
import dev.magaiver.subscription.domain.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/subscription")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionResource {

    private final SubscriptionService subscriptionService;
    private final CertificateService certificateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionOutput register(@Valid @RequestBody final SubscriptionInput subscriptionInput) {
        final Subscription subscriptionSaved = subscriptionService.save(subscriptionInput.assembler());
        return new SubscriptionOutput().disassembler(subscriptionSaved);
    }

    @PostMapping("/certificate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InputStreamResource> certificate(@RequestBody final SubscriptionInput subscriptionInput) {
        Subscription subscription = subscriptionService.findByIdOrElseThrow(subscriptionInput.getId());
        Certificate certificate = certificateService.generate(subscription);
        ByteArrayInputStream bis = certificateService.generatePDF(certificate);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline; filename=certificate.pdf");

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(httpHeaders)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/certificate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Certificate validate(@PathVariable String id) {
        return certificateService.findByIdOrElseThrow(id);
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
