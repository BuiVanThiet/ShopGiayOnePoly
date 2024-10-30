package com.example.shopgiayonepoly.controller;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.dto.request.AttributeRequet;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.CategoryService;
import com.example.shopgiayonepoly.service.ProductService;
import com.example.shopgiayonepoly.service.attribute.*;
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

    @Autowired
    ColorService colorService;

    @Autowired
    SizeService sizeService;


    @GetMapping("/product/productv2")
    public String productv2(Model model) {
        model.addAttribute("productList", productService.getProductNotStatus0());
        model.addAttribute("categoryList", categoryService.findAll());
        return "/Product/productv2";
    }

    @GetMapping("/product/create")
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

    @GetMapping("/api/productList")
    @ResponseBody
    public List<Product> getProductList() {
        return productService.findAll(); // Trả về danh sách sản phẩm
    }



    @PostMapping("/product/add")
    public String add(@ModelAttribute("product") Product product,
                      @RequestParam List<Integer> categories,
                      @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {

        // Gán danh mục cho sản phẩm
        Set<Category> selectedCategories = categoryService.findCategoriesByIds(categories);
        product.setCategories(selectedCategories);

        product.setStatus(1); // Hoặc trạng thái khác nếu cần

        // Tạo danh sách ảnh mới
        List<Image> newImages = new ArrayList<>();

        // Duyệt qua từng file ảnh
        for (MultipartFile multipartFile : imageFiles) {
            if (!multipartFile.isEmpty()) {
                // Tạo tên ảnh duy nhất bằng UUID
                String nameImage = UUID.randomUUID().toString();

                // Tải ảnh lên Cloudinary hoặc bộ nhớ khác
                String imageUrl = cloudinary.uploader()
                        .upload(multipartFile.getBytes(), Map.of("public_id", nameImage))
                        .get("url")
                        .toString();

                // Tạo đối tượng Image và gán URL ảnh
                Image image = new Image();
                image.setNameImage(nameImage);
                image.setProduct(product); // Liên kết ảnh với sản phẩm

                newImages.add(image);
            }
        }

        // Gán danh sách ảnh cho sản phẩm
        product.setImages(newImages);

        // Lưu sản phẩm
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

    @PostMapping("/attribute/quickly-add")
    public ResponseEntity<?> addAttribute(@RequestBody AttributeRequet request) {
        switch (request.getType()) {
            case "category":
                Category category = new Category();
                category.setCodeCategory(request.getCode());
                category.setNameCategory(request.getName());
                category.setStatus(1);
                categoryService.save(category);
                return ResponseEntity.ok("Thêm danh mục thành công!");
            case "color":
                Color color = new Color();
                color.setCodeColor(request.getCode());
                color.setNameColor(request.getName());
                color.setStatus(1);
                colorService.save(color);
                return ResponseEntity.ok("Thêm màu sắc thành công!");
            case "size":
                Size size = new Size();
                size.setCodeSize(request.getCode());
                size.setNameSize(request.getName());
                size.setStatus(1);
                sizeService.save(size);
                return ResponseEntity.ok("Thêm kích thước thành công!");
            case "material":
                Material material = new Material();
                material.setCodeMaterial(request.getCode());
                material.setNameMaterial(request.getName());
                material.setStatus(1);
                materialService.save(material);
                return ResponseEntity.ok("Thêm chất liệu thành công!");
            case "manufacturer":
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setCodeManufacturer(request.getCode());
                manufacturer.setNameManufacturer(request.getName());
                manufacturer.setStatus(1);
                manufacturerService.save(manufacturer);
                return ResponseEntity.ok("Thêm nhà sản xuất thành công!");
            case "origin":
                Origin origin = new Origin();
                origin.setCodeOrigin(request.getCode());
                origin.setNameOrigin(request.getName());
                origin.setStatus(1);
                originService.save(origin);
                return ResponseEntity.ok("Thêm nguồn gốc thành công!");
            case "sole":
                Sole sole = new Sole();
                sole.setCodeSole(request.getCode());
                sole.setNameSole(request.getName());
                sole.setStatus(1);
                soleService.save(sole);
                return ResponseEntity.ok("Thêm đế thành công!");
            default:
                return ResponseEntity.badRequest().body("Thêm mới không thành công!");
        }
    }

    @GetMapping("/attribute/list")
    public ResponseEntity<?> getAttributeList(@RequestParam String type) {
        switch (type) {
            case "category":
                List<Category> categories = categoryService.findAll();
                return ResponseEntity.ok(categories);
            case "color":
                List<Color> colors = colorService.findAll();
                return ResponseEntity.ok(colors);
            case "size":
                List<Size> sizes = sizeService.findAll();
                return ResponseEntity.ok(sizes);
            case "material":
                List<Material> materials = materialService.findAll();
                return ResponseEntity.ok(materials);
            case "manufacturer":
                List<Manufacturer> manufacturers = manufacturerService.findAll();
                return ResponseEntity.ok(manufacturers);
            case "origin":
                List<Origin> origins = originService.findAll();
                return ResponseEntity.ok(origins);
            case "sole":
                List<Sole> soles = soleService.findAll();
                return ResponseEntity.ok(soles);
            default:
                return ResponseEntity.badRequest().body("Loại thuộc tính không hợp lệ!");
        }
    }



    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
