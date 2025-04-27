package com.deliveryfood.service;

import com.deliveryfood.dto.UserRequestDTO;
import com.deliveryfood.dto.UserResponseDTO;
import com.deliveryfood.entity.User;
import com.deliveryfood.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setPhone("(11) 99999-9999");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        requestDTO = new UserRequestDTO(
            "John Doe",
            "john@example.com",
            "password123",
            "(11) 99999-9999"
        );
    }

    @Test
    void createUser_ShouldReturnUserResponseDTO() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(requestDTO);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getPhone(), response.phone());

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenEmailExists_ShouldThrowException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> 
            userService.createUser(requestDTO)
        );

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUser_ShouldReturnUserResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUser(1L);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getPhone(), response.phone());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_WhenNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            userService.getUser(1L)
        );

        verify(userRepository).findById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserResponseDTO() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(user.getId(), responses.getFirst().id());
        assertEquals(user.getName(), responses.getFirst().name());
        assertEquals(user.getEmail(), responses.getFirst().email());

        verify(userRepository).findAll();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.updateUser(1L, requestDTO);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(requestDTO.name(), response.name());
        assertEquals(requestDTO.email(), response.email());
        assertEquals(requestDTO.phone(), response.phone());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            userService.updateUser(1L, requestDTO)
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenExists_ShouldReturnTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenNotExists_ShouldReturnFalse() {
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result = userService.deleteUser(1L);

        assertFalse(result);
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
}