package com.foodallergy.app.events;

import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodLogRepository;
import com.foodallergy.app.food.FoodRepository;
import com.foodallergy.app.food.FoodsHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventsController {
    Events events = Events.getInstance();
    FoodsHelper foodsHelper = FoodsHelper.getInstance();

    @Autowired
    private HttpSession session;

    @Autowired
    EventLogRepository eventLogRepository;

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodLogRepository foodLogRepository;

    @GetMapping("/events")
    public String eventsHome(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/login";
        }

        String username = getSessionUsername();
        int userId = getSessionUserId();
        List<EventsEntity> events = this.events.getEventsIdsByCountByUserId(eventLogRepository, eventsRepository, userId);

        model.addAttribute("pageTitle", "Events Home");
        model.addAttribute("username", username);
        model.addAttribute("events", events);

        return "events";
    }

    @GetMapping("/events/details/{eventId}")
    public String detail(@PathVariable("eventId") int eventId, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/login";
        }

        int userId = getSessionUserId();
        EventsEntity event = events.findEventById(eventsRepository, eventId);
        ArrayList<LocalDate> eventDates = events.findEventLogDatesByEventId(eventLogRepository, eventId);
        ArrayList<Food> foods = foodsHelper.getFoodMostCommonByDates(foodLogRepository, foodRepository, eventDates, userId);

        model.addAttribute("pageTitle", "Event Details");
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("occurenceDates", eventDates);
        model.addAttribute("foods", foods);

        return "event-detail";
    }

    @GetMapping("/events/add")
    public String add(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/login";
        }
        model.addAttribute("pageTitle", "Add Event");
        return "logEvent";
    }

    @PostMapping("/events/doAdd")
    public String doAdd(@RequestParam("name") String name, @RequestParam("eventDate") String eventDate) {
        if (!isLoggedIn()) {
            return "redirect:/login";
        }

        int userId = getSessionUserId();
        EventsEntity event = new EventsEntity().setName(name).setUserId(userId).save(eventsRepository);
        EventLog eventLog = new EventLog().setEventId(event.getId()).setUserId(userId).setDateOccured(eventDate)
                .save(eventLogRepository);

        return "redirect:/events";
    }

    @PostMapping("/events/occurence/add")
    public String addOccurence(
            @RequestParam("eventId") int eventId,
            @RequestParam("occurrenceDate") String occurrenceDate) {
        if (!isLoggedIn()) {
            return "redirect:/login";
        }

        int userId = getSessionUserId();
        EventLog eventLog = new EventLog().setEventId(eventId).setUserId(userId).setDateOccured(occurrenceDate)
                .save(eventLogRepository);

        return "redirect:/events/details/" + eventId;
    }

    public boolean isLoggedIn() {
        return session.getAttribute("username") != null;
    }

    public String getSessionUsername() {
        return session.getAttribute("username").toString();
    }

    public int getSessionUserId() {
        return (int) session.getAttribute("userId");
    }
}
