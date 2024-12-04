package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Category;
import com.example.shopgiayonepoly.entites.Category;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CashierInventoryService;
import com.example.shopgiayonepoly.service.TimekeepingService;
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
    @Autowired
    TimekeepingService timekeepingService;
    @Autowired
    CashierInventoryService cashierInventoryService;

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
        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        for (Category listCategory : categoryService.findAll()) {
            if (category.getCodeCategory().trim().equalsIgnoreCase(listCategory.getCodeCategory().trim())) {
                checkCode = false;
                break;
            }
        }
        for (Category listCategory : categoryService.findAll()) {
            if (category.getNameCategory().trim().equalsIgnoreCase(listCategory.getNameCategory().trim())) {
                checkName = false;
                break;
            }
        }
        if (!checkCode) {
            thongBao.put("message", "Mã danh mục đã tồn tại");
            thongBao.put("check", "2");
        } else if (!checkName) {
            thongBao.put("message", "Tên danh mục đã tồn tại");
            thongBao.put("check", "2");
        } else if (category.getCodeCategory().isEmpty()) {
            thongBao.put("message", "Mã danh mục không được để trống");
            thongBao.put("check", "2");
        } else if (category.getNameCategory().isEmpty()) {
            thongBao.put("message", "Tên danh mục không được để trống");
            thongBao.put("check", "2");
        } else if (category.getCodeCategory().length() > 10) {
            thongBao.put("message", "Mã danh mục không được dài quá 10 ký tự");
            thongBao.put("check", "2");
        } else if (category.getNameCategory().length() > 50) {
            thongBao.put("message", "Tên danh mục không được dài quá 50 ký tự");
            thongBao.put("check", "2");
        } else {
            category.setStatus(1);
            categoryService.save(category);
            thongBao.put("message", "Thêm danh mục thành công");
            thongBao.put("check", "1");
        }

        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/update-category")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateCategory(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
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
                if (codeCategory.trim().equalsIgnoreCase(listCategory.getCodeCategory().trim()) && id != listCategory.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Category listCategory : categoryService.findAll()) {
                if (nameCategory.trim().equalsIgnoreCase(listCategory.getNameCategory().trim()) && id != listCategory.getId()) {
                    checkName = false;
                    break;
                }
            }
            // Gọi service để cập nhật dữ liệu danh mục sắc trong cơ sở dữ liệu
            if (!checkCode) {
                thongBao.put("message", "Mã danh mục đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên danh mục đã tồn tại");
                thongBao.put("check", "2");
            } else if (codeCategory.isEmpty()) {
                thongBao.put("message", "Mã danh mục không được để trống");
                thongBao.put("check", "2");
            } else if (nameCategory.isEmpty()) {
                thongBao.put("message", "Tên danh mục không được để trống");
                thongBao.put("check", "2");
            } else if (codeCategory.length() > 10) {
                thongBao.put("message", "Mã danh mục không được dài quá 10 ký tự");
                thongBao.put("check", "2");
            } else if (nameCategory.length() > 50) {
                thongBao.put("message", "Tên danh mục không được dài quá 50 ký tự");
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
        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
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

    protected Map<String,String> checkLoginAndLogOutByStaff(Integer idStaff) {
        Map<String,String> thongBao = new HashMap<>();
        String checkLogin = getCheckStaffAttendanceYetBill(idStaff,1);
        String checkLogOut = getCheckStaffAttendanceYetBill(idStaff,2);
        System.out.println(checkLogin);
        if(!checkLogin.equals("Có")) {
            thongBao.put("message","Mời bạn điểm danh trước khi làm việc!");
            return thongBao;
        }

        if(checkLogin.equals("Có") && checkLogOut.equals("Có")) {
            thongBao.put("message","Bạn đã điểm danh vào và ra rồi, không thể làm việc được nữa!");
            return thongBao;
        }
        thongBao.put("message","");
        return thongBao;
    }


    protected String getCheckStaffAttendanceYetBill(
//            @PathVariable("id") Integer idStaff,@PathVariable("type") Integer timekeepingTypeCheck
            Integer idStaff, Integer timekeepingTypeCheck
    ) {
        List<Object[]> checkLoginLogOut = this.timekeepingService.getCheckStaffAttendanceYet(idStaff, timekeepingTypeCheck);

        // Kiểm tra nếu danh sách không rỗng và có kết quả
        if (!checkLoginLogOut.isEmpty() && checkLoginLogOut.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            return checkLoginLogOut.get(0)[0].toString();
        }
        // Trường hợp không có dữ liệu
        return "Không";
    }

}
