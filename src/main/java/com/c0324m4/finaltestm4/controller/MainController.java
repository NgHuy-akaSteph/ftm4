package com.c0324m4.finaltestm4.controller;

import com.c0324m4.finaltestm4.model.NewProduct;
import com.c0324m4.finaltestm4.model.Product;
import com.c0324m4.finaltestm4.service.CategoryService;
import com.c0324m4.finaltestm4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private static final int PAGE_SIZE = 5;

    @GetMapping("/")
    public String showAllProducts(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Product> products = productService.findAllProducts(page, PAGE_SIZE);
        model.addAttribute("productList", products.getContent());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        return "home";
    }

    @GetMapping("/products/new")
    public String showAddForm(Model model){
        model.addAttribute("product", new NewProduct());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "add_product";
    }

    @PostMapping("/products/new")
    public String addProduct(@Valid @ModelAttribute("product") NewProduct newProduct, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("product", newProduct);
            return "add_product";
        }
        Product product = new Product();
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setCategory(categoryService.findById(newProduct.getCategoryId()));
        product.setStatus("chờ duyệt");
        productService.saveProduct(product);
        return "redirect:/";
    }

    @GetMapping("/products/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model){
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "edit_product";

    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "price", required = false) Long price,
                                 @RequestParam(value = "category", required = false) Long categoryId,
                                 Model model) {
        List<Product> products = productService.searchProducts(name, price, categoryId);
        model.addAttribute("sName", name);
        model.addAttribute("sPrice", price);
        model.addAttribute("sCategory", categoryId);
        model.addAttribute("productList", products);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        return "home";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam("productIds") List<Long> productIds){
        for(Long id : productIds){
            productService.deleteProduct(id);
        }
        return "redirect:/";
    }

    @PostMapping("/delete_all")
    public String deleteAllProducts() {
        productService.deleteAllProducts();
        return "redirect:/";
    }

}
