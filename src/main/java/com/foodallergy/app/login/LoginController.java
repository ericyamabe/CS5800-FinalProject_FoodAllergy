package com.foodallergy.app.login;

import com.foodallergy.app.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import com.foodallergy.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public void login(Model model) {
        model.addAttribute("username", "");
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session) {
        User user = (User) userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/login?error_msg=User or password is incorrect";
        }

        if (!user.getPassword().equals(password)) {
            return "redirect:/login?error_msg=Username or password is incorrect";
        }

        session.setAttribute("username", user.getUsername());
        return "redirect:/home";
    }
}
