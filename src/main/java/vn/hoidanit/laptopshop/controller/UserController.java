package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.ui.Model;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @RequestMapping("/")
    // public String getHomePage(Model model) {
    // List<User> listUser =
    // this.userService.getAllUserByEmail("receptionist1@clinic.com");
    // model.addAttribute("listUser", listUser);
    // return "viewUser";
    // }

    // view all user
    @RequestMapping("/admin/user")
    public String getAdminHome(Model model) {
        List<User> listUser = this.userService.getAllUser();
        model.addAttribute("listUser", listUser);
        return "admin/user/tableUser";
    }

    // view user detail
    @RequestMapping("/admin/user/view/{id}")
    public String getUserDetailPage(Model model, @PathVariable Long id) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "admin/user/viewUserDetail";
    }

    // get create user page
    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    // create user
    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createUser(Model model, @ModelAttribute("newUser") User user) {
        System.out.println("Creating user..." + user);
        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    // get update user page
    @GetMapping("/admin/user/updateUser/{id}")
    public String getUpdateUserPage(Model model, @PathVariable Long id) {
        model.addAttribute("updateUser", this.userService.getUserById(id));
        return "admin/user/editUser";
    }

    // update user
    @RequestMapping(value = "/admin/user/updateUser", method = RequestMethod.POST)
    public String updateUser(Model model, @ModelAttribute("updateUser") User user) {
        System.out.println("Updating user..." + user);
        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    // delete user
    @RequestMapping(value = "/admin/user/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        System.out.println("Deleting user...");
        this.userService.deleteUserById(id);
        return "redirect:/admin/user";
    }

}
