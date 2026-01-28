# Testing Process Report: User Management Module

## 1. Module Overview (Summary)
**Module Name:** User Management
**Class:** `vn.hoidanit.laptopshop.controller.admin.UserController`

### Functionality
This module is responsible for managing users in the Admin dashboard. Key functions include:
1.  **List Users:** Display a table of all users (`/admin/user`).
2.  **View Detail:** Show specific user information (`/admin/user/view/{id}`).
3.  **Create User:** Handle form submission to add a new user (`/admin/user/create`).
4.  **Update User:** Edit existing user details (`/admin/user/update/{id}`).
5.  **Delete User:** Remove a user from the system (`/admin/user/delete/{id}`).

### Complexity Metrics
*   **Items (Fields):** 8 items (ID, Email, Password, FullName, Address, Phone, Avatar, Role).
*   **Transactions:**
    1.  Form Validation (Spring Validation).
    2.  File Upload (Avatar image processing).
    3.  Password Hashing (BCrypt).
    4.  Database Persistence (Save/Update/Delete via Service).
    5.  Role Assignment.

---

## 2. Issue Discovery (Bug Hunting)
**Tool Used:** JUnit 5 with Mockito (Unit Testing).

### The Issue: NullPointerException in `createUser`
*   **Description:** When a user submits the creation form without selecting a **Role** (or if the role data is null), the system attempts to access `user.getRole().getName()`.
*   **Consequence:** Since `user.getRole()` is null, calling `.getName()` throws a `NullPointerException`, causing a 500 Internal Server Error crash.

### Design Test Case (Before Fix)
We wrote a test case specifically to reproduce this crash. The test passes if it successfully catches the exception, confirming the bug exists.

```java
// File: UserControllerTest.java

// BUG HUNTING: Test case to confirm the system crashes when Role is null
@Test
public void testCreateUser_WhenRoleIsNull_ShouldThrowNullPointerException() {
    // Arrange
    User user = new User();
    user.setPassword("123456");
    user.setRole(null); // SIMULATE BUG: No role provided

    BindingResult bindingResult = mock(BindingResult.class);
    MultipartFile file = mock(MultipartFile.class);

    // Mock dependencies
    when(bindingResult.hasErrors()).thenReturn(false);
    when(uploadService.handleSaveUploadFile(file, "Avatar")).thenReturn("test.jpg");
    when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");

    // Act & Assert
    // We expect the system to throw NullPointerException
    assertThrows(NullPointerException.class, () -> {
        userController.createUser(model, user, bindingResult, file);
    });
}
```

**Result:** ✅ Test Passed (The exception was thrown as expected -> **Bug Confirmed**).

---

## 3. Bug Fixing & Verification
### The Solution
We added a defensive check (`if` statement) to ensure `user.getRole()` is not null before accessing its properties.

### Fixed Code (Controller)
```java
// File: UserController.java

// ... existing code ...
user.setAvatar(avatarName);
user.setPassword(hashPassword);

// FIX: Check for null before accessing role name
if (user.getRole() != null) {
    user.setRole(this.userService.findRoleByName(user.getRole().getName()));
}

this.userService.handleSaveUser(user);
// ... existing code ...
```

### Verification Test Case (After Fix)
We updated the test case to expect a **success** outcome (redirect) instead of an exception.

```java
// File: UserControllerTest.java

// VERIFICATION: Confirm the system runs successfully even with null Role
@Test
public void testCreateUser_WhenRoleIsNull_ShouldRunSuccessfully() {
    // Arrange
    User user = new User();
    user.setPassword("123456");
    user.setRole(null); // Input is still null

    BindingResult bindingResult = mock(BindingResult.class);
    MultipartFile file = mock(MultipartFile.class);

    when(bindingResult.hasErrors()).thenReturn(false);
    when(uploadService.handleSaveUploadFile(file, "Avatar")).thenReturn("test.jpg");
    when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");

    // Act
    String viewName = userController.createUser(model, user, bindingResult, file);

    // Assert
    // The system should NOT crash, and should redirect to the user list
    assertEquals("redirect:/admin/user", viewName);
    verify(userService).handleSaveUser(user);
}
```

**Result:** ✅ Test Passed (Green checkmark).
**Conclusion:** The module is now robust against missing Role data. The bug is resolved.
