package com.foodallergy.app.home;

import com.foodallergy.app.events.EventLogRepository;
import com.foodallergy.app.events.Events;
import com.foodallergy.app.events.EventsRepository;
import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodLogRepository;
import com.foodallergy.app.food.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private HttpSession session;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private EventLogRepository eventLogRepository;
    @Autowired
    private FoodLogRepository foodLogRepository;

    @GetMapping("/home")
    public String home(Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        String username = session.getAttribute("username").toString();
        int userId = (int) session.getAttribute("userId");

        List<Object> eventsLog = eventLogRepository.getEventsIdsByCountByUserId(userId);
        ArrayList<Events> events = new ArrayList<Events>();
        ArrayList<Food> foods = new ArrayList<Food>();

        for (Object eventLog : eventsLog) {
            Events event = eventsRepository.findById(Integer.parseInt(eventLog.toString()));
            events.add(event);
        }

        List<Object> foodsList = foodLogRepository.getFoodMostCommonByUserId(userId);

        for (Object foodLog : foodsList) {
            Food food = foodRepository.findById(Integer.parseInt(foodLog.toString()));
            foods.add(food);
        }

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
