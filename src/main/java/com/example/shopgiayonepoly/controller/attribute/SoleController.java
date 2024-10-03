package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Sole;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.attribute.SoleService;
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
public class SoleController {

    @Autowired
    SoleService soleService;

    String mess = "";
    String soleMess = "";

    @GetMapping("/sole")
    public String list(Model model) {
        model.addAttribute("soleList", soleService.getSoleNotStatus0());
        model.addAttribute("soleAdd", new Sole());
        return "Attribute/sole";
    }

    @GetMapping("/sole/delete")
    public ResponseEntity<List<Sole>> listSoleDelete() {
        List<Sole> deletedSoles = soleService.getSoleDelete();
        return new ResponseEntity<>(deletedSoles, HttpStatus.OK);
    }

    @GetMapping("/sole/active")
    public ResponseEntity<List<Sole>> listActive() {
        List<Sole> listSoleActive = soleService.getSoleNotStatus0();
        return new ResponseEntity<>(listSoleActive, HttpStatus.OK);
    }

    @RequestMapping("/sole/add")
    public String add(@ModelAttribute("soleAdd") Sole sole) {
        sole.setStatus(1);
        soleService.save(sole);
        this.mess = "Thêm hóa đơn mới thành công!";
        this.soleMess = "1";
        return "redirect:/attribute/sole";
    }

    @PostMapping("/sole/update-status")
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
            soleService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-sole")
    @ResponseBody
    public ResponseEntity<String> updateSole(@RequestBody Map<String, Object> payload) {
        try {
            int id;
            String codeSole;
            String nameSole;

            // Lấy giá trị từ payload
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            codeSole = (String) payload.get("codeSole");
            nameSole = (String) payload.get("nameSole");

            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
            soleService.updateSole(id, codeSole, nameSole);

            return ResponseEntity.ok("Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật dữ liệu thất bại");
        }
    }

    @PostMapping("/delete-sole")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSole(@RequestBody Map<String, Object> payload) {
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
            soleService.updateStatus(id, status);

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
