package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.ProductService;
import com.example.shopgiayonepoly.service.attribute.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/staff")
public class ProductController {

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

    @GetMapping("/product")
    public String list(Model model) {
        model.addAttribute("productList", productService.getProductNotStatus0());
        model.addAttribute("productAdd", new Product());
        model.addAttribute("materialList", materialService.findAll());
        model.addAttribute("manufacturerList", manufacturerService.findAll());
        model.addAttribute("originList", originService.findAll());
        model.addAttribute("soleList", soleService.findAll());
        return "/Product/product";
    }

//    @GetMapping("/product/delete")
//    public ResponseEntity<List<Product>> listProductDelete() {
//        List<Product> deletedProducts = productService.getProductDelete();
//        return new ResponseEntity<>(deletedProducts, HttpStatus.OK);
//    }
//
//    @GetMapping("/product/active")
//    public ResponseEntity<List<Product>> listActive() {
//        List<Product> listProductActive = productService.getProductNotStatus0();
//        return new ResponseEntity<>(listProductActive, HttpStatus.OK);
//    }
//
    @RequestMapping("/product/add")
    public String add(@ModelAttribute("productAdd") Product product) {
        product.setStatus(1);
        productService.save(product);
        return "redirect:/staff/product";
    }

    @GetMapping("/product/get-one/{id}")
    public ResponseEntity<Product> getOneByID(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy sản phẩm
        }
    }

    @GetMapping("/product/check-code/{codeProduct}")
    public ResponseEntity<Product> checkProductCode(@PathVariable String codeProduct) {
        Optional<Product> product = productService.getOneProductByCodeProduct(codeProduct);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy sản phẩm
        }
    }
//
//    @PostMapping("/product/update-status")
//    @ResponseBody
//    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> payload) {
//        try {
//            int id;
//            int status;
//
//            // Lấy id và status từ payload, kiểm tra kiểu dữ liệu
//            if (payload.get("id") instanceof Integer) {
//                id = (Integer) payload.get("id");
//            } else {
//                id = Integer.parseInt((String) payload.get("id"));
//            }
//
//            if (payload.get("status") instanceof Integer) {
//                status = (Integer) payload.get("status");
//            } else {
//                status = Integer.parseInt((String) payload.get("status"));
//            }
//
//            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
//            productService.updateStatus(id, status);
//
//            return ResponseEntity.ok("Cập nhật trạng thái thành công");
//        } catch (NumberFormatException e) {
//            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
//        }
//    }
//
//    @PostMapping("/update-product")
//    @ResponseBody
//    public ResponseEntity<String> updateProduct(@RequestBody Map<String, Object> payload) {
//        try {
//            int id;
//            String codeProduct;
//            String nameProduct;
//
//            // Lấy giá trị từ payload
//            if (payload.get("id") instanceof Integer) {
//                id = (Integer) payload.get("id");
//            } else {
//                id = Integer.parseInt((String) payload.get("id"));
//            }
//
//            codeProduct = (String) payload.get("codeProduct");
//            nameProduct = (String) payload.get("nameProduct");
//
//            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
//            productService.updateProduct(id, codeProduct, nameProduct);
//
//            return ResponseEntity.ok("Cập nhật dữ liệu thành công");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Cập nhật dữ liệu thất bại");
//        }
//    }
//
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
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }



}
