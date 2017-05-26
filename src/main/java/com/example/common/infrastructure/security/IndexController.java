package com.example.common.infrastructure.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by gkgranada on 26/05/2017.
 */
@Controller
public class IndexController implements org.springframework.boot.autoconfigure.web.ErrorController{


    @GetMapping("/login")
    public String getLoginForm(Model model) {
        return "dashboard/login";
    }

    // Login form with error
    @RequestMapping("/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "dashboard/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
