package com.foodallergy.app.login;

import com.foodallergy.app.user.*;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private HttpSession session;

    @GetMapping("/login")
    public String login(Model model) {
        if (session.getAttribute("username") != null) {
            return "redirect:/home";
        }

        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String username, @RequestParam String password) {
        UserEntity userEntity = (UserEntity) userRepository.findByUsername(username);
        UserFactory userFactory = new UserFactory();
        User user = userFactory.getUser(userEntity);

        if (userEntity == null) {
            session.setAttribute("errorMessage", "User or password is incorrect");
            return "redirect:/login";
        }

        if (!userEntity.getPassword().equals(password)) {
            session.setAttribute("errorMessage", "User or password is incorrect");
            return "redirect:/login?error_msg=Username or password is incorrect";
        }


        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getUserId());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/login";
    }
}
