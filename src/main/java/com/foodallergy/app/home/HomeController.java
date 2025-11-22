package com.foodallergy.app.home;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public void home(HttpSession session) {
        String user = session.getAttribute("username").toString();

    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
