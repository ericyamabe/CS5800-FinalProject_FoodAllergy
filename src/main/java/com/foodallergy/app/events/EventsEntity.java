package com.foodallergy.app.events;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name="events")
public class EventsEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private int userId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EventsEntity setName(String name) {
        this.name = name;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public EventsEntity setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public EventsEntity save(EventsRepository eventsRepository) {
        return eventsRepository.save(this);
    }
}
