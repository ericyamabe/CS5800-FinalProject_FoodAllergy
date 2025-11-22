package com.foodallergy.app.events;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventsController {
    @GetMapping("/events")
    public String eventsHome(HttpSession session) {
        return "redirect:/home";
    }

    @GetMapping("/logEvent")
    public String logEvent(HttpSession session) {
        return "redirect:/home";
    }

    @PostMapping("/doLogEvent")
    public String doLogEvent(HttpSession session) {
        return "redirect:/home";
    }
}
