package com.foodallergy.app.registration;

import com.foodallergy.app.user.UserEntity;
import com.foodallergy.app.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @GetMapping("/register")
    public void register(Model model) {
    }

    @PostMapping("/doRegister")
    public String doRegister(@RequestParam("name") String name,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password, Model model) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        this.userRepository.save(user);

        String registration_message;

        if (session.getAttribute("registration_status") != null) {
            if(session.getAttribute("registration_status").equals("success")) {
                registration_message = "Your account has been created. Please login to continue";
                model.addAttribute("registration_message", registration_message);
            }
        }

        return "login";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
