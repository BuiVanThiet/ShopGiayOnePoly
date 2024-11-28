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
import java.math.BigDecimal;
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
        model.addAttribute("categoryList", categoryService.getCategoryNotStatus0());
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
//        model.addAttribute("productList", productService.findAllProductsWithOneImage());
        model.addAttribute("product", new Product());
        model.addAttribute("materialList", materialService.getMaterialNotStatus0());
        model.addAttribute("manufacturerList", manufacturerService.getManufacturerNotStatus0());
        model.addAttribute("originList", originService.getOriginNotStatus0());
        model.addAttribute("colorList", colorService.getColorNotStatus0());
        model.addAttribute("sizeList", sizeService.getSizeNotStatus0());
        model.addAttribute("soleList", soleService.getSoleNotStatus0());
        model.addAttribute("categoryList", categoryService.getCategoryNotStatus0());
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
        model.addAttribute("colorList", colorService.getColorNotStatus0());
        model.addAttribute("sizeList", sizeService.getSizeNotStatus0());
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
                imageFiles.isEmpty() || imageFiles.size() > 10 || imageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            check = "3";
            message = "Lỗi khi thêm sản phẩm";
            return ResponseEntity.ok("Lỗi khi thêm sản phẩm");
        } else {
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
            Product savedProduct = productService.save(product);

            // Nếu có productDetails, cập nhật ID sản phẩm cho các chi tiết sản phẩm và lưu
            if (!productDetails.isEmpty()) {
                for (ProductDetail detail : productDetails) {
                    if (detail.getPrice().compareTo(BigDecimal.valueOf(1000)) < 0 ||
                            detail.getImport_price().compareTo(BigDecimal.valueOf(1000)) < 0 ||
                            detail.getQuantity() <= 0 || detail.getWeight() <= 0 || detail.getWeight() > 5000) {
                        check = "3";
                        message = "Lỗi khi thêm sản phẩm";
                        return ResponseEntity.ok("Lỗi khi thêm sản phẩm");
                    }
                }
                productDetails.forEach(detail -> detail.setProduct(savedProduct));
                productDetailRepository.saveAll(productDetails);
            }
            check = "1";
            message = "Thêm sản phẩm thành công";
        }

        return ResponseEntity.ok("Thêm sản phẩm thành công");

    }


    @PostMapping("/add-productDetail/{idProduct}")
    public ResponseEntity<String> addProductDetail(Model model, @PathVariable("idProduct") Integer id,
                                                   @RequestBody List<ProductDetail> productDetails) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null && productDetails.size() > 0) {
            for (ProductDetail productDetail : productDetails) {
                if (productDetail.getPrice().compareTo(BigDecimal.valueOf(1000)) < 0 ||
                        productDetail.getImport_price().compareTo(BigDecimal.valueOf(1000)) < 0 ||
                        productDetail.getQuantity() <= 0 || productDetail.getWeight() <= 0 || productDetail.getWeight() > 5000) {
                    check = "3";
                    message = "Lỗi khi thêm sản phẩm";
                    return ResponseEntity.ok("Lỗi khi thêm sản phẩm");
                }
            }
            productDetails.forEach(detail -> detail.setProduct(product));
            productDetailRepository.saveAll(productDetails);
            check = "1";
            message = "Thêm chi tiết sản phẩm thành công";
            return ResponseEntity.ok("Thêm chi tiết sản phẩm thành công");
        } else {
            check = "3";
            message = "Có lỗi khi thêm chi tiết sản phẩm";
            model.addAttribute("check", check);
            model.addAttribute("message", message);
            check = "";
            message = "";
            return ResponseEntity.ok("");
        }
    }

    @PutMapping  ("/update-multiple/product-detail")
    public ResponseEntity<?> updateMultipleProductDetails(@RequestBody List<ProductDetail> productDetails) {
        try {
            for (ProductDetail detail : productDetails) {
                // Tìm ProductDetail từ DB bằng ID
                ProductDetail existingDetail = productDetailRepository.findById(detail.getId())
                        .orElseThrow(() -> new RuntimeException("ProductDetail không tồn tại với ID: " + detail.getId()));

                // Cập nhật các trường
                existingDetail.setPrice(detail.getPrice());
                existingDetail.setImport_price(detail.getImport_price());
                existingDetail.setQuantity(detail.getQuantity());
                existingDetail.setWeight(detail.getWeight());
                existingDetail.setDescribe(detail.getDescribe());
                // Lưu thay đổi vào DB
                productDetailRepository.save(existingDetail);
            }

            return ResponseEntity.ok("Cập nhật thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi trong quá trình cập nhật: " + e.getMessage());
        }
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
                !checkMaterial || !checkOrigin || !checkManufacturer || imageFiles.size() > 10 ||
                imageFiles.isEmpty() && existingProduct.getImages().isEmpty() || imageFiles.stream().allMatch(MultipartFile::isEmpty) && existingProduct.getImages().isEmpty()) {
            check = "3";
            message = "Lỗi khi sửa sản phẩm";
            return ResponseEntity.ok("Lỗi khi sửa sản phẩm");
        } else {
            if (!imageFiles.isEmpty() && !imageFiles.stream().allMatch(MultipartFile::isEmpty)){
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

                existingProduct.getImages().addAll(newImages);
            }


            // Cập nhật sản phẩm vào cơ sở dữ liệu
            productService.save(existingProduct);
            check = "1";
            message = "Sửa sản phẩm thành công";
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
            model.addAttribute("check", check);
            model.addAttribute("message", message);
            check = "";
            message = "";
            model.addAttribute("messagee", "Chưa thêm sản phẩm chi tiết nào.");
            model.addAttribute("idProduct", idProduct);
            model.addAttribute("addDetailUrl", "/staff/product/create/product-detail/" + idProduct); // URL dẫn đến trang thêm chi tiết
        } else {
            model.addAttribute("check", check);
            model.addAttribute("message", message);
            check = "";
            message = "";
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
        List<Integer> listIDCategory = new ArrayList<>();
        Set<String> listImage = new HashSet<>();
        for (Category item : product.get().getCategories()) {
            listIDCategory.add(item.getId());
        }
        for (Image item : product.get().getImages()) {
            listImage.add(item.getNameImage());
        }

        model.addAttribute("selectedCategoryIds", listIDCategory);
        model.addAttribute("listImage", listImage);


        model.addAttribute("materialList", materialService.getMaterialNotStatus0());
        model.addAttribute("manufacturerList", manufacturerService.getManufacturerNotStatus0());
        model.addAttribute("originList", originService.getOriginNotStatus0());
        model.addAttribute("colorList", colorService.getColorNotStatus0());
        model.addAttribute("sizeList", sizeService.getSizeNotStatus0());
        model.addAttribute("soleList", soleService.getSoleNotStatus0());
        model.addAttribute("categoryList", categoryService.getCategoryNotStatus0());
        model.addAttribute("nameProductList", productService.findAllNameProduct());
        model.addAttribute("message", message);
        model.addAttribute("check", check);
        check = "";
        message = "";
        // Trả về tên view tương ứng (template Thymeleaf)
        return "/Product/updateProduct";
    }



    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
