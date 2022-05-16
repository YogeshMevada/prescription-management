package com.prescription.ui.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class PageController {

    @RequestMapping(value = "/{page}")
    public String loadPage(@PathVariable(value = "page") final String page, final Model model) {
        log.info("Inside page controller. Loading {}...", page);
        return "";
    }
}
