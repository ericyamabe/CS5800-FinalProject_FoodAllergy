package com.example.servingwebcontent.user;

import com.foodallergy.app.user.UserEntity;
import com.foodallergy.app.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(user);

        UserEntity result = userRepository.findByUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepository).findByUsername("john");
    }

    @Test
    void testFindById() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        when(userRepository.findById(1)).thenReturn(user);

        UserEntity result = userRepository.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        verify(userRepository).findById(1);
    }
}
