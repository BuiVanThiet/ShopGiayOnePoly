package com.example.shopgiayonepoly.restController.shift;

import com.example.shopgiayonepoly.dto.request.Shift.TimekeepingFilterRequest;
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

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api-timekeeping")
public class TimekeepingRestController {
    @Autowired
    TimekeepingService timekeepingService;
    @Autowired
    StaffService staffService;
    @Autowired
    ShiftService shiftService;
    TimekeepingFilterRequest timekeepingFilterRequest = null;

    @GetMapping("/list/{page}")
    public List<Object[]> getListTimekeeping(@PathVariable("page") String page, HttpSession session) {
        System.out.println(page + "day la so trang");
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        try {
            if(timekeepingFilterRequest == null) {
                try {
                    // Định dạng để parse chuỗi thành đối tượng Date
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    // Lấy ngày hiện tại
                    Date currentDate = new Date();

                    // Đặt thời gian bắt đầu (00:00:00)
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Date startDate = calendar.getTime();

                    // Đặt thời gian kết thúc (23:59:59)
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    calendar.set(Calendar.MILLISECOND, 999);
                    Date endDate = calendar.getTime();
                    timekeepingFilterRequest = new TimekeepingFilterRequest("",startDate,endDate,"10:00:00","11:00:00",null);
                } catch (Exception e) {
                    return null;
                }
            }
            System.out.println("Start Date: " + timekeepingFilterRequest.getStartDate());
            System.out.println("End Date: " + timekeepingFilterRequest.getEndDate());
            System.out.println("Start Time: " + timekeepingFilterRequest.getStartTime());
            System.out.println("End Time: " + timekeepingFilterRequest.getEndTime());
            System.out.println("Timekeeping_typeCheck: " + timekeepingFilterRequest.getTimekeeping_typeCheck());
            int pageNumber = Integer.parseInt(page); // Nếu không phải số hợp lệ sẽ ném ra NumberFormatException

            Pageable pageable = PageRequest.of(pageNumber-1,5);
            return convertListToPage(timekeepingService.getAllTimekeeping(timekeepingFilterRequest),pageable).getContent();
        }catch (NumberFormatException e) {
            System.out.println("Lỗi: Tham số 'page' không phải là số hợp lệ.");
            return null; // Hoặc trả về một thông báo lỗi nếu cần thiết
        }
    }
    @GetMapping("/max-page-timekeeping")
    public Integer getTimekeepingMaxPage(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        if(timekeepingFilterRequest == null) {
            try {
                // Định dạng để parse chuỗi thành đối tượng Date
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                // Lấy ngày hiện tại
                Date currentDate = new Date();

                // Đặt thời gian bắt đầu (00:00:00)
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date startDate = calendar.getTime();

                // Đặt thời gian kết thúc (23:59:59)
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                Date endDate = calendar.getTime();
                timekeepingFilterRequest = new TimekeepingFilterRequest("",startDate,endDate,"10:00:00","11:00:00",null);
            } catch (Exception e) {
                return null;
            }
        }
        Integer pageNumber = (int) Math.ceil((double) timekeepingService.getAllTimekeeping(timekeepingFilterRequest).size() / 5);
        return pageNumber;
    }

    @PostMapping("/filter-timekeeping")
    public TimekeepingFilterRequest getFilterShift(@RequestBody TimekeepingFilterRequest timekeepingFilterRequest2, HttpSession session) {
            Staff staffLogin = (Staff) session.getAttribute("staffLogin");
            if(staffLogin == null) {
                return null;
            }
            if(staffLogin.getStatus() != 1) {
                return null;
            }

            try {
                // Định dạng để parse chuỗi thành đối tượng Date
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                // Lấy ngày bat dau
                Date currentDate = null;

                if(timekeepingFilterRequest2.getStartDate() == null) {
                    currentDate = new Date();
                }else {
                    currentDate = timekeepingFilterRequest2.getStartDate();
                }

                // Đặt thời gian bắt đầu (00:00:00)
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Date startDate = calendar.getTime();

                Date currentDate2 = null;
                if(timekeepingFilterRequest2.getEndDate() == null) {
                    currentDate2 = new Date();
                }else {
                    currentDate2 = timekeepingFilterRequest2.getEndDate();
                }
                Calendar calendar2 = Calendar.getInstance();
                // Đặt thời gian kết thúc (23:59:59)
                calendar2.setTime(currentDate2);
                calendar2.set(Calendar.HOUR_OF_DAY, 23);
                calendar2.set(Calendar.MINUTE, 59);
                calendar2.set(Calendar.SECOND, 59);
                calendar2.set(Calendar.MILLISECOND, 999);
                Date endDate = calendar2.getTime();

                timekeepingFilterRequest = new TimekeepingFilterRequest(
                        timekeepingFilterRequest2.getSearchTerm(),
                        startDate,
                        endDate,
                        timekeepingFilterRequest2.getStartTime(),
                        timekeepingFilterRequest2.getEndTime(),
                        timekeepingFilterRequest2.getTimekeeping_typeCheck()
                );
                return timekeepingFilterRequest;
            } catch (Exception e) {
                return null;
            }
    }
    @PostMapping("/check-in-staff")
    public ResponseEntity<Map<String,String>> getCheckIn(@RequestBody Map<String,String> data,HttpSession session) {
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

        String checkLogin = getCheckStaffAttendanceYet(staffLogin.getId(),1);
        System.out.println(checkLogin);
        if(checkLogin.equals("Có")) {
            thongBao.put("message","Ca Làm này đã được điểm danh!");
            thongBao.put("check","3");
            System.out.println("da co");
            return ResponseEntity.ok(thongBao);
        }else {
            if(staffLogin.getShift().getStatus() != 1) {
                thongBao.put("message","Ca làm của bạn đang bị xóa hoặc ngừng hoạt động, mời bạn báo cáo cho quản lý!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }

            if(isShiftOver(staffLogin.getShift().getEndTime())) {
                thongBao.put("message","Ca làm của bạn đã qua, mời bạn báo cáo cho quản lý nếu bạn muốn làm việc!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
            System.out.println("ko co");
            String note = data.get("noteData");
            timekeepingService.getCheckIn(staffLogin.getId(), note.trim() == "" || note == null ? "Chấm công vào làm" : note);
            thongBao.put("message","Điểm danh vào làm thành công!");
            thongBao.put("check","1");
            return ResponseEntity.ok(thongBao);
        }
    }
    @PostMapping("/check-out-staff")
    public ResponseEntity<Map<String,String>> getCheckOut(@RequestBody Map<String,String> data,HttpSession session) {
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

        String checkLogin = getCheckStaffAttendanceYet(staffLogin.getId(),1);
        String checkLogOut = getCheckStaffAttendanceYet(staffLogin.getId(),2);
        if (checkLogin.equals("Có") && checkLogOut.equals("Không")) {
            if(staffLogin.getShift().getStatus() != 1) {
                thongBao.put("message","Ca làm của bạn đang bị xóa hoặc ngừng hoạt động, mời bạn báo cáo cho quản lý!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
            String note = data.get("noteData");
            timekeepingService.getCheckOut(staffLogin.getId(), note.trim() == "" || note == null ? "Chấm công ra về" : note);
            thongBao.put("message", "Điểm danh ra về thành công!");
            thongBao.put("check", "1");
            return ResponseEntity.ok(thongBao);
        }else if (checkLogin.equals("Không")) {
            thongBao.put("message", "Chưa điểm danh vào làm!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }else {
            thongBao.put("message", "Đã điểm danh ra về!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
    }

//    @GetMapping("/checkTest/{id}/{type}")
    public String getCheckStaffAttendanceYet(
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
    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

    public boolean isShiftOver(LocalTime endTime) {
        LocalTime currentTime = LocalTime.now(); // Lấy thời gian hiện tại
        return currentTime.isAfter(endTime); // Kiểm tra nếu thời gian hiện tại sau thời gian kết thúc
    }


}
