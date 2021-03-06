package com.prescription.ui.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/authenticate")
public class LoginController {

    @GetMapping("/login")
    public String login() {
        log.info("Login page reached");
        return "login";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "register";
    }

    @GetMapping(value = "/dashboard")
    public String getLandingPage() {
        return "dashboard";
    }

    @GetMapping(value = "/error")
    public String getErrorPage() {
        return "errorPage";
    }
}
