package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ui.Model;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUser = this.userService.getAllUserByEmail("receptionist1@clinic.com");
        System.out.println(arrUser);
        return "hello";
    }

    // @RequestMapping("/admin/user")
    // public String getAdminHome(Model model) {
    // model.addAttribute("newUser", new User());
    // return "admin/user/create";
    // }

    // @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    // public String createUser(Model model, @ModelAttribute("newUser") User user) {
    // System.out.println("Creating user..." + user);
    // this.userService.handleSaveUser(user);
    // return "hello";
    // }

}
