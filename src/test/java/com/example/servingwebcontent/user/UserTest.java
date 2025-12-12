package com.foodallergy.app.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserTest {

    // Simple concrete implementation so we can instantiate User
    private static class TestUser extends User {
        public TestUser(UserEntity userEntity) {
            super(userEntity);
        }
    }

    @Mock
    private UserEntity userEntity;

    private User user;

    @BeforeEach
    void setUp() {
        user = new TestUser(userEntity);
    }

    @Test
    void getUserId_delegatesToUserEntity() {
        when(userEntity.getUserId()).thenReturn(42);

        Integer id = user.getUserId();

        assertEquals(42, id);
        verify(userEntity).getUserId();
    }

    @Test
    void getName_delegatesToUserEntity() {
        when(userEntity.getName()).thenReturn("John Doe");

        String name = user.getName();

        assertEquals("John Doe", name);
        verify(userEntity).getName();
    }

    @Test
    void getUsername_delegatesToUserEntity() {
        when(userEntity.getUsername()).thenReturn("johndoe");

        String username = user.getUsername();

        assertEquals("johndoe", username);
        verify(userEntity).getUsername();
    }

    @Test
    void getPassword_delegatesToUserEntity() {
        when(userEntity.getPassword()).thenReturn("secret");

        String password = user.getPassword();

        assertEquals("secret", password);
        verify(userEntity).getPassword();
    }

    @Test
    void getPermission_delegatesToUserEntity() {
        when(userEntity.getPermission()).thenReturn("ADMIN");

        String permission = user.getPermission();

        assertEquals("ADMIN", permission);
        verify(userEntity).getPermission();
    }

    @Test
    void setUserId_delegatesToUserEntity() {
        user.setUserId(99);

        verify(userEntity).setUserId(99);
    }

    @Test
    void setName_delegatesToUserEntity() {
        user.setName("Alice");

        verify(userEntity).setName("Alice");
    }

    @Test
    void setUsername_delegatesToUserEntity() {
        user.setUsername("alice");

        verify(userEntity).setUsername("alice");
    }

    @Test
    void setPassword_delegatesToUserEntity() {
        user.setPassword("pwd");

        verify(userEntity).setPassword("pwd");
    }

    @Test
    void setPermission_delegatesToUserEntity() {
        user.setPermission("USER");

        verify(userEntity).setPermission("USER");
    }

    @Test
    @SuppressWarnings("rawtypes")
    void getPermissions_returnsDefaultAdminFalse() {
        HashMap permissions = user.getPermissions();

        assertNotNull(permissions);
        assertTrue(permissions.containsKey("admin"));
        assertEquals(Boolean.FALSE, permissions.get("admin"));
        assertEquals(1, permissions.size());
    }
}
