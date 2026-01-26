package vn.hoidanit.laptopshop.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@ExtendWith(MockitoExtension.class) // Sử dụng JUnit 5 với Mockito
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UploadService uploadService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAdminHome() {
        // Arrange
        List<User> listUser = new ArrayList<>();
        listUser.add(new User());
        when(userService.getAllUser()).thenReturn(listUser);

        // Act
        String viewName = userController.getAdminHome(model);

        // Assert
        assertEquals("admin/user/show", viewName);
        verify(model).addAttribute("listUser", listUser);
    }

    @Test
    public void testGetUserDetailPage() {
        // Arrange
        long id = 1L;
        User user = new User();
        when(userService.getUserById(id)).thenReturn(user);

        // Act
        String viewName = userController.getUserDetailPage(model, id);

        // Assert
        assertEquals("admin/user/detail", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testGetCreateUserPage() {
        // Act
        String viewName = userController.getCreateUserPage(model);

        // Assert
        assertEquals("admin/user/create", viewName);
        verify(model).addAttribute(eq("newUser"), any(User.class));
    }

    @Test
    public void testCreateUser_Success() {
        // Arrange
        User user = new User();
        user.setPassword("123456");
        Role role = new Role();
        role.setName("USER");
        user.setRole(role);

        BindingResult bindingResult = mock(BindingResult.class);
        MultipartFile file = mock(MultipartFile.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(uploadService.handleSaveUploadFile(file, "Avatar")).thenReturn("test.jpg");
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        // userService.findRoleByName is called inside the controller to set the role
        when(userService.findRoleByName("USER")).thenReturn(new Role());

        // Act
        String viewName = userController.createUser(model, user, bindingResult, file);

        // Assert
        assertEquals("redirect:/admin/user", viewName);
        verify(userService).handleSaveUser(user);
    }

    @Test
    public void testCreateUser_HasErrors() {
        // Arrange
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        MultipartFile file = mock(MultipartFile.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = userController.createUser(model, user, bindingResult, file);

        // Assert
        assertEquals("admin/user/create", viewName);
        // Verify that save is NOT called
        verify(userService, org.mockito.Mockito.never()).handleSaveUser(any());
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        BindingResult bindingResult = mock(BindingResult.class);
        MultipartFile file = mock(MultipartFile.class);

        User currentUser = new User(); // User tồn tại trong DB
        when(userService.getUserById(1L)).thenReturn(currentUser);
        when(file.isEmpty()).thenReturn(true); // Giả sử không upload ảnh mới

        // Act
        String viewName = userController.updateUser(model, user, bindingResult, file);

        // Assert
        assertEquals("redirect:/admin/user", viewName);
        verify(userService).handleSaveUser(currentUser);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        long id = 1L;

        // Act
        String viewName = userController.deleteUser(id);

        // Assert
        assertEquals("redirect:/admin/user", viewName);
        verify(userService).deleteUserById(id);
    }
}
