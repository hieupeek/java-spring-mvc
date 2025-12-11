package vn.hoidanit.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetailController {
    @GetMapping("/client/product/detail")
    public String detail() {
        return "client/product/detail";
    }
}
