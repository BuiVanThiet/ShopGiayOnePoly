package com.example.shopgiayonepoly.controller;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.baseMethod.BaseProduct;
import com.example.shopgiayonepoly.entites.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Controller
@RequestMapping("/staff/product")
@RequiredArgsConstructor
public class ProductController extends BaseProduct {
    private final Cloudinary cloudinary;


    @GetMapping("/productv2")
    public String productv2(Model model) {
        model.addAttribute("productList", productService.getProductNotStatus0());
        model.addAttribute("categoryList", categoryService.findAll());
        return "/Product/productv2";
    }

    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("productList", productService.findAllProductsWithOneImage());
        model.addAttribute("product", new Product());
        model.addAttribute("materialList", materialService.findAll());
        model.addAttribute("manufacturerList", manufacturerService.findAll());
        model.addAttribute("originList", originService.findAll());
        model.addAttribute("colorList", colorService.findAll());
        model.addAttribute("sizeList", sizeService.findAll());
        model.addAttribute("soleList", soleService.findAll());
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("nameProductList", productService.findAllNameProduct());
        return "/Product/create";
    }


    @RequestMapping("/add")
    public String add(@ModelAttribute("product") Product product,
                      @RequestParam List<Integer> categories,
                      @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {

        // Gán danh mục cho sản phẩm
        Set<Category> selectedCategories = categoryService.findCategoriesByIds(categories);
        product.setCategories(selectedCategories);
        product.setStatus(1);

        // Tạo danh sách ảnh mới
        List<Image> newImages = new ArrayList<>();

        // Duyệt qua từng file ảnh
        for (MultipartFile multipartFile : imageFiles) {
            if (!multipartFile.isEmpty()) {
                // Tạo tên ảnh duy nhất bằng UUID
                String nameImage = UUID.randomUUID().toString();

                cloudinary.uploader()
                        .upload(multipartFile.getBytes(), Map.of("public_id", nameImage))
                        .get("url")
                        .toString();

                // Tạo đối tượng Image và gán sản phẩm cho ảnh
                Image image = new Image();
                image.setNameImage(nameImage);
                image.setProduct(product); // Gán sản phẩm cho ảnh
                image.setStatus(1);
                // Thêm ảnh vào danh sách
                newImages.add(image);
            }
        }

        // Gán danh sách ảnh mới vào sản phẩm
        product.setImages(newImages);

        // Lưu sản phẩm vào cơ sở dữ liệu
        productService.save(product);

        return "redirect:/staff/product/productv2"; // Chuyển hướng sau khi hoàn tất
    }

    @GetMapping("/detail/{idProduct}")
    public String viewProductDetail(@PathVariable("idProduct") Integer idProduct, Model model) {
        List<ProductDetail> productDetails = productService.findAllProductDetailByIDProduct(idProduct);
        model.addAttribute("productDetailList", productDetails);
        return "/Product/productDetail";
    }
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
