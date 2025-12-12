package com.foodallergy.app.food;

import jakarta.persistence.*;

@Entity
@Table(name="food")
public class Food {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="user_id")
    private int userId;

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public int getUserId(){
        return this.userId;
    }
}
