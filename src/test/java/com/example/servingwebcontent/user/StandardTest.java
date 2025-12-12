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
class StandardTest {

    @Mock
    private UserEntity userEntity;

    private Standard standard;

    @BeforeEach
    void setUp() {
        standard = new Standard(userEntity);
    }

    @Test
    void getUserId_delegatesToUserEntity() {
        when(userEntity.getUserId()).thenReturn(1);

        assertEquals(1, standard.getUserId());
        verify(userEntity).getUserId();
    }

    @Test
    void getName_delegatesToUserEntity() {
        when(userEntity.getName()).thenReturn("Standard User");

        assertEquals("Standard User", standard.getName());
        verify(userEntity).getName();
    }

    @Test
    void setName_delegatesToUserEntity() {
        standard.setName("Standard User");

        verify(userEntity).setName("Standard User");
    }

    @Test
    void getPermission_delegatesToUserEntity() {
        when(userEntity.getPermission()).thenReturn("standard");

        assertEquals("standard", standard.getPermission());
        verify(userEntity).getPermission();
    }
}
