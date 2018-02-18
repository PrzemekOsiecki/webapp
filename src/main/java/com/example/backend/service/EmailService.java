package com.example.backend.service;

import com.example.web.domain.frontend.Feedback;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendFeedbackEmail(Feedback feedback);

    void sendGenericEmailMessage(SimpleMailMessage message);

}
