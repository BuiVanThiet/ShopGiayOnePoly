package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.attribute.MaterialService;
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
public class MaterialController {

    @Autowired
    MaterialService materialService;

    String mess = "";
    String materialMess = "";

    @GetMapping("/material")
    public String list(Model model) {
        model.addAttribute("materialList", materialService.getClientNotStatus0());
        model.addAttribute("materialAdd", new Material());
        return "Attribute/material";
    }

    @GetMapping("/material/delete")
    public ResponseEntity<List<Material>> listMaterialDelete() {
        List<Material> deletedMaterials = materialService.getMaterialDelete();
        return new ResponseEntity<>(deletedMaterials, HttpStatus.OK);
    }

    @GetMapping("/material/active")
    public ResponseEntity<List<Material>> listActive() {
        List<Material> listMaterialActive = materialService.getClientNotStatus0();
        return new ResponseEntity<>(listMaterialActive, HttpStatus.OK);
    }

//    @GetMapping("/popup")
//    public String popup(Model model) {
//        return "Attribute/popup";
//    }

    @RequestMapping("/material/add")
    public String add(@ModelAttribute("materialAdd") Material material) {
        material.setStatus(1);
        materialService.save(material);
        this.mess = "Thêm hóa đơn mới thành công!";
        this.materialMess = "1";
        return "redirect:/attribute/material";
    }

    @PostMapping("/material/update-status")
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
            materialService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-material")
    @ResponseBody
    public ResponseEntity<String> updateMaterial(@RequestBody Map<String, Object> payload) {
        try {
            int id;
            String codeMaterial;
            String nameMaterial;

            // Lấy giá trị từ payload
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt((String) payload.get("id"));
            }

            codeMaterial = (String) payload.get("codeMaterial");
            nameMaterial = (String) payload.get("nameMaterial");

            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
            materialService.updateMaterial(id, codeMaterial, nameMaterial);

            return ResponseEntity.ok("Cập nhật dữ liệu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật dữ liệu thất bại");
        }
    }

    @PostMapping("/delete-material")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMaterial(@RequestBody Map<String, Object> payload) {
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
            materialService.updateStatus(id, status);

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
