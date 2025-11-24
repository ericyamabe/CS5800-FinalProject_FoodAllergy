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

    public EventLog setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public EventLog setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getDateOccured() {
        return dateOccured;
    }

    public EventLog setDateOccured(String dateOccured) {
        this.dateOccured = dateOccured;
        return this;
    }

    public EventLog save(EventLogRepository eventLogRepository) {
        return eventLogRepository.save(this);
    }
}
