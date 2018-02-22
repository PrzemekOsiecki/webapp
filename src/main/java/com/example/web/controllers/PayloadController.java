package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PayloadController {

    private static final String PAYLOAD_PAGE = "payload/payload";

    @RequestMapping("/payload")
    String getPayloadPage() {
        return PAYLOAD_PAGE;
    }
}
