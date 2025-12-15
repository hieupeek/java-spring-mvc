package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final UploadService uploadService;

    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    // get all product
    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        model.addAttribute("listProduct", this.productService.getAllProduct());
        return "admin/product/show";
    }

    // view product detail
    @GetMapping("/admin/product/view/{id}")
    public String getProductDetailPage(Model model, @PathVariable Long id) {
        model.addAttribute("product", this.productService.getProductById(id));
        return "admin/product/detail";
    }

    // get create product page
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    // create product
    @PostMapping(value = "/admin/product/create")
    public String createProduct(Model model,
            @ModelAttribute("newProduct") @Valid Product product,

            BindingResult newProductBindingResult,

            @RequestParam("hoidanitFile") MultipartFile file) {

        System.out.println("Creating product..." + product);

        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        String avatarName = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(avatarName);
        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }

    // get update product page
    @GetMapping("/admin/product/updateProduct/{id}")
    public String getUpdateProductPage(Model model, @PathVariable Long id) {
        model.addAttribute("updateProduct", this.productService.getProductById(id));
        return "admin/product/edit";
    }

    // update product
    @PostMapping(value = "/admin/product/updateProduct")
    public String updateProduct(@ModelAttribute("updateProduct") @Valid Product product,
            BindingResult updateProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {
        System.out.println("Updating product..." + product);

        List<FieldError> errors = updateProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        // validate
        if (updateProductBindingResult.hasErrors()) {
            return "admin/product/edit";
        }

        Product currentProduct = this.productService.getProductById(product.getId()).get();
        if (currentProduct != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }
        }

        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setQuantity(product.getQuantity());
        currentProduct.setDetailDesc(product.getDetailDesc());
        currentProduct.setShortDesc(product.getShortDesc());
        currentProduct.setFactory(product.getFactory());
        currentProduct.setTarget(product.getTarget());

        this.productService.handleSaveProduct(currentProduct);
        return "redirect:/admin/product";
    }

    // delete product
    @GetMapping(value = "/admin/product/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {
        System.out.println("Deleting product...");
        this.productService.deleteProductById(id);
        return "redirect:/admin/product";
    }
}
