package com.example.shopgiayonepoly.controller;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.baseMethod.BaseProduct;
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

    String message = "";
    String check = "";

    @GetMapping("")
    public String product(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        model.addAttribute("productList", productService.getProductNotStatus0());
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("message", message);
        model.addAttribute("check", check);
        check = "";
        message = "";
        return "/Product/product";
    }

    @GetMapping("/create")
    public String createProduct(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
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
        model.addAttribute("message", message);
        model.addAttribute("check", check);
        check = "";
        message = "";
        return "/Product/create";
    }

    @GetMapping("/create/product-detail/{idProduct}")
    public String createProductDetail(Model model, @PathVariable("idProduct") Integer idProduct, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        model.addAttribute("product", productRepository.findById(idProduct));
        model.addAttribute("colorList", colorService.findAll());
        model.addAttribute("sizeList", sizeService.findAll());
        return "/Product/createProductDetail";
    }


    @PostMapping("/add-product-with-details")
    public ResponseEntity<String> addProductWithDetails(
            @ModelAttribute Product product,
            @RequestParam(value = "productDetails", required = false) String productDetailsJson,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles, HttpSession session) throws IOException {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            check = "3";
            message = "Nhân viên chưa đăng nhập";
            return ResponseEntity.ok("Nhân viên chưa đăng nhập");
        }
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
                        .get("url");
                Image image = new Image();
                image.setNameImage(nameImage);
                image.setProduct(product);
                image.setStatus(1);
                newImages.add(image);
            }
        }
        product.setImages(newImages);
        boolean checkCodeProduct = true;
        for (Product listProduct : productService.findAll()) {
            if (product.getCodeProduct().trim().equalsIgnoreCase(listProduct.getCodeProduct().trim().toLowerCase())) {
                checkCodeProduct = false;
                break;
            }
        }
        boolean checkManufacturer = product.getManufacturer() != null && manufacturerService.findById(product.getManufacturer().getId()).isPresent();
        boolean checkMaterial = product.getMaterial() != null && materialService.findById(product.getMaterial().getId()).isPresent();
        boolean checkSole = product.getSole() != null && soleService.findById(product.getSole().getId()).isPresent();
        boolean checkOrigin = product.getOrigin() != null && originService.findById(product.getOrigin().getId()).isPresent();

        if (product.getCodeProduct() == null || product.getCodeProduct().trim().isEmpty() || !checkSole ||
                product.getCategories().isEmpty() || product.getCodeProduct().length() > 10 ||
                !checkCodeProduct || product.getNameProduct() == null ||
                product.getNameProduct().trim().isEmpty() || product.getNameProduct().length() > 255 ||
                !checkMaterial || !checkOrigin || !checkManufacturer ||
                imageFiles.isEmpty() || imageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            check = "3";
            message = "Lỗi khi thêm sản phẩm";
            return ResponseEntity.ok("Lỗi khi thêm sản phẩm");
        } else {
            check = "1";
            message = "Thêm sản phẩm thành công";
        }

        // Lưu sản phẩm
        Product savedProduct = productService.save(product);

        // Nếu có productDetails, cập nhật ID sản phẩm cho các chi tiết sản phẩm và lưu
        if (!productDetails.isEmpty()) {
            productDetails.forEach(detail -> detail.setProduct(savedProduct));
            productDetailRepository.saveAll(productDetails);
        }

        return ResponseEntity.ok("Thêm sản phẩm thành công");

    }

    @PostMapping("/update-product/{id}")
    public ResponseEntity<String> updateProductWithDetails(
            @PathVariable("id") Integer id,
            @ModelAttribute Product product,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles, HttpSession session) throws IOException {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            check = "3";
            message = "Nhân viên chưa đăng nhập";
            return ResponseEntity.ok("Nhân viên chưa đăng nhập");
        }
        // Kiểm tra sản phẩm có tồn tại hay không
        Optional<Product> existingProductOpt = productService.findById(id);
        if (!existingProductOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại");
        }

        Product existingProduct = existingProductOpt.get();

        // Cập nhật thông tin sản phẩm
        existingProduct.setCodeProduct(product.getCodeProduct());
        existingProduct.setNameProduct(product.getNameProduct());
        existingProduct.setDescribe(product.getDescribe());
        existingProduct.setMaterial(product.getMaterial());
        existingProduct.setCategories(product.getCategories());
        existingProduct.setManufacturer(product.getManufacturer());
        existingProduct.setSole(product.getSole());
        existingProduct.setOrigin(product.getOrigin());

        // Kiểm tra mã sản phẩm có trùng không (so với các sản phẩm khác)
        boolean checkCodeProduct = true;
        if (!product.getCodeProduct().equals(existingProduct.getCodeProduct())) {
            for (Product listProduct : productService.findAll()) {
                if (product.getCodeProduct().trim().equalsIgnoreCase(listProduct.getCodeProduct().trim().toLowerCase())) {
                    checkCodeProduct = false;
                    break;
                }
            }
        }
        boolean checkManufacturer = product.getManufacturer() != null && manufacturerService.findById(product.getManufacturer().getId()).isPresent();
        boolean checkMaterial = product.getMaterial() != null && materialService.findById(product.getMaterial().getId()).isPresent();
        boolean checkSole = product.getSole() != null && soleService.findById(product.getSole().getId()).isPresent();
        boolean checkOrigin = product.getOrigin() != null && originService.findById(product.getOrigin().getId()).isPresent();


        // Kiểm tra các trường cần thiết khác
        if (product.getCodeProduct() == null || product.getCodeProduct().trim().isEmpty() || !checkSole ||
                product.getCategories() == null || product.getCategories().isEmpty() || product.getCodeProduct().length() > 10 ||
                !checkCodeProduct || product.getNameProduct() == null ||
                product.getNameProduct().trim().isEmpty() || product.getNameProduct().length() > 255 ||
                !checkMaterial || !checkOrigin || !checkManufacturer ||
                imageFiles.isEmpty() || imageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi cập nhật sản phẩm: Các trường thông tin không hợp lệ");
        }

        existingProduct.getImages().clear();

        // Thêm các ảnh mới vào sản phẩm
        List<Image> newImages = new ArrayList<>();
        for (MultipartFile multipartFile : imageFiles) {
            if (!multipartFile.isEmpty()) {
                String nameImage = UUID.randomUUID().toString();
                cloudinary.uploader()
                        .upload(multipartFile.getBytes(), Map.of("public_id", nameImage))
                        .get("url");

                Image image = new Image();
                image.setNameImage(nameImage);
                image.setProduct(existingProduct);  // Liên kết ảnh với sản phẩm
                image.setStatus(1);  // Đảm bảo trạng thái ảnh là 1 (hoặc trạng thái khác mà bạn mong muốn)
                newImages.add(image);
            }
        }

        // Thêm ảnh mới vào collection ảnh của sản phẩm
        existingProduct.getImages().addAll(newImages);

        // Cập nhật sản phẩm vào cơ sở dữ liệu
        Product updatedProduct = productService.save(existingProduct);
        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật sản phẩm");
        }

        return ResponseEntity.ok("Sản phẩm đã được cập nhật thành công");
    }


    @GetMapping("/detail/{idProduct}")
    public String viewProductDetail(@PathVariable("idProduct") Integer idProduct, Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        List<ProductDetail> productDetails = productService.findAllProductDetailByIDProduct(idProduct);

        if (productDetails == null || productDetails.isEmpty()) {
            model.addAttribute("message", "Chưa thêm sản phẩm chi tiết nào.");
        } else {
            model.addAttribute("productDetailList", productDetails);
        }

        return "/Product/productDetail";
    }

    @GetMapping("/view-update/{id}")
    public String getProductById(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        // Lấy dữ liệu sản phẩm theo ID
        Optional<Product> product = productService.findById(id);
        // Đưa dữ liệu sản phẩm vào model
        model.addAttribute("product", product);

        model.addAttribute("materialList", materialService.findAll());
        model.addAttribute("manufacturerList", manufacturerService.findAll());
        model.addAttribute("originList", originService.findAll());
        model.addAttribute("colorList", colorService.findAll());
        model.addAttribute("sizeList", sizeService.findAll());
        model.addAttribute("soleList", soleService.findAll());
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("nameProductList", productService.findAllNameProduct());

        // Trả về tên view tương ứng (template Thymeleaf)
        return "/Product/updateProduct";
    }



    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
