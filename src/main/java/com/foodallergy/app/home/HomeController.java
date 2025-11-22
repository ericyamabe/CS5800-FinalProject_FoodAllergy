package com.foodallergy.app.home;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(HttpSession session) {
        String user = session.getAttribute("username").toString();

        if(user == null) {
            return "redirect:/login?error_msg=You must be logged in";
        }
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
