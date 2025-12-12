package com.foodallergy.app.registration;

import com.foodallergy.app.user.UserEntity;
import com.foodallergy.app.user.UserRepository;
import com.foodallergy.app.registration.States;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @GetMapping("/register")
    public void register(Model model) {
        States states = new States();
        StateIterator stateIterator = states.iterator();
        ArrayList<String> statesList = new ArrayList<String>();

        while(stateIterator.hasNext()) {
            statesList.add(stateIterator.next());
        }

        model.addAttribute("states", statesList);
    }

    @PostMapping("/doRegister")
    public String doRegister(@RequestParam("name") String name,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("state") String state, Model model) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setState(state);
        this.userRepository.save(user);

        String registration_message;
        registration_message = "Your account has been created. Please login to continue";
        model.addAttribute("registration_message", registration_message);

        return "login";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
