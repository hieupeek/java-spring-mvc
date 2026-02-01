package vn.hoidanit.laptopshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.RoleRepository;
import vn.hoidanit.laptopshop.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testHandleSaveUser_Success() {
        // Arrange
        User user = new User();
        user.setPassword("123456");

        Role role = new Role();
        role.setName("USER");
        user.setRole(role);

        when(passwordEncoder.encode("123456")).thenReturn("hashed_password");
        when(roleRepository.findByName("USER")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        User result = userService.handleSaveUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("hashed_password", result.getPassword());
        assertEquals("USER", result.getRole().getName());
        verify(userRepository).save(user);
    }

    /**
     * BUG HUNTING: Kiểm tra trường hợp Role bị null.
     * Hiện tại handleSaveUser gọi user.getRole().getName() mà không check null.
     */
    @Test
    public void testHandleSaveUser_WhenRoleIsNull_ShouldThrowNullPointerException() {
        // Arrange
        User user = new User();
        user.setPassword("123456");
        user.setRole(null); // Giả lập trường hợp lỗi

        when(passwordEncoder.encode("123456")).thenReturn("hashed_password");

        // Act & Assert
        // Kỳ vọng ném ra NullPointerException vì code chưa được fix
        assertThrows(NullPointerException.class, () -> {
            userService.handleSaveUser(user);
        });
    }

    @Test
    public void testHandleUpdateUser_Success() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFullName("Old Name");

        User updatedInfo = new User();
        updatedInfo.setId(1L);
        updatedInfo.setFullName("New Name");

        Role newRole = new Role();
        newRole.setName("ADMIN");
        updatedInfo.setRole(newRole);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findByName("ADMIN")).thenReturn(newRole);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        User result = userService.handleUpdateUser(updatedInfo);

        // Assert
        assertEquals("New Name", result.getFullName());
        assertEquals("ADMIN", result.getRole().getName());
    }
}
