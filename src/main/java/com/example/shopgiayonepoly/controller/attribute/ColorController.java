package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CashierInventoryService;
import com.example.shopgiayonepoly.service.TimekeepingService;
import com.example.shopgiayonepoly.service.attribute.ColorService;
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
public class ColorController {

    @Autowired
    ColorService colorService;

    @Autowired
    TimekeepingService timekeepingService;
    @Autowired
    CashierInventoryService cashierInventoryService;

    @RequestMapping("/color")
    public String list(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if(staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        model.addAttribute("colorList", colorService.getColorNotStatus0());
        model.addAttribute("colorAdd", new Color());
        return "Attribute/color";
    }

    @GetMapping("/color/delete")
    public ResponseEntity<List<Color>> listColorDelete(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<Color> deletedColors = colorService.getColorDelete();
        return new ResponseEntity<>(deletedColors, HttpStatus.OK);
    }

    @GetMapping("/color/active")
    public ResponseEntity<List<Color>> listActive(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<Color> listColorActive = colorService.getColorNotStatus0();
        return new ResponseEntity<>(listColorActive, HttpStatus.OK);
    }

    @GetMapping("/color/get-code")
    public ResponseEntity<List<String>> findAllCodeColor(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<String> codeColor = new ArrayList<>();
        for (Color listColor : colorService.findAll()) {
            codeColor.add(listColor.getCodeColor());
        }
        return new ResponseEntity<>(codeColor, HttpStatus.OK);
    }

    @GetMapping("/color/get-name")
    public ResponseEntity<List<String>> findAllNameColor(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<String> nameColor = new ArrayList<>();
        for (Color listColor : colorService.findAll()) {
            nameColor.add(listColor.getNameColor());
        }
        return new ResponseEntity<>(nameColor, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/color/add")
    public ResponseEntity<Map<String, String>> add(@ModelAttribute Color color, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        for (Color listColor : colorService.findAll()) {
            if (color.getCodeColor().trim().toLowerCase().equals(listColor.getCodeColor().trim().toLowerCase())) {
                checkCode = false;
            }
        }
        for (Color listColor : colorService.findAll()) {
            if (color.getNameColor().trim().toLowerCase().equals(listColor.getNameColor().trim().toLowerCase())) {
                checkName = false;
            }
        }
        if (!checkCode) {
            thongBao.put("message", "Mã màu sắc đã tồn tại");
            thongBao.put("check", "2");
        } else if (!checkName) {
            thongBao.put("message", "Tên màu đã tồn tại");
            thongBao.put("check", "2");
        } else if (color.getCodeColor().isEmpty()) {
            thongBao.put("message", "Mã màu không được để trống");
            thongBao.put("check", "2");
        } else if (color.getNameColor().isEmpty()) {
            thongBao.put("message", "Tên màu không được để trống");
            thongBao.put("check", "2");
        } else if (color.getCodeColor().length() > 10) {
            thongBao.put("message", "Mã màu không được dài quá 10 ký tự");
            thongBao.put("check", "2");
        } else if (color.getNameColor().length() > 50) {
            thongBao.put("message", "Tên màu không được dài quá 50 ký tự");
            thongBao.put("check", "2");
        } else {
            color.setStatus(1);
            colorService.save(color);
            thongBao.put("message", "Thêm màu sắc thành công");
            thongBao.put("check", "1");
        }

        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/color/update-status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> payload, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
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
            colorService.updateStatus(id, status);

            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái");
        }
    }

    @PostMapping("/update-color")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateColor(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
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
            boolean checkCode = true;
            for (Color listColor : colorService.findAll()) {
                if (codeColor.trim().toLowerCase().equals(listColor.getCodeColor().trim().toLowerCase()) && id != listColor.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Color listColor : colorService.findAll()) {
                if (nameColor.trim().toLowerCase().equals(listColor.getNameColor().trim().toLowerCase()) && id != listColor.getId()) {
                    checkName = false;
                    break; 
                }
            }
            // Gọi service để cập nhật dữ liệu màu sắc trong cơ sở dữ liệu
            if (codeColor.isEmpty() || codeColor.length() > 10) {
                thongBao.put("message", "Mã màu sắc không được trống và lớn hơn 10 kí tự");
                thongBao.put("check", "2");
            } else if (!checkCode) {
                thongBao.put("message", "Mã màu sắc đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên màu sắc đã tồn tại");
                thongBao.put("check", "2");
            } else if (nameColor.isEmpty() || nameColor.length() > 50) {
                thongBao.put("message", "Tên màu sắc không được trống và lớn hơn 50 kí tự");
                thongBao.put("check", "2");
            } else {
                colorService.updateColor(id, codeColor, nameColor);
                thongBao.put("message", "Sửa màu sắc thành công");
                thongBao.put("check", "1");
            }
            return ResponseEntity.ok(thongBao);
        } catch (Exception e) {
            thongBao.put("message", "Sửa màu sắc thất bại");
            thongBao.put("check", "2");
            return ResponseEntity.ok(thongBao);
        }
    }

    @PostMapping("/delete-color")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteColor(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
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

    protected Map<String,String> checkLoginAndLogOutByStaff(Integer idStaff) {
        Map<String,String> thongBao = new HashMap<>();
        String checkLogin = getCheckStaffAttendanceYetBill(idStaff,1);
        String checkLogOut = getCheckStaffAttendanceYetBill(idStaff,2);
        System.out.println(checkLogin);
        if(!checkLogin.equals("Có")) {
            thongBao.put("message","Mời bạn điểm danh trước khi làm việc!");
            return thongBao;
        }

        if(checkLogin.equals("Có") && checkLogOut.equals("Có")) {
            thongBao.put("message","Bạn đã điểm danh vào và ra rồi, không thể làm việc được nữa!");
            return thongBao;
        }
        thongBao.put("message","");
        return thongBao;
    }


    protected String getCheckStaffAttendanceYetBill(
//            @PathVariable("id") Integer idStaff,@PathVariable("type") Integer timekeepingTypeCheck
            Integer idStaff, Integer timekeepingTypeCheck
    ) {
        List<Object[]> checkLoginLogOut = this.timekeepingService.getCheckStaffAttendanceYet(idStaff, timekeepingTypeCheck);

        // Kiểm tra nếu danh sách không rỗng và có kết quả
        if (!checkLoginLogOut.isEmpty() && checkLoginLogOut.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            return checkLoginLogOut.get(0)[0].toString();
        }
        // Trường hợp không có dữ liệu
        return "Không";
    }

}
