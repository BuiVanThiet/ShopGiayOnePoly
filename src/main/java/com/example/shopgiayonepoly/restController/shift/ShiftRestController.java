package com.example.shopgiayonepoly.restController.shift;

import com.example.shopgiayonepoly.dto.request.Shift.ShiftApplyRequest;
import com.example.shopgiayonepoly.dto.request.Shift.ShiftFilterRequest;
import com.example.shopgiayonepoly.dto.request.Shift.ShiftRequest;
import com.example.shopgiayonepoly.entites.Shift;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.ShiftService;
import com.example.shopgiayonepoly.service.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    Integer idShift = null;
    String keySearch = "";
    Integer checkShift = 1;
    ShiftFilterRequest shiftFilterRequest = null;
    @GetMapping("/list/{page}")
    public List<Object[]> getAllShift(@PathVariable("page") String page, HttpSession session) {
        if(shiftFilterRequest == null) {
            shiftFilterRequest = new ShiftFilterRequest(null,null,null,null,null,null);
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        return convertListToPage(shiftService.getAllShiftByTime(shiftFilterRequest.getStartTimeBegin(), shiftFilterRequest.getStartTimeEnd(), shiftFilterRequest.getEndTimeBegin(), shiftFilterRequest.getEndTimeEnd(), shiftFilterRequest.getStatusShift(), shiftFilterRequest.getStatus()),pageable).getContent();
    }
    @GetMapping("/max-page")
    public Integer getMaxPageShift(HttpSession session) {
        if(shiftFilterRequest == null) {
            shiftFilterRequest = new ShiftFilterRequest(null,null,null,null,null,null);
        }
        Integer pageNumber = (int) Math.ceil((double) shiftService.getAllShiftByTime(shiftFilterRequest.getStartTimeBegin(), shiftFilterRequest.getStartTimeEnd(), shiftFilterRequest.getEndTimeBegin(), shiftFilterRequest.getEndTimeEnd(), shiftFilterRequest.getStatusShift(), shiftFilterRequest.getStatus()).size() / 5);
        return pageNumber;
    }
    @PostMapping("/filter-shift")
    public ShiftFilterRequest getFilterShift(@RequestBody ShiftFilterRequest shiftFilterRequest2,HttpSession session) {
        shiftFilterRequest = shiftFilterRequest2;
        return shiftFilterRequest;
    }

    @GetMapping("/restore-shift/{id}")
    public ResponseEntity<Map<String,String>> getRestoreShift(@PathVariable("id") String id) {
        Map<String,String> thongBao = new HashMap<>();
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
    public ResponseEntity<Map<String,String>> getDeleteShift(@PathVariable("id") String id) {
        Map<String,String> thongBao = new HashMap<>();
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
    public ResponseEntity<Map<String,String>> getAddOrUpdateShift(@RequestBody ShiftRequest shiftRequest) {
        Map<String,String> thongBao = new HashMap<>();


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
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        return convertListToPage(this.shiftService.getAllStaffByShift(idShift,keySearch,checkShift),pageable).getContent();
    }

    @GetMapping("/max-page-list-staff")
    public Integer getMaxPageListCustomer(HttpSession session) {
        Integer pageNumber = (int) Math.ceil((double) this.shiftService.getAllStaffByShift(idShift,keySearch,checkShift).size() / 5);
        return pageNumber;
    }
    @PostMapping("/filter-list-staff")
    public ResponseEntity<?> getFilterListCustomer(@RequestBody Map<String, String> requestData,HttpSession session) {
        String idShiftData = requestData.get("idShiftData");
        String keySearchData = requestData.get("keySearchData");
        String checkShiftData = requestData.get("checkShiftData");
        idShift = Integer.parseInt(idShiftData);
        keySearch = keySearchData;
        checkShift = Integer.parseInt(checkShiftData);
        return ResponseEntity.ok("done");
    }

    @PostMapping("/save-or-update-shift-in-staff")
    public ResponseEntity<Map<String,String>> getAddOrUpdateShift(@RequestBody ShiftApplyRequest data) {
        Map<String,String> thongBao = new HashMap<>();

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
    public ResponseEntity<Map<String,String>> getRemoveShiftStaff(@RequestBody ShiftApplyRequest data) {
        Map<String,String> thongBao = new HashMap<>();

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

    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }
}
