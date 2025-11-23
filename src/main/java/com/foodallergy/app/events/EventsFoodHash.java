package com.foodallergy.app.events;

import jakarta.persistence.*;

@Entity
@Table(name="events_food_hash")
public class EventsFoodHash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="event_id")
    private int eventId;

    @Column(name="food_id")
    private int foodId;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}
