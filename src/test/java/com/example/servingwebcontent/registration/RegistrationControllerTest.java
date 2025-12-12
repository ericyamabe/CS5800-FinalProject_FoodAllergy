package com.foodallergy.app.registration;

import com.foodallergy.app.user.UserEntity;
import com.foodallergy.app.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private RegistrationController registrationController;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ExtendedModelMap();
    }

    @Test
    void register_addsStatesAttributeToModel() {
        registrationController.register(model);

        assertTrue(model.containsAttribute("states"));

        Object statesObj = model.getAttribute("states");
        assertNotNull(statesObj);
        assertTrue(statesObj instanceof ArrayList<?>);
    }

    @Test
    void doRegister_createsUser_savesIt_andReturnsLoginView() {
        String name = "John Doe";
        String username = "johndoe";
        String password = "secret";
        String state = "California";

        String viewName = registrationController.doRegister(
                name, username, password, state, model
        );

        assertEquals("login", viewName);

        assertTrue(model.containsAttribute("registration_message"));
        assertEquals(
                "Your account has been created. Please login to continue",
                model.getAttribute("registration_message")
        );

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        assertNotNull(savedUser);
        assertEquals(name, savedUser.getName());
        assertEquals(username, savedUser.getUsername());
        assertEquals(password, savedUser.getPassword());
        assertEquals(state, savedUser.getState());
    }
}
