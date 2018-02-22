package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    private static final String LOGIN_VIEW_PAGE = "user/login";

    @RequestMapping("/login")
    String getLoginPage() {
        return LOGIN_VIEW_PAGE;
    }
}
