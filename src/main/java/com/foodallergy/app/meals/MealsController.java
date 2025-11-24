package com.foodallergy.app.meals;

import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MealsController {
    @Autowired
    HttpSession session;

    @Autowired
    MealsRepository mealsRepository;

    @Autowired
    FoodRepository foodRepository;
    @Autowired
    private MealFoodHashRepository mealFoodHashRepository;

    @GetMapping("/meals")
    public String meals(Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        int userId = (Integer) session.getAttribute("userId");
        List<Meals> meals = mealsRepository.findByUserId(userId);

        model.addAttribute("pageTitle", "Meals Home");
        model.addAttribute("meals", meals);
        return "meals";
    }

    @GetMapping("/meals/add")
    public String logMeal(Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        model.addAttribute("pageTitle", "Add Meal");
        return "addMeal";
    }

    @PostMapping("/meals/doAdd")
    public String doLogMeal(@RequestParam("name") String name) {
        int userId = (Integer) session.getAttribute("userId");

        Meals meal = new Meals();
        meal.setUserId(userId);
        meal.setName(name);
        mealsRepository.save(meal);

        return "redirect:/meals/" + meal.getId();
    }

    @GetMapping("/meals/{mealId}")
    public String meals(Model model, @PathVariable int mealId) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        int userId = (Integer) session.getAttribute("userId");

        Meals meal = mealsRepository.findById(mealId);
        ArrayList<Food> associatedFoods = new ArrayList<>();
        List<Food> userFoods = foodRepository.findByUserId(userId);
        List<MealFoodHash> associatedHashedFoods = mealFoodHashRepository.findByMealId(mealId);

        for(MealFoodHash row : associatedHashedFoods) {
            Food food = foodRepository.findById(row.getFoodId());
            if(food != null) {
                associatedFoods.add(food);
            }
        }

        model.addAttribute("pageTitle", "Associate Foods");
        model.addAttribute("foods", userFoods);
        model.addAttribute("associatedFoods", associatedFoods);
        model.addAttribute("mealId", mealId);
        model.addAttribute("mealName", meal.getName());

        return "meal-details";
    }

    @PostMapping("/meals/doAssociateFood")
    public String doAssociateFood(@RequestParam("mealId")  int mealId, @RequestParam("foodIds") String foodIds) {
        String[] foods = foodIds.split(",");

        for(String foodId : foods) {
            MealFoodHash mealFoodHash = new MealFoodHash();
            mealFoodHash.setMealId(mealId);
            mealFoodHash.setFoodId(Integer.parseInt(foodId));
            mealFoodHashRepository.save(mealFoodHash);
        }

        return "redirect:/meals/" + mealId;
    }
}