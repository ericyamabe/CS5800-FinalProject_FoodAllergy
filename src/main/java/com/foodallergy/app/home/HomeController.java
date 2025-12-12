package com.foodallergy.app.home;

import com.foodallergy.app.events.EventLogRepository;
import com.foodallergy.app.events.Events;
import com.foodallergy.app.events.EventsEntity;
import com.foodallergy.app.events.EventsRepository;
import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodLogRepository;
import com.foodallergy.app.food.FoodRepository;
import com.foodallergy.app.food.FoodsHelper;
import com.foodallergy.app.user.User;
import com.foodallergy.app.user.UserEntity;
import com.foodallergy.app.user.UserFactory;
import com.foodallergy.app.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {
    Events events = Events.getInstance();
    FoodsHelper foodsHelper = FoodsHelper.getInstance();

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

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        String username = session.getAttribute("username").toString();
        int userId = (int) session.getAttribute("userId");
        UserEntity userEntity = (UserEntity) userRepository.findById(userId);
        UserFactory userFactory = new UserFactory();
        User user = userFactory.getUser(userEntity);
        HashMap permissions = user.getPermissions();
        String permissionLevel;

        if (permissions.get("admin") == Boolean.TRUE) {
            permissionLevel = "admin";
        } else {
            permissionLevel = "standard";
        }

        List<EventsEntity> eventsList = events.getEventsIdsByCountByUserId(eventLogRepository, eventsRepository, userId);
        List<Food> foods = foodsHelper.getMostCommonByUserId(foodLogRepository, foodRepository, userId);

        model.addAttribute("pageTitle", "Food Allergies Home Page");
        model.addAttribute("username", username);
        model.addAttribute("events", eventsList);
        model.addAttribute("foods", foods);
        model.addAttribute("permissionLevel", permissionLevel);
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
