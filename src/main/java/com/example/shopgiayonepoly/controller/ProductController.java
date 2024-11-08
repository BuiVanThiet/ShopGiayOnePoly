package com.example.shopgiayonepoly.controller;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.baseMethod.BaseProduct;
import com.example.shopgiayonepoly.dto.response.ProductDetailResponse;
import com.example.shopgiayonepoly.entites.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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


    @PostMapping("/add-product-with-details")
    public ResponseEntity<String> addProductWithDetails(
            @ModelAttribute Product product,
            @RequestParam(value = "productDetails", required = false) String productDetailsJson,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {

        // Khởi tạo danh sách chi tiết sản phẩm
        List<ProductDetail> productDetails = new ArrayList<>();

        // Chuyển đổi productDetailsJson từ JSON thành List<ProductDetail> nếu có dữ liệu
        if (productDetailsJson != null && !productDetailsJson.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            productDetails = Arrays.asList(objectMapper.readValue(productDetailsJson, ProductDetail[].class));
        }

        // Gán danh mục cho sản phẩm
        Set<Category> selectedCategories = new HashSet<>(categoryService.findCategoriesByIds(
                product.getCategories().stream().map(Category::getId).collect(Collectors.toList())));
        product.setCategories(selectedCategories);
        product.setStatus(1);

        // Xử lý ảnh
        List<Image> newImages = new ArrayList<>();
        for (MultipartFile multipartFile : imageFiles) {
            if (!multipartFile.isEmpty()) {
                String nameImage = UUID.randomUUID().toString();
                cloudinary.uploader()
                        .upload(multipartFile.getBytes(), Map.of("public_id", nameImage))
                        .get("url")
                        .toString();

                Image image = new Image();
                image.setNameImage(nameImage);
                image.setProduct(product);
                image.setStatus(1);
                newImages.add(image);
            }
        }
        product.setImages(newImages);

        // Lưu sản phẩm
        Product savedProduct = productService.save(product);
        if (savedProduct == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm sản phẩm");
        }

        // Nếu có productDetails, cập nhật ID sản phẩm cho các chi tiết sản phẩm và lưu
        if (!productDetails.isEmpty()) {
            productDetails.forEach(detail -> detail.setProduct(savedProduct));
            productDetailRepository.saveAll(productDetails);
        }

        return ResponseEntity.ok("Sản phẩm và chi tiết sản phẩm đã được thêm thành công");
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
