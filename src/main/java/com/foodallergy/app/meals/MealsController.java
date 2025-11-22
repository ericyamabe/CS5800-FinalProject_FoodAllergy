package com.foodallergy.app.meals;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MealsController {
    @GetMapping("/meals")
    public String meals(HttpSession session) {
        return "redirect:/home";
    }

    @GetMapping("/logMeal")
    public String logMeal(HttpSession session) {
        return "redirect:/home";
    }

    @PostMapping("/doLogMeal")
    public String doLogMeal(HttpSession session) {
        return "redirect:/home";
    }
}
