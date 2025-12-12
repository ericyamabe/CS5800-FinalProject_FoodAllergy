package com.foodallergy.app.user;

import java.util.HashMap;

public class Admin extends User {
    public Admin(UserEntity userEntity) {
        super(userEntity);
    }

    public HashMap getPermissions() {
        HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
        permissions.put("admin", Boolean.TRUE);
        return permissions;
    }
}
