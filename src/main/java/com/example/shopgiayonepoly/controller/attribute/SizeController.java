package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Size;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.attribute.SizeService;
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
public class SizeController {

    @Autowired
    SizeService sizeService;

    @RequestMapping("/size")
    public String list(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        model.addAttribute("sizeList", sizeService.getSizeNotStatus0());
        model.addAttribute("sizeAdd", new Size());
        return "Attribute/size";
    }

    @GetMapping("/size/delete")
    public ResponseEntity<List<Size>> listSizeDelete(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Size> deletedSizes = sizeService.getSizeDelete();
        return new ResponseEntity<>(deletedSizes, HttpStatus.OK);
    }

    @GetMapping("/size/active")
    public ResponseEntity<List<Size>> listActive(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Size> listSizeActive = sizeService.getSizeNotStatus0();
        return new ResponseEntity<>(listSizeActive, HttpStatus.OK);
    }

    @GetMapping("/size/get-code")
    public ResponseEntity<List<String>> findAllCodeSize(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> codeSize = new ArrayList<>();
        for (Size listSize : sizeService.findAll()) {
            codeSize.add(listSize.getCodeSize());
        }
        return new ResponseEntity<>(codeSize, HttpStatus.OK);
    }

    @GetMapping("/size/get-name")
    public ResponseEntity<List<String>> findAllNameSize(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> nameSize = new ArrayList<>();
        for (Size listSize : sizeService.findAll()) {
            nameSize.add(listSize.getNameSize());
        }
        return new ResponseEntity<>(nameSize, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/size/add")
    public ResponseEntity<Map<String, String>> add(@ModelAttribute Size size, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return null;
        }
        boolean checkCode = true;
        boolean checkName = true;
        for (Size listSize : sizeService.findAll()) {
            if (size.getCodeSize().trim().toLowerCase().equals(listSize.getCodeSize().trim().toLowerCase())) {
                checkCode = false;
            }
        }
        for (Size listSize : sizeService.findAll()) {
            if (size.getNameSize().trim().toLowerCase().equals(listSize.getNameSize().trim().toLowerCase())) {
                checkName = false;
            }
        }
        if (!checkCode || !checkName || size.getCodeSize().isEmpty() || size.getNameSize().isEmpty() || size.getCodeSize().length() > 10 || size.getNameSize().length() > 50) {
            thongBao.put("message", "Dữ liệu không hợp lệ");
            thongBao.put("check", "2");
        } else {
            size.setStatus(1);
            sizeService.save(size);
            thongBao.put("message", "Thêm kích cỡ thành công");
            thongBao.put("check", "1");
        }
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/size/update-status")
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
            sizeService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-size")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateSize(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return null;
        }
        try {
            int id;
            String codeSize;
            String nameSize;

            // Lấy giá trị từ payload
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            codeSize = (String) payload.get("codeSize");
            nameSize = (String) payload.get("nameSize");
            boolean checkCode = true;
            for (Size listSize : sizeService.findAll()) {
                if (codeSize.trim().toLowerCase().equals(listSize.getCodeSize().trim().toLowerCase()) && id != listSize.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Size listSize : sizeService.findAll()) {
                if (nameSize.trim().toLowerCase().equals(listSize.getNameSize().trim().toLowerCase()) && id != listSize.getId()) {
                    checkName = false;
                    break;
                }
            }
            // Gọi service để cập nhật dữ liệu kích cỡ trong cơ sở dữ liệu
            if (codeSize.isEmpty() || codeSize.length() > 10) {
                thongBao.put("message", "Mã kích cỡ không được trống và lớn hơn 10 kí tự");
                thongBao.put("check", "2");
            } else if (!checkCode) {
                thongBao.put("message", "Mã kích cỡ đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên kích cỡ đã tồn tại");
                thongBao.put("check", "2");
            } else if (nameSize.isEmpty() || nameSize.length() > 50) {
                thongBao.put("message", "Tên kích cỡ không được trống và lớn hơn 50 kí tự");
                thongBao.put("check", "2");
            } else {
                sizeService.updateSize(id, codeSize, nameSize);
                thongBao.put("message", "Sửa kích cỡ thành công");
                thongBao.put("check", "1");
            }
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            thongBao.put("message", "Sửa kích cỡ thất bại");
            thongBao.put("check", "2");
            return ResponseEntity.ok(thongBao);
        }
    }

    @PostMapping("/delete-size")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteSize(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return null;
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
                thongBao.put("message", "Xóa kích cỡ thành công");
                thongBao.put("check", "1");
            } else {
                thongBao.put("message", "Khôi phục kích cỡ thành công");
                thongBao.put("check", "1");
            }
            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            sizeService.updateStatus(id, status);
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
