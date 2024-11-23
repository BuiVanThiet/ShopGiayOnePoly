package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.baseMethod.BaseProduct;
import com.example.shopgiayonepoly.dto.request.AttributeRequet;
import com.example.shopgiayonepoly.entites.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-api")
public class ProductRestController extends BaseProduct {
    @GetMapping("/search")
    @ResponseBody
    public List<Product> searchProducts(Integer idCategory, String searchTerm) {
        return productService.findProducts(idCategory, searchTerm);
    }

    @GetMapping("/findProductDelete")
    @ResponseBody
    public List<Product> findProductDelete(Integer idCategory, String searchTerm) {
        return productService.findProductDelete(idCategory, searchTerm);
    }

    @GetMapping("/detail/search")
    @ResponseBody
    public List<ProductDetail> searchProductDetail(String searchTerm, Integer idProduct) {
        return productService.searchProductDetailsByKeyword(searchTerm, idProduct);
    }

    @GetMapping("/productList")
    @ResponseBody
    public List<Product> getProductList() {
        return productService.findAll(); // Trả về danh sách sản phẩm
    }


    @GetMapping("/get-one/{id}")
    public ResponseEntity<Product> getOneByID(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getImage/{productId}")
    public ResponseEntity<List<Image>> getImagesByProductId(@PathVariable Integer productId) {
        List<Image> images = productService.findAllImagesByProductId(productId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/getCategory/{productId}")
    public ResponseEntity<List<Integer>> getCategoriesByProductId(@PathVariable Integer productId) {
        List<CategoryProduct> categories = productService.findAllCategoryByProductId(productId);
        List<Integer> idCategories = categories.stream()
                .map(CategoryProduct::getIdCategory)
                .collect(Collectors.toList());
        return ResponseEntity.ok(idCategories);
    }


    @GetMapping("/check-code/{codeProduct}")
    public ResponseEntity<Product> checkProductCode(@PathVariable String codeProduct) {
        Optional<Product> product = productService.getOneProductByCodeProduct(codeProduct);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete")
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


    @PostMapping("/attribute/quickly-add")
    public ResponseEntity<Map<String, String>> addAttribute(@RequestBody AttributeRequet request, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if (staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        switch (request.getType()) {
            case "category":
                Category category = new Category();
                category.setCodeCategory(request.getCode());
                category.setNameCategory(request.getName());
                for (Category listCategory : categoryService.findAll()) {
                    if (category.getCodeCategory().trim().toLowerCase().equals(listCategory.getCodeCategory().trim().toLowerCase())) {
                        checkCode = false;
                        break;
                    }
                }
                for (Category listCategory : categoryService.findAll()) {
                    if (category.getNameCategory().trim().toLowerCase().equals(listCategory.getNameCategory().trim().toLowerCase())) {
                        checkName = false;
                        break;
                    }
                }
                if (!checkCode || !checkName || category.getCodeCategory().isEmpty() || category.getNameCategory().isEmpty() || category.getCodeCategory().length() > 10 || category.getNameCategory().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    category.setStatus(1);
                    categoryService.save(category);
                    thongBao.put("message", "Thêm danh mục thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "color":
                Color color = new Color();
                color.setCodeColor(request.getCode());
                color.setNameColor(request.getName());

                for (Color listColor : colorService.findAll()) {
                    if (color.getCodeColor().trim().toLowerCase().equals(listColor.getCodeColor().trim().toLowerCase())) {
                        checkCode = false;
                    }
                }
                for (Color listColor : colorService.findAll()) {
                    if (color.getNameColor().trim().toLowerCase().equals(listColor.getNameColor().trim().toLowerCase())) {
                        checkName = false;
                    }
                }
                if (!checkCode || !checkName || color.getCodeColor().isEmpty() || color.getNameColor().isEmpty() || color.getCodeColor().length() > 10 || color.getNameColor().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    color.setStatus(1);
                    colorService.save(color);
                    thongBao.put("message", "Thêm màu sắc thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "size":
                Size size = new Size();
                size.setCodeSize(request.getCode());
                size.setNameSize(request.getName());
                size.setStatus(1);
                sizeService.save(size);
                return ResponseEntity.ok(thongBao);
            case "material":
                Material material = new Material();
                material.setCodeMaterial(request.getCode());
                material.setNameMaterial(request.getName());
                material.setStatus(1);
                materialService.save(material);
                return ResponseEntity.ok(thongBao);
            case "manufacturer":
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setCodeManufacturer(request.getCode());
                manufacturer.setNameManufacturer(request.getName());
                manufacturer.setStatus(1);
                manufacturerService.save(manufacturer);
                return ResponseEntity.ok(thongBao);
            case "origin":
                Origin origin = new Origin();
                origin.setCodeOrigin(request.getCode());
                origin.setNameOrigin(request.getName());
                origin.setStatus(1);
                originService.save(origin);
                return ResponseEntity.ok(thongBao);
            case "sole":
                Sole sole = new Sole();
                sole.setCodeSole(request.getCode());
                sole.setNameSole(request.getName());
                sole.setStatus(1);
                soleService.save(sole);
                return ResponseEntity.ok(thongBao);
            default:
                thongBao.put("message", "Thêm thuộc tính thành côngfsdfdsfds");
                thongBao.put("check", "1");
                return ResponseEntity.ok(thongBao);
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


    @GetMapping("/findAllCodeProduct")
    public List<String> findAllCodeProduct() {
        return productService.findAllCodeProduct();
    }

    @PostMapping("/restore")
    public void restoreProduct(Integer id, Integer status) {
        productService.updateStatus(id, status);
    }

    @PostMapping("/delete-multiple")
    public ResponseEntity<?> deleteMultipleProducts(@RequestBody List<Integer> ids, HttpSession session) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("Danh sách sản phẩm không hợp lệ.");
        }

        try {
            for (Integer item : ids) {
                productService.updateStatus(item, 0);  // Cập nhật trạng thái xóa
            }
            return ResponseEntity.ok("Xóa sản phẩm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    @PostMapping("/restore-multiple")
    public ResponseEntity<?> restoreMultipleProducts(@RequestBody List<Integer> ids, HttpSession session) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("Danh sách sản phẩm không hợp lệ.");
        }

        try {
            for (Integer item : ids) {
                productService.updateStatus(item, 1);  // Cập nhật trạng thái xóa
            }
            return ResponseEntity.ok("Khôi phục sản phẩm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    @GetMapping("/find-quantity")
    public Integer findAllQuantity(Integer id) {
        return productService.findQuantityByIDProduct(id);
    }

}