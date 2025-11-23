package com.foodallergy.app.registration;

import com.foodallergy.app.user.User;
import com.foodallergy.app.user.UserRepository;
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

    @GetMapping("/register")
    public void register(Model model) {
    }

    @PostMapping("/doRegister")
    public String doRegister(@RequestParam("name") String name,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        this.userRepository.save(user);
        return "login";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
