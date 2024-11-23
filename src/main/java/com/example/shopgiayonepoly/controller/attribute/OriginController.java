package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.attribute.OriginService;
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
public class OriginController {

    @Autowired
    OriginService originService;

    @RequestMapping("/origin")
    public String list(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        model.addAttribute("originList", originService.getOriginNotStatus0());
        model.addAttribute("originAdd", new Origin());
        return "Attribute/origin";
    }

    @GetMapping("/origin/delete")
    public ResponseEntity<List<Origin>> listOriginDelete(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Origin> deletedOrigins = originService.getOriginDelete();
        return new ResponseEntity<>(deletedOrigins, HttpStatus.OK);
    }

    @GetMapping("/origin/active")
    public ResponseEntity<List<Origin>> listActive(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Origin> listOriginActive = originService.getOriginNotStatus0();
        return new ResponseEntity<>(listOriginActive, HttpStatus.OK);
    }

    @GetMapping("/origin/get-code")
    public ResponseEntity<List<String>> findAllCodeOrigin(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> codeOrigin = new ArrayList<>();
        for (Origin listOrigin : originService.findAll()) {
            codeOrigin.add(listOrigin.getCodeOrigin());
        }
        return new ResponseEntity<>(codeOrigin, HttpStatus.OK);
    }

    @GetMapping("/origin/get-name")
    public ResponseEntity<List<String>> findAllNameOrigin(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> nameOrigin = new ArrayList<>();
        for (Origin listOrigin : originService.findAll()) {
            nameOrigin.add(listOrigin.getNameOrigin());
        }
        return new ResponseEntity<>(nameOrigin, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/origin/add")
    public ResponseEntity<Map<String, String>> add(@ModelAttribute Origin origin, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        for (Origin listOrigin : originService.findAll()) {
            if (origin.getCodeOrigin().trim().toLowerCase().equals(listOrigin.getCodeOrigin().trim().toLowerCase())) {
                checkCode = false;
            }
        }
        for (Origin listOrigin : originService.findAll()) {
            if (origin.getNameOrigin().trim().toLowerCase().equals(listOrigin.getNameOrigin().trim().toLowerCase())) {
                checkName = false;
            }
        }
        if (!checkCode || !checkName || origin.getCodeOrigin().isEmpty() || origin.getNameOrigin().isEmpty() || origin.getCodeOrigin().length() > 10 || origin.getNameOrigin().length() > 50) {
            thongBao.put("message", "Dữ liệu không hợp lệ");
            thongBao.put("check", "2");
        } else {
            origin.setStatus(1);
            originService.save(origin);
            thongBao.put("message", "Thêm xuất xứ thành công");
            thongBao.put("check", "1");
        }
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/origin/update-status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> payload, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
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
            originService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-origin")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateOrigin(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        try {
            int id;
            String codeOrigin;
            String nameOrigin;

            // Lấy giá trị từ payload
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            codeOrigin = (String) payload.get("codeOrigin");
            nameOrigin = (String) payload.get("nameOrigin");
            boolean checkCode = true;
            for (Origin listOrigin : originService.findAll()) {
                if (codeOrigin.trim().toLowerCase().equals(listOrigin.getCodeOrigin().trim().toLowerCase()) && id != listOrigin.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Origin listOrigin : originService.findAll()) {
                if (nameOrigin.trim().toLowerCase().equals(listOrigin.getNameOrigin().trim().toLowerCase()) && id != listOrigin.getId()) {
                    checkName = false;
                    break;
                }
            }
            // Gọi service để cập nhật dữ liệu xuất xứ trong cơ sở dữ liệu
            if (codeOrigin.isEmpty() || codeOrigin.length() > 10) {
                thongBao.put("message", "Mã xuất xứ không được trống và lớn hơn 10 kí tự");
                thongBao.put("check", "2");
            } else if (!checkCode) {
                thongBao.put("message", "Mã xuất xứ đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên xuất xứ đã tồn tại");
                thongBao.put("check", "2");
            } else if (nameOrigin.isEmpty() || nameOrigin.length() > 50) {
                thongBao.put("message", "Tên xuất xứ không được trống và lớn hơn 50 kí tự");
                thongBao.put("check", "2");
            } else {
                originService.updateOrigin(id, codeOrigin, nameOrigin);
                thongBao.put("message", "Sửa xuất xứ thành công");
                thongBao.put("check", "1");
            }
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            thongBao.put("message", "Sửa xuất xứ thất bại");
            thongBao.put("check", "2");
            return ResponseEntity.ok(thongBao);
        }
    }

    @PostMapping("/delete-origin")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteOrigin(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
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
                thongBao.put("message", "Xóa xuất xứ thành công");
                thongBao.put("check", "1");
            } else {
                thongBao.put("message", "Khôi phục xuất xứ thành công");
                thongBao.put("check", "1");
            }
            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            originService.updateStatus(id, status);
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
