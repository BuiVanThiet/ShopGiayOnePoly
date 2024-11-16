package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.attribute.ColorService;
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
public class ColorController {

    @Autowired
    ColorService colorService;

    @RequestMapping("/color")
    public String list(Model model) {
        model.addAttribute("colorList", colorService.getColorNotStatus0());
        model.addAttribute("colorAdd", new Color());
        return "Attribute/color";
    }

    @GetMapping("/color/delete")
    public ResponseEntity<List<Color>> listColorDelete() {
        List<Color> deletedColors = colorService.getColorDelete();
        return new ResponseEntity<>(deletedColors, HttpStatus.OK);
    }

    @GetMapping("/color/active")
    public ResponseEntity<List<Color>> listActive() {
        List<Color> listColorActive = colorService.getColorNotStatus0();
        return new ResponseEntity<>(listColorActive, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/color/add")
    public ResponseEntity<Map<String, String>> add(@ModelAttribute Color color) {
        Map<String, String> thongBao = new HashMap<>();
        color.setStatus(1);
        colorService.save(color);
        thongBao.put("message", "Thêm màu sắc thành công");
        thongBao.put("check", "1");
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/color/update-status")
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
    public ResponseEntity<Map<String, String>> updateColor(@RequestBody Map<String, Object> payload) {
        Map<String, String> thongBao = new HashMap<>();
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
            thongBao.put("message", "Sửa màu sắc thành công");
            thongBao.put("check", "1");
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            thongBao.put("message", "Sửa màu sắc thất bại");
            thongBao.put("check", "2");
            return ResponseEntity.ok(thongBao);
        }
    }

    @PostMapping("/delete-color")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteColor(@RequestBody Map<String, Object> payload) {
        Map<String, String> thongBao = new HashMap<>();
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
                thongBao.put("message", "Xóa màu sắc thành công");
                thongBao.put("check", "1");
            } else {
                thongBao.put("message", "Khôi phục màu sắc thành công");
                thongBao.put("check", "1");
            }
            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            colorService.updateStatus(id, status);
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
