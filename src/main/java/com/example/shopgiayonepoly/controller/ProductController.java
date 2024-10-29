package com.example.shopgiayonepoly.controller;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.CategoryService;
import com.example.shopgiayonepoly.service.ProductService;
import com.example.shopgiayonepoly.service.attribute.ManufacturerService;
import com.example.shopgiayonepoly.service.attribute.MaterialService;
import com.example.shopgiayonepoly.service.attribute.OriginService;
import com.example.shopgiayonepoly.service.attribute.SoleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/staff")
@RequiredArgsConstructor
public class ProductController {
    private final Cloudinary cloudinary;

    @Autowired
    ProductService productService;

    @Autowired
    MaterialService materialService;
    @Autowired
    OriginService originService;
    @Autowired
    ManufacturerService manufacturerService;
    @Autowired
    SoleService soleService;

    @Autowired
    CategoryService categoryService;

//    @GetMapping("/product")
//    public String list(Model model) {
//        model.addAttribute("productList", productService.getProductNotStatus0());
//        model.addAttribute("productAdd", new Product());
//        model.addAttribute("materialList", materialService.findAll());
//        model.addAttribute("manufacturerList", manufacturerService.findAll());
//        model.addAttribute("originList", originService.findAll());
//        model.addAttribute("soleList", soleService.findAll());
//        model.addAttribute("categoryList", categoryService.findAll());
//        return "/Product/product";
//    }

    @GetMapping("/product/productv2")
    public String productv2(Model model) {
        model.addAttribute("productList", productService.getProductNotStatus0());
        model.addAttribute("categoryList", categoryService.findAll());
        return "/Product/productv2";
    }

    @GetMapping("/product/create")
    public String createProduct(Model model) {
        model.addAttribute("productList", productService.findAllProductsWithOneImage());
        model.addAttribute("productAdd", new Product());
        model.addAttribute("materialList", materialService.findAll());
        model.addAttribute("manufacturerList", manufacturerService.findAll());
        model.addAttribute("originList", originService.findAll());
        model.addAttribute("soleList", soleService.findAll());
        model.addAttribute("categoryList", categoryService.findAll());
        return "/Product/create";
    }

    @GetMapping("/api/productList")
    @ResponseBody
    public List<Product> getProductList() {
        return productService.findAll(); // Trả về danh sách sản phẩm
    }



    @RequestMapping("/product/add")
    public String add(@ModelAttribute("productAdd") Product product,
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

                // Thêm ảnh vào danh sách
                newImages.add(image);
            }
        }

        // Gán danh sách ảnh mới vào sản phẩm
        product.setImages(newImages);

        // Lưu sản phẩm vào cơ sở dữ liệu
        productService.save(product);

        return "redirect:/staff/product"; // Chuyển hướng sau khi hoàn tất
    }




    @GetMapping("/product/get-one/{id}")
    public ResponseEntity<Product> getOneByID(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("product/getImage/{productId}")
    public ResponseEntity<List<Image>> getImagesByProductId(@PathVariable Integer productId) {
        List<Image> images = productService.findAllImagesByProductId(productId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("product/getCategory/{productId}")
    public ResponseEntity<List<Integer>> getCategoriesByProductId(@PathVariable Integer productId) {
        List<CategoryProduct> categories = productService.findAllCategoryByProductId(productId);
        List<Integer> idCategories = categories.stream()
                .map(CategoryProduct::getIdCategory)
                .collect(Collectors.toList());
        return ResponseEntity.ok(idCategories);
    }


    @GetMapping("/product/check-code/{codeProduct}")
    public ResponseEntity<Product> checkProductCode(@PathVariable String codeProduct) {
        Optional<Product> product = productService.getOneProductByCodeProduct(codeProduct);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/product/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteProduct(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            int id;
            int status;
            // Lấy id và status từ payload, kiểm tra kiểu dữ liệu
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt(payload.get("id").toString());
            }

            if (payload.get("status") instanceof Integer) {
                status = (Integer) payload.get("status");
            } else {
                status = Integer.parseInt(payload.get("status").toString());
            }

            productService.updateStatus(id, status);

            response.put("success", true);
            response.put("message", "Xóa thành công");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            // Xử lý lỗi parse dữ liệu
            response.put("success", false);
            response.put("message", "Dữ liệu không hợp lệ: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Xử lý lỗi khác
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/product/search")
    @ResponseBody
    public List<Product> searchProducts(Integer idCategory, String searchTerm) {
            return productService.findProducts(idCategory, searchTerm);
    }


    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
