package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import org.springframework.ui.Model;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    private final UploadService uploadService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    // @RequestMapping("/")
    // public String getHomePage(Model model) {
    // List<User> listUser =
    // this.userService.getAllUserByEmail("receptionist1@clinic.com");
    // model.addAttribute("listUser", listUser);
    // return "viewUser";
    // }

    // view all user
    @GetMapping("/admin/user")
    public String getAdminHome(Model model) {
        List<User> listUser = this.userService.getAllUser();
        model.addAttribute("listUser", listUser);
        return "admin/user/show";
    }

    // view user detail
    @GetMapping("/admin/user/view/{id}")
    public String getUserDetailPage(Model model, @PathVariable Long id) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "admin/user/detail";
    }

    // get create user page
    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    // create user
    @PostMapping(value = "/admin/user/create")
    public String createUser(Model model,
            @ModelAttribute("newUser") @Valid User user,
            BindingResult newUserBindingResult,

            @RequestParam("hoidanitFile") MultipartFile file) {

        System.out.println("Creating user..." + user);

        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        // validate
        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }

        String avatarName = this.uploadService.handleSaveUploadFile(file, "Avatar");
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setAvatar(avatarName);
        user.setPassword(hashPassword);
        user.setRole(this.userService.findRoleByName(user.getRole().getName()));
        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    // get update user page
    @GetMapping("/admin/user/updateUser/{id}")
    public String getUpdateUserPage(Model model, @PathVariable Long id) {
        model.addAttribute("updateUser", this.userService.getUserById(id));
        return "admin/user/edit";
    }

    // update user
    @PostMapping(value = "/admin/user/updateUser")
    public String updateUser(Model model, @ModelAttribute("updateUser") User user) {
        System.out.println("Updating user..." + user);
        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    // delete user
    @GetMapping(value = "/admin/user/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        System.out.println("Deleting user...");
        this.userService.deleteUserById(id);
        return "redirect:/admin/user";
    }

}