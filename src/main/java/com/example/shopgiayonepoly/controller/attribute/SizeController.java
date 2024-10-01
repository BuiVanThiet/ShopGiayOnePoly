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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attribute")
public class SizeController {

    @Autowired
    SizeService sizeService;

    String mess = "";
    String sizeMess = "";

    @GetMapping("/size")
    public String list(Model model) {
        model.addAttribute("sizeList", sizeService.getClientNotStatus0());
        model.addAttribute("sizeAdd", new Size());
        return "Attribute/size";
    }

    @GetMapping("/size/delete")
    public ResponseEntity<List<Size>> listSizeDelete() {
        List<Size> deletedSizes = sizeService.getSizeDelete();
        return new ResponseEntity<>(deletedSizes, HttpStatus.OK);
    }

    @GetMapping("/size/active")
    public ResponseEntity<List<Size>> listActive() {
        List<Size> listSizeActive = sizeService.getClientNotStatus0();
        return new ResponseEntity<>(listSizeActive, HttpStatus.OK);
    }

//    @GetMapping("/popup")
//    public String popup(Model model) {
//        return "Attribute/popup";
//    }

    @RequestMapping("/size/add")
    public String add(@ModelAttribute("sizeAdd") Size size) {
        size.setStatus(1);
        sizeService.save(size);
        this.mess = "Thêm hóa đơn mới thành công!";
        this.sizeMess = "1";
        return "redirect:/attribute/size";
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
    public ResponseEntity<String> updateSize(@RequestBody Map<String, Object> payload) {
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

            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
            sizeService.updateSize(id, codeSize, nameSize);

            return ResponseEntity.ok("Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật dữ liệu thất bại");
        }
    }

    @PostMapping("/delete-size")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSize(@RequestBody Map<String, Object> payload) {
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
            sizeService.updateStatus(id, status);

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
