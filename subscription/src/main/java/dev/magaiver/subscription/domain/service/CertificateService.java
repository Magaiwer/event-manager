package dev.magaiver.subscription.domain.service;

import dev.magaiver.subscription.domain.model.Certificate;
import dev.magaiver.subscription.domain.model.Subscription;
import dev.magaiver.subscription.domain.repository.CertificateRepository;
import dev.magaiver.subscription.domain.service.exeption.CertificateNotFoundException;
import dev.magaiver.subscription.domain.service.exeption.NoCheckInFound;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.cert.CertificateException;

import static java.lang.String.*;

/**
 * @author Magaiver Santos
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final SubscriptionService subscriptionService;
    private final EventService eventService;

    private static final String URL_CLIENT = "http://event.client:4200/";

    public Certificate generate(Subscription subscription) {
        Subscription sub = subscriptionService.findByIdOrElseThrow(subscription.getId());

        boolean alreadyCheckIn = eventService.verifyCheckInExists(sub.getUserEmail(), sub.getEvent().getId());
        if (!alreadyCheckIn) throw new NoCheckInFound("Not check in found for this event!");

        Certificate certificate = findBySubscriptionId(sub.getId());
        if (certificate == null) {
            certificate = new Certificate("", sub);
            return certificateRepository.save(certificate);
        }

        return certificate;
    }

    public ByteArrayInputStream generatePDF(Certificate certificate) {
        try {

            PDDocument document = PDDocument.load(getClass().getClassLoader().getResourceAsStream("certificado-template-form.pdf"));
            PDAcroForm pDAcroForm = document.getDocumentCatalog().getAcroForm();

            PDField field = pDAcroForm.getField("userEmail");
            field.setValue(certificate.getSubscription().getUserEmail());
            field.setReadOnly(true);

            field = pDAcroForm.getField("eventDescription");
            field.setValue(format("Certificamos o comparecimento no evento %s realizado no dia",
                    certificate.getSubscription().getEvent().getName()));
            field.setReadOnly(true);

            field = pDAcroForm.getField("dateTimeEvent");
            field.setValue(certificate.getSubscription().getEvent().dateTimeStr());
            field.setReadOnly(true);

            field = pDAcroForm.getField("linkValidation");
            field.setValue(URL_CLIENT + "certificate/validate/" + certificate.getId());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            document.save(bos);
            document.close();
            return new ByteArrayInputStream(bos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Certificate findBySubscriptionId(String subscriptionId) {
        return certificateRepository.findBySubscriptionId(subscriptionId)
                .orElse(null);
    }

    public Certificate findByIdOrElseThrow(String id) {
        return certificateRepository.findById(id)
                .orElseThrow(()-> new CertificateNotFoundException(format("Certificate with code %s not found", id)));
    }
}