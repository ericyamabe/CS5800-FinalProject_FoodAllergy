package com.foodallergy.app.food;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class FoodController {
    @GetMapping("/food")
    public String food(HttpSession session) {
        return "redirect:/home";
    }

    @GetMapping("/logFood")
    public String logFood(HttpSession session) {
        return "redirect:/home";
    }

    @PostMapping("doLogFood")
    public String doLogFood(HttpSession session) {
        return "redirect:/home";
    }
}
