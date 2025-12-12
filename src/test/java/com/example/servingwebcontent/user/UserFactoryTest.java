package com.foodallergy.app.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserFactoryTest {

    @Test
    void testGetUser_Admin() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getPermission()).thenReturn("admin");

        UserFactory factory = new UserFactory();
        User user = factory.getUser(userEntity);

        assertNotNull(user);
        assertTrue(user instanceof Admin, "Expected Admin instance");
    }

    @Test
    void testGetUser_Standard() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getPermission()).thenReturn("standard");

        UserFactory factory = new UserFactory();
        User user = factory.getUser(userEntity);

        assertNotNull(user);
        assertTrue(user instanceof Standard, "Expected Standard instance");
    }

    @Test
    void testGetUser_AnyOtherPermissionBecomesStandard() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getPermission()).thenReturn("viewer");

        UserFactory factory = new UserFactory();
        User user = factory.getUser(userEntity);

        assertNotNull(user);
        assertTrue(user instanceof Standard, "Expected Standard instance for unknown permission");
    }
}