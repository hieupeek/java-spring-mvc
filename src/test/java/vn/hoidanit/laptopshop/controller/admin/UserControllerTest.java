package vn.hoidanit.laptopshop.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

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

@ExtendWith(MockitoExtension.class)
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

    // --- CÁC TEST CASE CŨ (HAPPY PATH) ---
    @Test
    public void testGetAdminHome() {
        List<User> listUser = new ArrayList<>();
        listUser.add(new User());
        when(userService.getAllUser()).thenReturn(listUser);
        String viewName = userController.getAdminHome(model);
        assertEquals("admin/user/show", viewName);
        verify(model).addAttribute("listUser", listUser);
    }

    @Test
    public void testGetUserDetailPage() {
        long id = 1L;
        User user = new User();
        when(userService.getUserById(id)).thenReturn(user);
        String viewName = userController.getUserDetailPage(model, id);
        assertEquals("admin/user/detail", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testGetCreateUserPage() {
        String viewName = userController.getCreateUserPage(model);
        assertEquals("admin/user/create", viewName);
        verify(model).addAttribute(eq("newUser"), any(User.class));
    }

    // --- CÁC TEST CASE TÌM LỖI (BUG HUNTING) ---

    // BUG 1: Kiểm thử trường hợp Role bị Null khi tạo User
    // Kỳ vọng: Code nên có lỗi NullPointerException vì logic
    // user.getRole().getName() không kiểm tra null
    @Test
    public void testCreateUser_WhenRoleIsNull_ShouldThrowNullPointerException() {
        // Arrange
        User user = new User();
        user.setPassword("123456");
        user.setRole(null); // GIẢ LẬP LỖI: Không có role

        BindingResult bindingResult = mock(BindingResult.class);
        MultipartFile file = mock(MultipartFile.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(uploadService.handleSaveUploadFile(file, "Avatar")).thenReturn("test.jpg");
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");

        // Act & Assert
        // Chúng ta mong đợi một ngoại lệ NullPointerException sẽ được ném ra ở đây
        // Nếu test này PASS, nghĩa là code của bạn ĐANG CÓ LỖI (không xử lý null)
        assertThrows(NullPointerException.class, () -> {
            userController.createUser(model, user, bindingResult, file);
        });
    }

    // BUG HUNTING 2: Update User không tồn tại
    // Kỳ vọng: Code xử lý an toàn, không crash, chỉ redirect về trang chủ
    @Test
    public void testUpdateUser_WhenUserNotFound_ShouldDoNothingAndRedirect() {
        // Arrange
        User user = new User();
        user.setId(999L); // ID không tồn tại
        BindingResult bindingResult = mock(BindingResult.class);
        MultipartFile file = mock(MultipartFile.class);

        when(userService.getUserById(999L)).thenReturn(null); // Service trả về null

        // Act
        String viewName = userController.updateUser(model, user, bindingResult, file);

        // Assert
        assertEquals("redirect:/admin/user", viewName);
        // Quan trọng: Phải đảm bảo KHÔNG GỌI hàm save user
        verify(userService, never()).handleSaveUser(any());
    }

    // BUG HUNTING 3: Update User với các trường rỗng
    // Kỳ vọng: Các trường cũ không bị ghi đè bởi chuỗi rỗng
    @Test
    public void testUpdateUser_WithEmptyFields_ShouldNotUpdateThoseFields() {
        // Arrange
        User inputUser = new User();
        inputUser.setId(1L);
        inputUser.setFullName(""); // Tên rỗng
        inputUser.setAddress(null); // Địa chỉ null

        User databaseUser = new User();
        databaseUser.setId(1L);
        databaseUser.setFullName("Old Name");
        databaseUser.setAddress("Old Address");

        BindingResult bindingResult = mock(BindingResult.class);
        MultipartFile file = mock(MultipartFile.class);

        when(userService.getUserById(1L)).thenReturn(databaseUser);
        when(file.isEmpty()).thenReturn(true);

        // Act
        userController.updateUser(model, inputUser, bindingResult, file);

        // Assert
        // Logic trong Controller check: if (user.getFullName() != null &&
        // !user.getFullName().isEmpty())
        // Nên databaseUser vẫn phải giữ nguyên giá trị cũ
        assertEquals("Old Name", databaseUser.getFullName());
        assertEquals("Old Address", databaseUser.getAddress());

        verify(userService).handleSaveUser(databaseUser);
    }

    @Test
    public void testDeleteUser() {
        long id = 1L;
        String viewName = userController.deleteUser(id);
        assertEquals("redirect:/admin/user", viewName);
        verify(userService).deleteUserById(id);
    }
}
