package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Size;
import com.example.shopgiayonepoly.entites.Size;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CashierInventoryService;
import com.example.shopgiayonepoly.service.TimekeepingService;
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

    @Autowired
    TimekeepingService timekeepingService;
    @Autowired
    CashierInventoryService cashierInventoryService;

    @RequestMapping("/size")
    public String list(Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if(staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
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
        if(staffLogin.getStatus() != 1) {
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
        if(staffLogin.getStatus() != 1) {
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
        if(staffLogin.getStatus() != 1) {
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
        if(staffLogin.getStatus() != 1) {
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
        for (Size listSize : sizeService.findAll()) {
            if (size.getCodeSize().trim().equalsIgnoreCase(listSize.getCodeSize().trim())) {
                checkCode = false;
                break;
            }
        }
        for (Size listSize : sizeService.findAll()) {
            if (size.getNameSize().trim().equalsIgnoreCase(listSize.getNameSize().trim())) {
                checkName = false;
                break;
            }
        }
        if (!checkCode) {
            thongBao.put("message", "Mã kích cỡ đã tồn tại");
            thongBao.put("check", "2");
        } else if (!checkName) {
            thongBao.put("message", "Tên kích cỡ đã tồn tại");
            thongBao.put("check", "2");
        } else if (size.getCodeSize().isEmpty()) {
            thongBao.put("message", "Mã kích cỡ không được để trống");
            thongBao.put("check", "2");
        } else if (size.getNameSize().isEmpty()) {
            thongBao.put("message", "Tên kích cỡ không được để trống");
            thongBao.put("check", "2");
        } else if (size.getCodeSize().length() > 10) {
            thongBao.put("message", "Mã kích cỡ không được dài quá 10 ký tự");
            thongBao.put("check", "2");
        } else if (size.getNameSize().length() > 50) {
            thongBao.put("message", "Tên kích cỡ không được dài quá 50 ký tự");
            thongBao.put("check", "2");
        } else {
            size.setStatus(1);
            sizeService.save(size);
            thongBao.put("message", "Thêm kích cỡ thành công");
            thongBao.put("check", "1");
        }

        return ResponseEntity.ok(thongBao);
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
                if (codeSize.trim().equalsIgnoreCase(listSize.getCodeSize().trim()) && id != listSize.getId()) {
                    checkCode = false;
                    break;
                }
            }
            boolean checkName = true;
            for (Size listSize : sizeService.findAll()) {
                if (nameSize.trim().equalsIgnoreCase(listSize.getNameSize().trim()) && id != listSize.getId()) {
                    checkName = false;
                    break;
                }
            }
            // Gọi service để cập nhật dữ liệu kích cỡ trong cơ sở dữ liệu
            if (!checkCode) {
                thongBao.put("message", "Mã kích cỡ đã tồn tại");
                thongBao.put("check", "2");
            } else if (!checkName) {
                thongBao.put("message", "Tên kích cỡ đã tồn tại");
                thongBao.put("check", "2");
            } else if (codeSize.isEmpty()) {
                thongBao.put("message", "Mã kích cỡ không được để trống");
                thongBao.put("check", "2");
            } else if (nameSize.isEmpty()) {
                thongBao.put("message", "Tên kích cỡ không được để trống");
                thongBao.put("check", "2");
            } else if (codeSize.length() > 10) {
                thongBao.put("message", "Mã kích cỡ không được dài quá 10 ký tự");
                thongBao.put("check", "2");
            } else if (nameSize.length() > 50) {
                thongBao.put("message", "Tên kích cỡ không được dài quá 50 ký tự");
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

    protected Map<String,String> checkLoginAndLogOutByStaff(Integer idStaff) {
        Map<String,String> thongBao = new HashMap<>();
        String checkLogin = getCheckStaffAttendanceYetBill(idStaff,1);
        String checkLogOut = getCheckStaffAttendanceYetBill(idStaff,2);
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
