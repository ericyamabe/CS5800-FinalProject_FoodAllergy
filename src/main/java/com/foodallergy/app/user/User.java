package com.foodallergy.app.user;

import java.util.HashMap;

public abstract class User {
    protected UserEntity userEntity;

    public User(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Integer getUserId() {
        return this.userEntity.getUserId();
    }

    public String getName() {
        return this.userEntity.getName();
    }

    public String getUsername() {
        return this.userEntity.getUsername();
    }

    public String getPassword() {
        return this.userEntity.getPassword();
    }

    public String getPermission() {
        return this.userEntity.getPermission();
    }

    public void setUserId(Integer id) {
        this.userEntity.setUserId(id);
    }

    public void setName(String name) {
        this.userEntity.setName(name);
    }

    public void setUsername(String username) {
        this.userEntity.setUsername(username);
    }

    public void setPassword(String password) {
        this.userEntity.setPassword(password);
    }

    public void setPermission(String permission) {
        this.userEntity.setPermission(permission);
    }

    @Override
    public String toString() {
        return this.userEntity.toString();
    }

    public HashMap getPermissions() {
        HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
        permissions.put("admin", Boolean.FALSE);
        return permissions;
    }
}
