package com.foodallergy.app.events;

import jakarta.persistence.*;

@Entity
@Table(name="events_meal_hash")
public class EventsMealHash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="event_id")
    private int eventId;

    @Column(name="meal_id")
    private int mealId;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
}
