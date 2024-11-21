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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attribute")
public class SoleController {

    @Autowired
    SoleService soleService;

    @RequestMapping("/sole")
    public String list(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        model.addAttribute("soleList", soleService.getSoleNotStatus0());
        model.addAttribute("soleAdd", new Sole());
        return "Attribute/sole";
    }

    @GetMapping("/sole/delete")
    public ResponseEntity<List<Sole>> listSoleDelete(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Sole> deletedSoles = soleService.getSoleDelete();
        return new ResponseEntity<>(deletedSoles, HttpStatus.OK);
    }

    @GetMapping("/sole/active")
    public ResponseEntity<List<Sole>> listActive(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<Sole> listSoleActive = soleService.getSoleNotStatus0();
        return new ResponseEntity<>(listSoleActive, HttpStatus.OK);
    }

    @GetMapping("/sole/get-code")
    public ResponseEntity<List<String>> findAllCodeSole(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> codeSole = new ArrayList<>();
        for (Sole listSole : soleService.findAll()) {
            codeSole.add(listSole.getCodeSole());
        }
        return new ResponseEntity<>(codeSole, HttpStatus.OK);
    }

    @GetMapping("/sole/get-name")
    public ResponseEntity<List<String>> findAllNameSole(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        List<String> nameSole = new ArrayList<>();
        for (Sole listSole : soleService.findAll()) {
            nameSole.add(listSole.getNameSole());
        }
        return new ResponseEntity<>(nameSole, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/sole/add")
    public ResponseEntity<Map<String, String>> add(@ModelAttribute Sole sole, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        for (Sole listSole : soleService.findAll()) {
            if (sole.getCodeSole().trim().toLowerCase().equals(listSole.getCodeSole().trim().toLowerCase())) {
                checkCode = false;
            }
        }
        for (Sole listSole : soleService.findAll()) {
            if (sole.getNameSole().trim().toLowerCase().equals(listSole.getNameSole().trim().toLowerCase())) {
                checkName = false;
            }
        }
        if (!checkCode || !checkName || sole.getCodeSole().isEmpty() || sole.getNameSole().isEmpty() || sole.getCodeSole().length() > 10 || sole.getNameSole().length() > 50) {
            thongBao.put("message", "Dữ liệu không hợp lệ");
            thongBao.put("check", "2");
        } else {
            sole.setStatus(1);
            soleService.save(sole);
            thongBao.put("message", "Thêm đế giày thành công");
            thongBao.put("check", "1");
        }
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/sole/update-status")
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
            soleService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-sole")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateSole(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
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
            boolean checkCode = true;
            for (Sole listSole : soleService.findAll()) {
                if (codeSole.trim().toLowerCase().equals(listSole.getCodeSole().trim().toLowerCase()) && id != listSole.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Sole listSole : soleService.findAll()) {
                if (nameSole.trim().toLowerCase().equals(listSole.getNameSole().trim().toLowerCase()) && id != listSole.getId()) {
                    checkName = false;
                    break;
                }
            }
            // Gọi service để cập nhật dữ liệu đế giày trong cơ sở dữ liệu
            if (codeSole.isEmpty() || codeSole.length() > 10) {
                thongBao.put("message", "Mã đế giày không được trống và lớn hơn 10 kí tự");
                thongBao.put("check", "2");
            } else if (!checkCode) {
                thongBao.put("message", "Mã đế giày đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên đế giày đã tồn tại");
                thongBao.put("check", "2");
            } else if (nameSole.isEmpty() || nameSole.length() > 50) {
                thongBao.put("message", "Tên đế giày không được trống và lớn hơn 50 kí tự");
                thongBao.put("check", "2");
            } else {
                soleService.updateSole(id, codeSole, nameSole);
                thongBao.put("message", "Sửa đế giày thành công");
                thongBao.put("check", "1");
            }
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            thongBao.put("message", "Sửa đế giày thất bại");
            thongBao.put("check", "2");
            return ResponseEntity.ok(thongBao);
        }
    }

    @PostMapping("/delete-sole")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteSole(@RequestBody Map<String, Object> payload, HttpSession session) {
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
                thongBao.put("message", "Xóa đế giày thành công");
                thongBao.put("check", "1");
            } else {
                thongBao.put("message", "Khôi phục đế giày thành công");
                thongBao.put("check", "1");
            }
            // Gọi service để cập nhật trạng thái trong cơ sở dữ liệu
            soleService.updateStatus(id, status);
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
