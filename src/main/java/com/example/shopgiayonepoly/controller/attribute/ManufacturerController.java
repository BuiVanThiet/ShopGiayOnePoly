package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.attribute.ManufacturerService;
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
public class ManufacturerController {

    @Autowired
    ManufacturerService manufacturerService;

    @RequestMapping("/manufacturer")
    public String list(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        model.addAttribute("manufacturerList", manufacturerService.getManufacturerNotStatus0());
        model.addAttribute("manufacturerAdd", new Manufacturer());
        return "Attribute/manufacturer";
    }

    @GetMapping("/manufacturer/delete")
    public ResponseEntity<List<Manufacturer>> listManufacturerDelete(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Manufacturer> deletedManufacturers = manufacturerService.getManufacturerDelete();
        return new ResponseEntity<>(deletedManufacturers, HttpStatus.OK);
    }

    @GetMapping("/manufacturer/active")
    public ResponseEntity<List<Manufacturer>> listActive(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Manufacturer> listManufacturerActive = manufacturerService.getManufacturerNotStatus0();
        return new ResponseEntity<>(listManufacturerActive, HttpStatus.OK);
    }

    @GetMapping("/manufacturer/get-code")
    public ResponseEntity<List<String>> findAllCodeManufacturer(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> codeManufacturer = new ArrayList<>();
        for (Manufacturer listManufacturer : manufacturerService.findAll()) {
            codeManufacturer.add(listManufacturer.getCodeManufacturer());
        }
        return new ResponseEntity<>(codeManufacturer, HttpStatus.OK);
    }

    @GetMapping("/manufacturer/get-name")
    public ResponseEntity<List<String>> findAllNameManufacturer(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> nameManufacturer = new ArrayList<>();
        for (Manufacturer listManufacturer : manufacturerService.findAll()) {
            nameManufacturer.add(listManufacturer.getNameManufacturer());
        }
        return new ResponseEntity<>(nameManufacturer, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/manufacturer/add")
    public ResponseEntity<Map<String, String>> add(@ModelAttribute Manufacturer manufacturer, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        for (Manufacturer listManufacturer : manufacturerService.findAll()) {
            if (manufacturer.getCodeManufacturer().trim().toLowerCase().equals(listManufacturer.getCodeManufacturer().trim().toLowerCase())) {
                checkCode = false;
            }
        }
        for (Manufacturer listManufacturer : manufacturerService.findAll()) {
            if (manufacturer.getNameManufacturer().trim().toLowerCase().equals(listManufacturer.getNameManufacturer().trim().toLowerCase())) {
                checkName = false;
            }
        }
        if (!checkCode || !checkName || manufacturer.getCodeManufacturer().isEmpty() || manufacturer.getNameManufacturer().isEmpty() || manufacturer.getCodeManufacturer().length() > 10 || manufacturer.getNameManufacturer().length() > 50) {
            thongBao.put("message", "Dữ liệu không hợp lệ");
            thongBao.put("check", "2");
        } else {
            manufacturer.setStatus(1);
            manufacturerService.save(manufacturer);
            thongBao.put("message", "Thêm nhà sản xuất thành công");
            thongBao.put("check", "1");
        }
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/manufacturer/update-status")
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
            manufacturerService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-manufacturer")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateManufacturer(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        try {
            int id;
            String codeManufacturer;
            String nameManufacturer;

            // Lấy giá trị từ payload
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            codeManufacturer = (String) payload.get("codeManufacturer");
            nameManufacturer = (String) payload.get("nameManufacturer");
            boolean checkCode = true;
            for (Manufacturer listManufacturer : manufacturerService.findAll()) {
                if (codeManufacturer.trim().toLowerCase().equals(listManufacturer.getCodeManufacturer().trim().toLowerCase()) && id != listManufacturer.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Manufacturer listManufacturer : manufacturerService.findAll()) {
                if (nameManufacturer.trim().toLowerCase().equals(listManufacturer.getNameManufacturer().trim().toLowerCase()) && id != listManufacturer.getId()) {
                    checkName = false;
                    break;
                }
            }
            // Gọi service để cập nhật dữ liệu nhà sản xuất trong cơ sở dữ liệu
            if (codeManufacturer.isEmpty() || codeManufacturer.length() > 10) {
                thongBao.put("message", "Mã nhà sản xuất không được trống và lớn hơn 10 kí tự");
                thongBao.put("check", "2");
            } else if (!checkCode) {
                thongBao.put("message", "Mã nhà sản xuất đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên nhà sản xuất đã tồn tại");
                thongBao.put("check", "2");
            } else if (nameManufacturer.isEmpty() || nameManufacturer.length() > 50) {
                thongBao.put("message", "Tên nhà sản xuất không được trống và lớn hơn 50 kí tự");
                thongBao.put("check", "2");
            } else {
                manufacturerService.updateManufacturer(id, codeManufacturer, nameManufacturer);
                thongBao.put("message", "Sửa nhà sản xuất thành công");
                thongBao.put("check", "1");
            }
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            thongBao.put("message", "Sửa nhà sản xuất thất bại");
            thongBao.put("check", "2");
            return ResponseEntity.ok(thongBao);
        }
    }

    @PostMapping("/delete-manufacturer")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteManufacturer(@RequestBody Map<String, Object> payload, HttpSession session) {
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
                thongBao.put("message", "Xóa nhà sản xuất thành công");
                thongBao.put("check", "1");
            } else {
                thongBao.put("message", "Khôi phục nhà sản xuất thành công");
                thongBao.put("check", "1");
            }
            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            manufacturerService.updateStatus(id, status);
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
