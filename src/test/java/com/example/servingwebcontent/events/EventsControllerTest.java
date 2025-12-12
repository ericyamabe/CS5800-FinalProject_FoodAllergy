package com.foodallergy.app.events;

import com.foodallergy.app.food.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventsControllerTest {

    @Mock
    private HttpSession session;

    @Mock
    private EventLogRepository eventLogRepository;

    @Mock
    private EventsRepository eventsRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FoodLogRepository foodLogRepository;

    @InjectMocks
    private EventsController eventsController;

    // We override the singletons in the controller with mocks
    private Events eventsMock;
    private FoodsHelper foodsHelperMock;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ExtendedModelMap();

        eventsMock = mock(Events.class);
        foodsHelperMock = mock(FoodsHelper.class);

        // override the default singletons in the controller
        eventsController.events = eventsMock;
        eventsController.foodsHelper = foodsHelperMock;
    }

    @Test
    void eventsHome_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String viewName = eventsController.eventsHome(model);

        assertEquals("redirect:/login", viewName);
        verify(session).getAttribute("username");
    }

    @Test
    void eventsHome_whenLoggedIn_populatesModelAndReturnsEventsView() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(42);

        List<EventsEntity> eventsList = new ArrayList<>();
        EventsEntity e1 = new EventsEntity();
        e1.setId(1);
        e1.setName("Headache");
        eventsList.add(e1);

        when(eventsMock.getEventsIdsByCountByUserId(
                eventLogRepository, eventsRepository, 42))
                .thenReturn(eventsList);

        String viewName = eventsController.eventsHome(model);

        assertEquals("events", viewName);
        assertEquals("Events Home", model.getAttribute("pageTitle"));
        assertEquals("john", model.getAttribute("username"));
        assertSame(eventsList, model.getAttribute("events"));

        verify(eventsMock).getEventsIdsByCountByUserId(eventLogRepository, eventsRepository, 42);
    }

    @Test
    void detail_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String viewName = eventsController.detail(5, model);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void detail_whenLoggedIn_populatesModelAndReturnsEventDetailView() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(7);

        int eventId = 10;

        EventsEntity event = new EventsEntity();
        event.setId(eventId);
        event.setName("Hives");
        when(eventsMock.findEventById(eventsRepository, eventId)).thenReturn(event);

        ArrayList<LocalDate> dates = new ArrayList<>();
        dates.add(LocalDate.of(2023, 1, 1));
        dates.add(LocalDate.of(2023, 1, 2));
        when(eventsMock.findEventLogDatesByEventId(eventLogRepository, eventId)).thenReturn(dates);

        ArrayList<Food> foods = new ArrayList<>();
        Food food = mock(Food.class); // we just need a Food instance; behavior is hidden by FoodAdapter
        foods.add(food);
        when(foodsHelperMock.getFoodMostCommonByDates(
                foodLogRepository, foodRepository, dates, 7))
                .thenReturn(foods);

        when(eventsMock.getFoodRelatedToAllEventsCount(eq(foodLogRepository), anyInt(), eq(dates)))
                .thenReturn((float) 0.75);

        String viewName = eventsController.detail(eventId, model);

        assertEquals("event-detail", viewName);
        assertEquals("Event Details", model.getAttribute("pageTitle"));
        assertEquals(eventId, model.getAttribute("eventId"));
        assertEquals("Hives", model.getAttribute("eventName"));
        assertEquals(dates, model.getAttribute("occurenceDates"));

        Object foodsAttr = model.getAttribute("foods");
        assertNotNull(foodsAttr);
        assertTrue(foodsAttr instanceof ArrayList<?>);
        ArrayList<?> foodList = (ArrayList<?>) foodsAttr;
        assertEquals(1, foodList.size());
    }

    @Test
    void add_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String viewName = eventsController.add(model);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void add_whenLoggedIn_setsPageTitleAndReturnsLogEventView() {
        when(session.getAttribute("username")).thenReturn("john");

        String viewName = eventsController.add(model);

        assertEquals("logEvent", viewName);
        assertEquals("Add Event", model.getAttribute("pageTitle"));
    }

    @Test
    void doAdd_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String viewName = eventsController.doAdd("Headache", "2023-01-01");

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void doAdd_whenLoggedIn_createsEventAndRedirectsToEvents() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(99);

        when(eventsRepository.save(any(EventsEntity.class))).thenAnswer(invocation -> {
            EventsEntity e = invocation.getArgument(0);
            e.setId(123);
            return e;
        });

        when(eventLogRepository.save(any(EventLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String viewName = eventsController.doAdd("Headache", "2023-01-01");

        assertEquals("redirect:/events", viewName);
        verify(eventsRepository).save(any(EventsEntity.class));
        verify(eventLogRepository).save(any(EventLog.class));
    }

    @Test
    void addOccurence_whenNotLoggedIn_redirectsToLogin() {
        when(session.getAttribute("username")).thenReturn(null);

        String viewName = eventsController.addOccurence(10, "2023-02-01");

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void addOccurence_whenLoggedIn_savesEventLogAndRedirectsToDetails() {
        when(session.getAttribute("username")).thenReturn("john");
        when(session.getAttribute("userId")).thenReturn(50);

        when(eventLogRepository.save(any(EventLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int eventId = 10;
        String viewName = eventsController.addOccurence(eventId, "2023-02-01");

        assertEquals("redirect:/events/details/" + eventId, viewName);
        verify(eventLogRepository).save(any(EventLog.class));
    }

    @Test
    void isLoggedIn_returnsTrueWhenUsernameInSession() {
        when(session.getAttribute("username")).thenReturn("john");

        assertTrue(eventsController.isLoggedIn());
    }

    @Test
    void isLoggedIn_returnsFalseWhenNoUsernameInSession() {
        when(session.getAttribute("username")).thenReturn(null);

        assertFalse(eventsController.isLoggedIn());
    }

    @Test
    void getSessionUsername_returnsUsernameFromSession() {
        when(session.getAttribute("username")).thenReturn("john");

        assertEquals("john", eventsController.getSessionUsername());
    }

    @Test
    void getSessionUserId_returnsUserIdFromSession() {
        when(session.getAttribute("userId")).thenReturn(77);

        assertEquals(77, eventsController.getSessionUserId());
    }
}
