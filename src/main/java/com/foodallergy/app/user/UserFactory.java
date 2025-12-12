package com.foodallergy.app.user;

public class UserFactory {
    public User getUser(UserEntity userEntity) {
        String permission = userEntity.getPermission();
        if (permission.equals("admin")) {
            return new Admin(userEntity);
        }

        return new Standard(userEntity);
    }
}
