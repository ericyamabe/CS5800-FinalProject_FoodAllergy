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
    private FoodRepository foodRepository;

    @Autowired
    EventLogRepository eventLogRepository;

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

        List<Object> eventsLog = eventLogRepository.getEventsWithCountByUserId(userId);
        ArrayList<Events> events = new ArrayList<Events>();

        for (Object eventLog : eventsLog) {
            Events event = eventsRepository.findById(Integer.parseInt(eventLog.toString()));
            events.add(event);
        }

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
        Events event = eventsRepository.findById(eventId);
        ArrayList<String> occurences = new ArrayList<String>();
        List<EventLog> eventLogs = eventLogRepository.findByEventId(eventId);

        for (EventLog eventLog : eventLogs) {
            occurences.add(eventLog.getDateOccured().toString());
        }

        model.addAttribute("pageTitle", "Event Details");
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("occurenceDates", occurences);

        String username = session.getAttribute("username").toString();
        return "event-detail";
    }

    @GetMapping("/events/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Add Event");
        return "logEvent";
    }

    @PostMapping("/events/doAdd")
    public String doAdd(@RequestParam("name") String name,
                        @RequestParam("eventDate") String eventDate,
                        @RequestParam(value = "associateFood", defaultValue = "No") String associateFood) {
        int userId = (int) session.getAttribute("userId");
        Events event = new Events();
        event.setName(name);
        event.setUserId(userId);
        eventsRepository.save(event);

        EventLog eventLog = new EventLog();
        eventLog.setEventId(event.getId());
        eventLog.setUserId(userId);
        eventLog.setDateOccured(eventDate);
        eventLogRepository.save(eventLog);

        if(associateFood.equals("yes")) {
            return "redirect:/events/addFood/" + event.getId();
        }
        return "redirect:/events";
    }

    @PostMapping("/events/occurence/add")
    public String addOccurence(@RequestParam("eventId") int eventId, @RequestParam("occurrenceDate") String occurrenceDate) {
        int userId = (int) session.getAttribute("userId");
        EventLog eventLog = new EventLog();
        eventLog.setEventId(eventId);
        eventLog.setUserId(userId);
        eventLog.setDateOccured(occurrenceDate);
        eventLogRepository.save(eventLog);

        return "redirect:/events/details/" + eventId;
    }
}
