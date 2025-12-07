package vn.hoidanit.laptopshop.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.ui.Model;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    private final UploadService uploadService;

    public UserController(UserService userService, UploadService uploadService) {
        this.userService = userService;
        this.uploadService = uploadService;
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
    public String createUser(Model model, @ModelAttribute("newUser") User user,
            @RequestParam("hoidanitFile") MultipartFile file) {

        System.out.println("Creating user..." + user);
        this.uploadService.handleSaveUploadFile(file, "Avatar");
        // this.userService.handleSaveUser(user);
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