package com.example.web.controllers;

import com.example.backend.service.EmailService;
import com.example.web.domain.frontend.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContactController {

    private static final String CONTACT_PAGE = "contact/contact";

    private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String getContact(ModelMap model) {
        Feedback feedback = new Feedback();
        model.addAttribute("feedback",feedback);
        return CONTACT_PAGE;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String createContact(@ModelAttribute("feedback") Feedback feedback) {
        LOG.info("Feedback content: {}", feedback);
        emailService.sendFeedbackEmail(feedback);
        return CONTACT_PAGE;
    }
}
