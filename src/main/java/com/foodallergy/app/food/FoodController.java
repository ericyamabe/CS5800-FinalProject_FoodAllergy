package com.foodallergy.app.food;

import com.foodallergy.app.events.EventLogRepository;
import com.foodallergy.app.events.Events;
import com.foodallergy.app.events.EventsRepository;
import com.foodallergy.app.events.FoodLog;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FoodController {
    @Autowired
    HttpSession session;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    FoodLogRepository foodLogRepository;

    @Autowired
    EventLogRepository eventLogRepository;
    @Autowired
    private EventsRepository eventsRepository;

    @GetMapping("/food")
    public String food(Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        int userId = (Integer) session.getAttribute("userId");
        List<Food> foods = foodRepository.findByUserId(userId);
        model.addAttribute("pageTitle", "Add Food");
        model.addAttribute("foods", foods);
        return "food";
    }

    @GetMapping("/food/details/{foodId}")
    public String foodDetails(@PathVariable("foodId") int foodId, Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        int userId = (Integer) session.getAttribute("userId");
        Food food = foodRepository.findById(foodId);
        List<FoodLog> foodLog = foodLogRepository.findByFoodId(foodId);
        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
        ArrayList<Events> events = new ArrayList<Events>();

        for (FoodLog log : foodLog) {
            dates.add(LocalDate.parse(log.getDateOccured().toString()));
        }

        List<Object> eventIds = eventLogRepository.getEventsByDates(dates, userId);

        for(Object eventId : eventIds) {
            Events event = eventsRepository.findById(Integer.parseInt(eventId.toString()));
            events.add(event);
        }

        model.addAttribute("pageTitle", "Details: " + food.getName());
        model.addAttribute("foodName", food.getName());
        model.addAttribute("foodLog", foodLog);
        model.addAttribute("events", events);
        return "food-details";
    }

    @GetMapping("/food/add")
    public String logFood(Model model) {
        int userId = (Integer) session.getAttribute("userId");
        List<Food> foods = foodRepository.findByUserId(userId);
        model.addAttribute("pageTitle", "Add Food");
        model.addAttribute("foods", foods);
        return "addFood.html";
    }

    @PostMapping("/food/doAddFood")
    public String doAddFood(@RequestParam(value = "existingId", defaultValue = "0") int existingId,
                            @RequestParam(value = "name", defaultValue = "") String name,
                            @RequestParam("date") String date) {
        int userId = (Integer) session.getAttribute("userId");

        if(existingId == 0) {
            Food food = new Food();
            food.setName(name);
            food.setUserId(userId);
            foodRepository.save(food);
            existingId = food.getId();
        }

        FoodLog foodLog = new FoodLog();
        foodLog.setFoodId(existingId);
        foodLog.setUserId(userId);
        foodLog.setDateOccured(date);
        foodLogRepository.save(foodLog);

        return "redirect:/food/details/" + existingId;
    }
}