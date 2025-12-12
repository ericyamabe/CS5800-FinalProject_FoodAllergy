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
class AdminTest {

    @Mock
    private UserEntity userEntity;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin(userEntity);
    }

    @Test
    void getUserId_delegatesToUserEntity() {
        when(userEntity.getUserId()).thenReturn(1);

        assertEquals(1, admin.getUserId());
        verify(userEntity).getUserId();
    }

    @Test
    void getName_delegatesToUserEntity() {
        when(userEntity.getName()).thenReturn("Admin User");

        assertEquals("Admin User", admin.getName());
        verify(userEntity).getName();
    }

    @Test
    void setName_delegatesToUserEntity() {
        admin.setName("Admin User");

        verify(userEntity).setName("Admin User");
    }

    @Test
    void getPermission_delegatesToUserEntity() {
        when(userEntity.getPermission()).thenReturn("admin");

        assertEquals("admin", admin.getPermission());
        verify(userEntity).getPermission();
    }
}
