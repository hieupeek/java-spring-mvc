package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Product;

@Controller
public class ProductController {

    @GetMapping("/admin/product")
    public String getProduct() {
        return "admin/product/show";
    }

    // get create product page
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable("id") Long id) {
        return "admin/product/update";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        return "admin/product/delete";
    }
}
