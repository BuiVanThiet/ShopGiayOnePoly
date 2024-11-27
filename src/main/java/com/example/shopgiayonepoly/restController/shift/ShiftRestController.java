package com.example.shopgiayonepoly.restController.shift;

import com.example.shopgiayonepoly.dto.request.Shift.ShiftApplyRequest;
import com.example.shopgiayonepoly.dto.request.Shift.ShiftFilterRequest;
import com.example.shopgiayonepoly.dto.request.Shift.ShiftRequest;
import com.example.shopgiayonepoly.entites.Shift;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.ShiftService;
import com.example.shopgiayonepoly.service.StaffService;
import com.example.shopgiayonepoly.service.TimekeepingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shift-api")
public class ShiftRestController {
    @Autowired
    ShiftService shiftService;
    @Autowired
    StaffService staffService;
    @Autowired
    TimekeepingService timekeepingService;
    Integer idShift = null;
    String keySearch = "";
    Integer checkShift = 1;
    ShiftFilterRequest shiftFilterRequest = null;
    @GetMapping("/list/{page}")
    public List<Object[]> getAllShift(@PathVariable("page") String page, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        if(shiftFilterRequest == null) {
            shiftFilterRequest = new ShiftFilterRequest(null,null,null,null,null,null);
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        return convertListToPage(shiftService.getAllShiftByTime(shiftFilterRequest.getStartTimeBegin(), shiftFilterRequest.getStartTimeEnd(), shiftFilterRequest.getEndTimeBegin(), shiftFilterRequest.getEndTimeEnd(), shiftFilterRequest.getStatusShift(), shiftFilterRequest.getStatus()),pageable).getContent();
    }
    @GetMapping("/max-page")
    public Integer getMaxPageShift(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        if(shiftFilterRequest == null) {
            shiftFilterRequest = new ShiftFilterRequest(null,null,null,null,null,null);
        }
        Integer pageNumber = (int) Math.ceil((double) shiftService.getAllShiftByTime(shiftFilterRequest.getStartTimeBegin(), shiftFilterRequest.getStartTimeEnd(), shiftFilterRequest.getEndTimeBegin(), shiftFilterRequest.getEndTimeEnd(), shiftFilterRequest.getStatusShift(), shiftFilterRequest.getStatus()).size() / 5);
        return pageNumber;
    }
    @PostMapping("/filter-shift")
    public ShiftFilterRequest getFilterShift(@RequestBody ShiftFilterRequest shiftFilterRequest2,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        shiftFilterRequest = shiftFilterRequest2;
        return shiftFilterRequest;
    }

    @GetMapping("/restore-shift/{id}")
    public ResponseEntity<Map<String,String>> getRestoreShift(@PathVariable("id") String id,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Shift shift = this.shiftService.findById(Integer.parseInt(id)).orElse(null);
        if(shift == null) {
            thongBao.put("message","Ca làm không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        shift.setStatus(1);
        shift.setUpdateDate(new Date());
        this.shiftService.save(shift);
        thongBao.put("message","Khôi phục ca làm thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/delete-shift/{id}")
    public ResponseEntity<Map<String,String>> getDeleteShift(@PathVariable("id") String id,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
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

        Shift shift = this.shiftService.findById(Integer.parseInt(id)).orElse(null);
        if(shift == null) {
            thongBao.put("message","Ca làm không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        shift.setStatus(0);
        shift.setUpdateDate(new Date());
        this.shiftService.save(shift);
        thongBao.put("message","Xóa ca làm thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/add-or-update-shift")
    public ResponseEntity<Map<String,String>> getAddOrUpdateShift(@RequestBody ShiftRequest shiftRequest,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
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

        Shift shift = null;
        if(shiftRequest.getId() == null) {
            shift = new Shift();
            shift.setId(shiftRequest.getId());
            shift.setStartTime(shiftRequest.getStartTime());
            shift.setEndTime(shiftRequest.getEndTime());
            shift.setUpdateDate(new Date());
            shift.setStatus(shiftRequest.getStatus());
            thongBao.put("message","Thêm ca làm thành công!");
        }else {
            shift = this.shiftService.findById(shiftRequest.getId()).orElse(null);
            if(shift == null) {
                thongBao.put("message","Ca làm không tồn tại!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
            shift.setStartTime(shiftRequest.getStartTime());
            shift.setEndTime(shiftRequest.getEndTime());
            shift.setUpdateDate(new Date());
            shift.setStatus(shiftRequest.getStatus());
            thongBao.put("message","Sửa ca làm thành công!");
        }
        this.shiftService.save(shift);
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    //danh sacsh nhan vien

    @GetMapping("/list-staff/{page}")
    public List<Object[]> getListCustomer(@PathVariable("page") String page,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        return convertListToPage(this.shiftService.getAllStaffByShift(idShift,keySearch,checkShift),pageable).getContent();
    }

    @GetMapping("/max-page-list-staff")
    public Integer getMaxPageListCustomer(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer pageNumber = (int) Math.ceil((double) this.shiftService.getAllStaffByShift(idShift,keySearch,checkShift).size() / 5);
        return pageNumber;
    }
    @PostMapping("/filter-list-staff")
    public ResponseEntity<?> getFilterListCustomer(@RequestBody Map<String, String> requestData,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        String idShiftData = requestData.get("idShiftData");
        String keySearchData = requestData.get("keySearchData");
        String checkShiftData = requestData.get("checkShiftData");
        idShift = Integer.parseInt(idShiftData);
        keySearch = keySearchData;
        checkShift = Integer.parseInt(checkShiftData);
        return ResponseEntity.ok("done");
    }

    @PostMapping("/save-or-update-shift-in-staff")
    public ResponseEntity<Map<String,String>> getAddOrUpdateShift(@RequestBody ShiftApplyRequest data,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
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

        Shift shift = this.shiftService.findById(data.getIdShift()).orElse(null);

        if(shift == null) {
            thongBao.put("message","Ca làm không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        for (Integer idStaff: data.getStaffIds()) {
            Staff staff = this.staffService.getStaffByID(idStaff);
            if(staff != null) {
                staff.setShift(shift);
                staff.setUpdateDate(new Date());
                this.staffService.save(staff);
            }
        }
        thongBao.put("message","Thêm ca làm vào nhân viên thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/remove-shift-in-staff")
    public ResponseEntity<Map<String,String>> getRemoveShiftStaff(@RequestBody ShiftApplyRequest data,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
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


        for (Integer idSt: data.getStaffIds()) {
            Staff staff = this.staffService.getStaffByID(idSt);
            if(staff != null) {
                staff.setShift(null);
                staff.setUpdateDate(new Date());
                this.staffService.save(staff);
            }
        }
        thongBao.put("message","Xóa ca làm cho nhân viên thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    //validate trùng
    @GetMapping("/check-shift-same")
    public ResponseEntity<List<Shift>> getCheckSaneShift() {
        return ResponseEntity.ok(this.shiftService.findAll());
    }
    //check co nhan vien lam khong
    @GetMapping("/check-shift-staff-working/{idShift}")
    public String getCheckShiftStaffWorking(@PathVariable("idShift") Integer idShift,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<Object[]> list = this.shiftService.getCheckShiftStaffWorking(idShift);
        if (!list.isEmpty() && list.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            String mess = list.get(0)[0].toString();
            System.out.println("mess check shift: " + mess);
            return mess;
        }
        return null;
    }
    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
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
