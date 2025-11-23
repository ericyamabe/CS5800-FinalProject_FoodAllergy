package com.foodallergy.app.events;

import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodRepository;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EventsController {
    @Autowired
    private HttpSession session;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventsFoodHashRepository eventsFoodHashRepository;

    @Autowired
    private EventsMealHashRepository eventsMealHashRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/events")
    public String eventsHome(Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        String username = session.getAttribute("username").toString();
        int userId = (int) session.getAttribute("userId");

        if(username == null) {
            return "redirect:/login?error_msg=You must be logged in";
        }

        List<Events> events = eventsRepository.findByUserId(userId);

        model.addAttribute("pageTitle", "Events Home");
        model.addAttribute("username", username);
        model.addAttribute("events", events);

        return "events";
    }

    @GetMapping("/events/details/{eventId}")
    public String detail(@PathVariable("eventId") int eventId, Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        int userId = (int) session.getAttribute("userId");
        ArrayList<Food> foodsList = new ArrayList<Food>();
        List<EventsFoodHash> foods = eventsFoodHashRepository.findByEventId(eventId);

        List<Food> usersFoods = foodRepository.findByUserId(userId);
        model.addAttribute("foods", usersFoods);

        for (EventsFoodHash food : foods) {
            Food foodItem = foodRepository.findById(food.getFoodId());

            if(foodItem != null) {
                foodsList.add(foodItem);
            }
        }

        model.addAttribute("pageTitle", "Event Details");
        model.addAttribute("foods", foodsList);
        model.addAttribute("usersFoods", usersFoods);
        model.addAttribute("eventId", eventId);

        String username = session.getAttribute("username").toString();
        return "event-detail";
    }

    @GetMapping("/events/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Event");
        return "logEvent";
    }

    @PostMapping("/events/doAdd")
    public String doAdd(@RequestParam("name") String name, @RequestParam("associateFood") String associateFood) {
        int userId = (int) session.getAttribute("userId");
        Events event = new Events();
        event.setName(name);
        event.setUserId(userId);
        eventsRepository.save(event);

        if(associateFood.equals("yes")) {
            return "redirect:/events/addFood/" + event.getId();
        }
        return "redirect:/events";
    }

    @GetMapping("/events/addFood/{eventId}")
    public String addFood(@PathVariable("eventId") int eventId, Model model) {
        int userId = (int) session.getAttribute("userId");
        List<Food> foods = foodRepository.findByUserId(userId);

        model.addAttribute("pageTitle", "Associate Food With Event");
        model.addAttribute("eventId", eventId);
        model.addAttribute("foods", foods);
        return "associateFoodWithEvent";
    }

    @PostMapping("/events/associate")
    public String doAssociate(@RequestParam("eventId") int eventId, @RequestParam("foodIds") List<Integer> foodIds) {
        for(int foodId : foodIds) {
            EventsFoodHash foodHash = new EventsFoodHash();
            foodHash.setEventId(eventId);
            foodHash.setFoodId(foodId);
            eventsFoodHashRepository.save(foodHash);
        }

        return "redirect:/events/details/" + eventId;
    }
}
