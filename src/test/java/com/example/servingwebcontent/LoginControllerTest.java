package com.foodallergy.app.login;

import com.foodallergy.app.user.UserEntity;
import com.foodallergy.app.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loginPage_whenNotLoggedIn_returnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void loginPage_whenLoggedIn_redirectsToHome() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "testUser");

        mockMvc.perform(get("/login").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void doLogin_whenUserDoesNotExist_redirectsWithError() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("john");
        mockUser.setPassword("pass123");
        mockUser.setName("John");
        mockUser.setUserId(5);
        mockUser.setPermission("admin");

        when(userRepository.findByUsername("john")).thenReturn(mockUser);

        mockMvc.perform(post("/doLogin")
                        .param("username", "john")
                        .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void doLogin_whenPasswordIncorrect_redirectsWithError() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("john");
        mockUser.setPassword("correctpw");
        mockUser.setName("John");
        mockUser.setUserId(5);
        mockUser.setPermission("admin");

        when(userRepository.findByUsername("john")).thenReturn(mockUser);

        mockMvc.perform(post("/doLogin")
                        .param("username", "john")
                        .param("password", "wrongpw"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error_msg=Username or password is incorrect"));
    }

    @Test
    void doLogin_whenCredentialsValid_redirectsToHome() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("john");
        mockUser.setUserId(5);
        mockUser.setPassword("mypassword");
        mockUser.setName("John");
        mockUser.setPermission("admin");

        when(userRepository.findByUsername("john")).thenReturn(mockUser);

        mockMvc.perform(post("/doLogin")
                        .param("username", "john")
                        .param("password", "mypassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    // -----------------------------------------------------
    // GET /logout
    // -----------------------------------------------------
    @Test
    void logout_shouldInvalidateSessionAndRedirect() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "john");

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
