# Quá trình Kiểm thử và Tối ưu hóa Module Quản lý Người dùng

## 1. Giới thiệu Module (Summary)
**Module:** Quản lý người dùng (User Management)
**Thành phần trọng tâm:** `vn.hoidanit.laptopshop.service.UserService`

### Chức năng và Đặc điểm:
*   **Chức năng chính:** Xử lý logic lưu mới (`handleSaveUser`) và cập nhật (`handleUpdateUser`) thông tin người dùng.
*   **Đặc điểm kiến trúc:** Đã thực hiện Refactor để đưa toàn bộ logic nghiệp vụ (Business Logic) từ Controller xuống Service. Controller hiện chỉ đóng vai trò điều hướng.
*   **Thông số (Complexity):**
    *   **Fields (Items):** 8 trường thông tin (Email, Password, Name, Address, Phone, Age, Avatar, Role).
    *   **Giao dịch (Transactions):** 5 bước xử lý: Kiểm tra user tồn tại, Băm mật khẩu (BCrypt), Gán quyền (Role), Lưu dữ liệu (JPA), và Điều hướng.

---

## 2. Tìm lỗi bằng JUnit (Bug Hunting)
**Công cụ:** JUnit 5 kết hợp Mockito.

### Lỗi phát hiện: NullPointerException (NPE)
*   **Vị trí:** Hàm `handleSaveUser` trong `UserService`.
*   **Nguyên nhân:** Khi tạo mới người dùng nhưng dữ liệu `Role` không được cung cấp (null), hệ thống cố gắng gọi `user.getRole().getName()`.
*   **Hậu quả:** Hệ thống crash ngay lập tức, trả về lỗi 500 cho người dùng.

### Test Case minh họa lỗi (Trước khi sửa):
```java
@Test
public void testHandleSaveUser_WhenRoleIsNull_ShouldThrowNullPointerException() {
    // Chuẩn bị dữ liệu rỗng Role
    User user = new User();
    user.setRole(null); 

    // Thực thi và kiểm tra: Mong đợi ném ra lỗi NPE
    assertThrows(NullPointerException.class, () -> {
        userService.handleSaveUser(user);
    });
}
```
**Kết quả:** Test **PASS** (Xác nhận code đang bị lỗi NullPointerException).

---

## 3. Sửa lỗi và Kiểm chứng (Fix & Demo)

### Cách sửa lỗi:
Thêm kiểm tra điều kiện (Null Check) cho đối tượng Role trước khi xử lý logic gán quyền.

### Mã nguồn sau khi sửa (Fix Code):
```java
public User handleSaveUser(User user) {
    // ... hash password ...

    // GIẢI PHÁP: Thêm check null
    if (user.getRole() != null && user.getRole().getName() != null) {
        Role r = this.findRoleByName(user.getRole().getName());
        user.setRole(r);
    }

    return this.userRepository.save(user);
}
```

### Kiểm chứng kết quả (Sau khi sửa):
Cập nhật Test Case để mong đợi hệ thống chạy mượt mà thay vì gặp lỗi.

```java
@Test
public void testHandleSaveUser_WhenRoleIsNull_ShouldRunSuccessfully() {
    User user = new User();
    user.setRole(null); // Vẫn test với dữ liệu lỗi

    // Hiện tại: Chạy thành công, không crash
    User result = userService.handleSaveUser(user);
    assertNotNull(result);
}
```
**Kết quả:** Test chạy thành công. Module đã trở nên bền bỉ, không còn bị crash khi dữ liệu đầu vào không hoàn hảo.

---

## 4. Kết luận
*   Việc đưa logic vào **Service** giúp mã nguồn sạch sẽ, dễ bảo trì và quan trọng nhất là dễ dàng thực hiện **Unit Test**.
*   Kiểm thử tự động giúp phát hiện sớm các lỗi "chết người" như `NullPointerException` trước khi ứng dụng được triển khai đến người dùng cuối.
*   Quá trình: **Viết Test -> Phát hiện lỗi -> Sửa code -> Chạy lại Test** là quy trình chuẩn để đảm bảo chất lượng phần mềm.
