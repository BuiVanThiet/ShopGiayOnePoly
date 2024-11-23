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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attribute")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/category")
    public String list(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if(staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        model.addAttribute("categoryList", categoryService.getCategoryNotStatus0());
        model.addAttribute("categoryAdd", new Category());
        return "Attribute/category";
    }

    @GetMapping("/category/delete")
    public ResponseEntity<List<Category>> listCategoryDelete(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<Category> deletedCategorys = categoryService.getCategoryDelete();
        return new ResponseEntity<>(deletedCategorys, HttpStatus.OK);
    }

    @GetMapping("/category/active")
    public ResponseEntity<List<Category>> listActive(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<Category> listCategoryActive = categoryService.getCategoryNotStatus0();
        return new ResponseEntity<>(listCategoryActive, HttpStatus.OK);
    }

    @GetMapping("/category/get-code")
    public ResponseEntity<List<String>> findAllCodeCategory(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<String> codeCategory = new ArrayList<>();
        for (Category listCategory : categoryService.findAll()) {
            codeCategory.add(listCategory.getCodeCategory());
        }
        return new ResponseEntity<>(codeCategory, HttpStatus.OK);
    }

    @GetMapping("/category/get-name")
    public ResponseEntity<List<String>> findAllNameCategory(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<String> nameCategory = new ArrayList<>();
        for (Category listCategory : categoryService.findAll()) {
            nameCategory.add(listCategory.getNameCategory());
        }
        return new ResponseEntity<>(nameCategory, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/category/add")
    public ResponseEntity<Map<String, String>> add(@ModelAttribute Category category, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        for (Category listCategory : categoryService.findAll()) {
            if (category.getCodeCategory().trim().toLowerCase().equals(listCategory.getCodeCategory().trim().toLowerCase())) {
                checkCode = false;
            }
        }
        for (Category listCategory : categoryService.findAll()) {
            if (category.getNameCategory().trim().toLowerCase().equals(listCategory.getNameCategory().trim().toLowerCase())) {
                checkName = false;
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
    }

    @PostMapping("/category/update-status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> payload, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
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
    public ResponseEntity<Map<String, String>> updateCategory(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
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
            boolean checkCode = true;
            for (Category listCategory : categoryService.findAll()) {
                if (codeCategory.trim().equalsIgnoreCase(listCategory.getCodeCategory().trim().toLowerCase()) && id != listCategory.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Category listCategory : categoryService.findAll()) {
                if (nameCategory.trim().equalsIgnoreCase(listCategory.getNameCategory().trim().toLowerCase()) && id != listCategory.getId()) {
                    checkName = false;
                    break;
                }
            }
            // Gọi service để cập nhật dữ liệu danh mục trong cơ sở dữ liệu
            if (codeCategory.isEmpty() || codeCategory.length() > 10) {
                thongBao.put("message", "Mã danh mục không được trống và lớn hơn 10 kí tự");
                thongBao.put("check", "2");
            } else if (!checkCode) {
                thongBao.put("message", "Mã danh mục đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên danh mục đã tồn tại");
                thongBao.put("check", "2");
            } else if (nameCategory.isEmpty() || nameCategory.length() > 50) {
                thongBao.put("message", "Tên danh mục không được trống và lớn hơn 50 kí tự");
                thongBao.put("check", "2");
            } else {
                categoryService.updateCategory(id, codeCategory, nameCategory);
                thongBao.put("message", "Sửa danh mục thành công");
                thongBao.put("check", "1");
            }
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            thongBao.put("message", "Sửa danh mục thất bại");
            thongBao.put("check", "2");
            return ResponseEntity.ok(thongBao);
        }
    }

    @PostMapping("/delete-category")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteCategory(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
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
            if (status == 0) {
                thongBao.put("message", "Xóa danh mục thành công");
                thongBao.put("check", "1");
            } else {
                thongBao.put("message", "Khôi phục danh mục thành công");
                thongBao.put("check", "1");
            }
            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            categoryService.updateStatus(id, status);
            return ResponseEntity.ok(thongBao);

        } catch (NumberFormatException e) {
            // Xử lý lỗi parse dữ liệu
            thongBao.put("message", "Lỗi khi xử lý dữ liệu");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            // Xử lý lỗi khác
            thongBao.put("message", "Lỗi khi xử lý dữ liệu");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
    }
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }



}
