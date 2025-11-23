package com.foodallergy.app.home;

import com.foodallergy.app.events.Events;
import com.foodallergy.app.events.EventsRepository;
import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    HttpSession session;

    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/home")
    public String home(Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        String username = session.getAttribute("username").toString();
        int userId = (int) session.getAttribute("userId");

        List<Events> events = eventsRepository.findByUserId(userId);
        List<Food> foods = foodRepository.findByUserId(userId);

        model.addAttribute("pageTitle", "Food Allergies Home Page");
        model.addAttribute("username", username);
        model.addAttribute("events", events);
        model.addAttribute("foods", foods);
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
