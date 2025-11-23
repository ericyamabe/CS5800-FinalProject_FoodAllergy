package com.foodallergy.app.events;

import jakarta.persistence.*;

@Entity
@Table(name="event_log")
public class EventLog {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="event_id")
    private int eventId;

    @Column(name="user_id")
    private int userId;

    @Column(name="date_occured")
    private String dateOccured;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDateOccured() {
        return dateOccured;
    }

    public void setDateOccured(String dateOccured) {
        this.dateOccured = dateOccured;
    }
}
