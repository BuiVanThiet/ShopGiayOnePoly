package com.example.shopgiayonepoly.controller;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.baseMethod.BaseProduct;
import com.example.shopgiayonepoly.dto.response.ProductDetailResponse;
import com.example.shopgiayonepoly.entites.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("")
    public String product(Model model) {
        model.addAttribute("productList", productService.getProductNotStatus0());
        model.addAttribute("categoryList", categoryService.findAll());
        return "/Product/product";
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

    @GetMapping("/create/product-detail/{idProduct}")
    public String createProductDetail(Model model, @PathVariable("idProduct") Integer idProduct) {
        model.addAttribute("product", productRepository.findById(idProduct));
        model.addAttribute("colorList", colorService.findAll());
        model.addAttribute("sizeList", sizeService.findAll());
        return "/Product/createProductDetail";
    }


    @RequestMapping("/add")
    public ResponseEntity<String> add(@ModelAttribute("product") Product product,
                                      @RequestParam List<Integer> categories,
                                      @RequestParam("imageFiles") List<MultipartFile> imageFiles,
                                      HttpSession session) throws IOException    {

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
        Product productSave = productService.save(product);
        session.setAttribute("idProductSave",productSave.getId());
        System.out.println("da them san pham ");
        return ResponseEntity.ok("Sản phẩm đã được thêm thành công");
    }

    @GetMapping("/detail/{idProduct}")
    public String viewProductDetail(@PathVariable("idProduct") Integer idProduct, Model model) {
        List<ProductDetailResponse> productDetails = productService.findAllProductDetailByIDProduct(idProduct);
        model.addAttribute("productDetailList", productDetails);
        return "/Product/productDetail";
    }

    @PostMapping("/add-productDetail")
    public String addMultipleProductDetails(@RequestBody List<ProductDetail> productDetails, Model model) {
        System.out.println("da tehm san pham chi tiet");
        productDetailRepository.saveAll(productDetails);
        model.addAttribute("productList", productService.getProductNotStatus0());
        model.addAttribute("categoryList", categoryService.findAll());
        return "product";
    }
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
