package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Category;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.attribute.CategoryService;
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

@Controller
@RequestMapping("/attribute")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    String mess = "";
    String categoryMess = "";

    @GetMapping("/category")
    public String list(Model model) {
        model.addAttribute("categoryList", categoryService.getCategoryNotStatus0());
        model.addAttribute("categoryAdd", new Category());
        return "Attribute/category";
    }

    @GetMapping("/category/delete")
    public ResponseEntity<List<Category>> listCategoryDelete() {
        List<Category> deletedCategorys = categoryService.getCategoryDelete();
        return new ResponseEntity<>(deletedCategorys, HttpStatus.OK);
    }

    @GetMapping("/category/active")
    public ResponseEntity<List<Category>> listActive() {
        List<Category> listCategoryActive = categoryService.getCategoryNotStatus0();
        return new ResponseEntity<>(listCategoryActive, HttpStatus.OK);
    }

    @RequestMapping("/category/add")
    public String add(@ModelAttribute("categoryAdd") Category category) {
        category.setStatus(1);
        categoryService.save(category);
        this.mess = "Thêm hóa đơn mới thành công!";
        this.categoryMess = "1";
        return "redirect:/attribute/category";
    }

    @PostMapping("/category/update-status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> payload) {
        try {
            int id;
            int status;

            // Lấy id và status từ payload, kiểm tra kiểu dữ liệu
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            if (payload.get("status") instanceof Integer) {
                status = (Integer) payload.get("status");
            } else {
                status = Integer.parseInt((String) payload.get("status"));
            }

            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            categoryService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-category")
    @ResponseBody
    public ResponseEntity<String> updateCategory(@RequestBody Map<String, Object> payload) {
        try {
            int id;
            String codeCategory;
            String nameCategory;

            // Lấy giá trị từ payload
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            codeCategory = (String) payload.get("codeCategory");
            nameCategory = (String) payload.get("nameCategory");

            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
            categoryService.updateCategory(id, codeCategory, nameCategory);

            return ResponseEntity.ok("Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật dữ liệu thất bại");
        }
    }

    @PostMapping("/delete-category")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCategory(@RequestBody Map<String, Object> payload) {
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

            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            categoryService.updateStatus(id, status);

            // Phản hồi thành công
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
