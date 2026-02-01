package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.RoleRepository;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public User handleSaveUser(User user) {
        // Logic nghiệp vụ: Hash mật khẩu và gán Role
        if (user.getPassword() != null) {
            String hashPassword = this.passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPassword);
        }

        Role r = this.findRoleByName(user.getRole().getName());
        user.setRole(r);

        return this.userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }

    public Role findRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public List<Role> getAllRole() {
        return this.roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.getUserById(user.getId());
        if (currentUser != null) {
            // Cập nhật các thông tin cơ bản
            currentUser.setFullName(user.getFullName());
            currentUser.setAddress(user.getAddress());
            currentUser.setPhone(user.getPhone());
            currentUser.setAge(user.getAge());

            // Cập nhật role
            if (user.getRole() != null) {
                currentUser.setRole(user.getRole());
            }

            // // UserController.java
            // if (user.getRole() != null) {
            // // Lấy Role đầy đủ từ DB thay vì dùng Role "rỗng" từ form
            // Role r = this.getRoleById(user.getRole().getId());
            // currentUser.setRole(r);
            // }

            // Lưu user (logic hash password và gán role chuẩn đã có trong service)
            return this.handleSaveUser(currentUser);
        }
        return null;
    }
}
