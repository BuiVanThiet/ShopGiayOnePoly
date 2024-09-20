package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.service.attribute.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/attribute")
public class ColorController {

    @Autowired
    ColorService colorService;

    String mess = "";
    String colorMess = "";

    @GetMapping("/color")
    public String list(Model model) {
        model.addAttribute("colorList", colorService.getClientNotStatus0());
        model.addAttribute("colorAdd", new Color());
        return "Attribute/color";
    }

    @GetMapping("/popup")
    public String popup(Model model) {
        return "Attribute/popup";
    }

    @RequestMapping("/color/add")
    public String add(@ModelAttribute("colorAdd") Color color) {
        color.setStatus(1);
        colorService.save(color);
        this.mess = "Thêm hóa đơn mới thành công!";
        this.colorMess = "1";
        return "redirect:/attribute/color";
    }

    @PostMapping("/update-status")
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
            colorService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-color")
    @ResponseBody
    public ResponseEntity<String> updateColor(@RequestBody Map<String, Object> payload) {
        try {
            int id;
            String codeColor;
            String nameColor;

            // Lấy giá trị từ payload
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            codeColor = (String) payload.get("codeColor");
            nameColor = (String) payload.get("nameColor");

            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
            colorService.updateColor(id, codeColor, nameColor);

            return ResponseEntity.ok("Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật dữ liệu thất bại");
        }
    }

    @PostMapping("/delete-color")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteColor(@RequestBody Map<String, Object> payload) {
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
            colorService.updateStatus(id, status);

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




}
