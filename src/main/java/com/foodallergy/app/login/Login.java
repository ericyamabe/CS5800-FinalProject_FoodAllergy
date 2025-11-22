package com.foodallergy.app.login;

import com.foodallergy.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class Login {
    @Autowired
    private UserRepository userRepository;
}
