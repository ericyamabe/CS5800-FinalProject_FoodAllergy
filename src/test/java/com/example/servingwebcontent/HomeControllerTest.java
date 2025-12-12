package com.foodallergy.app.home;

import com.foodallergy.app.events.EventLogRepository;
import com.foodallergy.app.events.Events;
import com.foodallergy.app.events.EventsEntity;
import com.foodallergy.app.events.EventsRepository;
import com.foodallergy.app.food.Food;
import com.foodallergy.app.food.FoodLogRepository;
import com.foodallergy.app.food.FoodRepository;
import com.foodallergy.app.food.FoodsHelper;
import com.foodallergy.app.user.User;
import com.foodallergy.app.user.UserEntity;
import com.foodallergy.app.user.UserFactory;
import com.foodallergy.app.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private EventsRepository eventsRepository;
    @MockBean private FoodRepository foodRepository;
    @MockBean private EventLogRepository eventLogRepository;
    @MockBean private FoodLogRepository foodLogRepository;
    @MockBean private UserRepository userRepository;

    @Test
    void testHome_redirectsWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testHome_loadsPageSuccessfully() throws Exception {
        int userId = 5;
        String username = "testUser";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", username);
        session.setAttribute("userId", userId);

        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setUsername(username);
        mockUserEntity.setUserId(userId);
        mockUserEntity.setPassword("mypassword");
        mockUserEntity.setName("John");
        mockUserEntity.setPermission("admin");

        when(userRepository.findById(userId)).thenReturn(mockUserEntity);

        List<EventsEntity> mockEvents = List.of(new EventsEntity());
        Events eventsSingleton = Events.getInstance();

        Events eventsSpy = spy(eventsSingleton);
        doReturn(mockEvents)
                .when(eventsSpy)
                .getEventsIdsByCountByUserId(eventLogRepository, eventsRepository, userId);

        Food milk = new Food();
        milk.setName("milk");
        milk.setUserId(userId);
        List<Food> foods = new ArrayList<Food>();
        foods.add(milk);

        when(mock(Food.class).getName()).thenReturn("Milk");

        var foodsHelperSpy = spy(FoodsHelper.getInstance());
        doReturn(foods)
                .when(foodsHelperSpy)
                .getMostCommonByUserId(foodLogRepository, foodRepository, userId);

        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("pageTitle"))
                .andExpect(model().attribute("username", "testUser"))
                .andExpect(model().attribute("permissionLevel", "admin"));
    }

    @Test
    void testErrorPage() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}
