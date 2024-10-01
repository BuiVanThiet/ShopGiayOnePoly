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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attribute")
public class ManufacturerController {

    @Autowired
    ManufacturerService manufacturerService;

    String mess = "";
    String manufacturerMess = "";

    @GetMapping("/manufacturer")
    public String list(Model model) {
        model.addAttribute("manufacturerList", manufacturerService.getClientNotStatus0());
        model.addAttribute("manufacturerAdd", new Manufacturer());
        return "Attribute/manufacturer";
    }

    @GetMapping("/manufacturer/delete")
    public ResponseEntity<List<Manufacturer>> listManufacturerDelete() {
        List<Manufacturer> deletedManufacturers = manufacturerService.getManufacturerDelete();
        return new ResponseEntity<>(deletedManufacturers, HttpStatus.OK);
    }

    @GetMapping("/manufacturer/active")
    public ResponseEntity<List<Manufacturer>> listActive() {
        List<Manufacturer> listManufacturerActive = manufacturerService.getClientNotStatus0();
        return new ResponseEntity<>(listManufacturerActive, HttpStatus.OK);
    }

//    @GetMapping("/popup")
//    public String popup(Model model) {
//        return "Attribute/popup";
//    }

    @RequestMapping("/manufacturer/add")
    public String add(@ModelAttribute("manufacturerAdd") Manufacturer manufacturer) {
        manufacturer.setStatus(1);
        manufacturerService.save(manufacturer);
        this.mess = "Thêm hóa đơn mới thành công!";
        this.manufacturerMess = "1";
        return "redirect:/attribute/manufacturer";
    }

    @PostMapping("/manufacturer/update-status")
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
            manufacturerService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-manufacturer")
    @ResponseBody
    public ResponseEntity<String> updateManufacturer(@RequestBody Map<String, Object> payload) {
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

            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
            manufacturerService.updateManufacturer(id, codeManufacturer, nameManufacturer);

            return ResponseEntity.ok("Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật dữ liệu thất bại");
        }
    }

    @PostMapping("/delete-manufacturer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteManufacturer(@RequestBody Map<String, Object> payload) {
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
            manufacturerService.updateStatus(id, status);

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
