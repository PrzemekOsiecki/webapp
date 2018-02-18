package com.example.backend.service;

import com.example.web.domain.frontend.Feedback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.email.address}")
    private String defaultEmailAddress;

    @Override
    public void sendFeedbackEmail(Feedback feedback) {
        sendGenericEmailMessage(prepareSimpleMailMessageFormFeedback(feedback));
    }

    protected SimpleMailMessage prepareSimpleMailMessageFormFeedback(Feedback feedback) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(defaultEmailAddress);
        mail.setFrom(feedback.getEmail());
        mail.setSubject("Feedback form " + feedback.getFirstName() + " " + feedback.getLastName());
        mail.setText(feedback.getFeedback());
        return mail;
    }
}
