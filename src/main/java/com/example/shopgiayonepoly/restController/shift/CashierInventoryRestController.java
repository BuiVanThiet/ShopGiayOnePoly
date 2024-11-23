package com.example.shopgiayonepoly.restController.shift;

import com.example.shopgiayonepoly.dto.request.Shift.CashierInventoryFilterByIdStaffRequest;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CashierInventoryService;
import com.example.shopgiayonepoly.service.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api-cashierInventory")
public class CashierInventoryRestController {
    @Autowired
    StaffService staffService;
    @Autowired
    CashierInventoryService cashierInventoryService;

    CashierInventoryFilterByIdStaffRequest cashierInventoryFilterByIdStaffRequest = null;

    @GetMapping("/list/{page}")
    public List<Object[]> getListCashierInventory(@PathVariable("page") String page, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        try {
            if(cashierInventoryFilterByIdStaffRequest == null) {
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
                    cashierInventoryFilterByIdStaffRequest = new CashierInventoryFilterByIdStaffRequest("",startDate,endDate);
                } catch (Exception e) {
                    return null;
                }
            }
            int pageNumber = Integer.parseInt(page); // Nếu không phải số hợp lệ sẽ ném ra NumberFormatException

            Pageable pageable = PageRequest.of(pageNumber-1,5);
            return convertListToPage(cashierInventoryService.getAllCashierInventoryByStaff(
                    cashierInventoryFilterByIdStaffRequest.getKeyStaff(),
                    cashierInventoryFilterByIdStaffRequest.getStartDate(),
                    cashierInventoryFilterByIdStaffRequest.getEndDate()),pageable).getContent();
        }catch (NumberFormatException e) {
            System.out.println("Lỗi: Tham số 'page' không phải là số hợp lệ.");
            return null; // Hoặc trả về một thông báo lỗi nếu cần thiết
        }
    }

    @GetMapping("/max-page-cashierInventory")
    public Integer getTimekeepingMaxPage(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        if(cashierInventoryFilterByIdStaffRequest == null) {
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
                cashierInventoryFilterByIdStaffRequest = new CashierInventoryFilterByIdStaffRequest("",startDate,endDate);
            } catch (Exception e) {
                return null;
            }
        }
        Integer pageNumber = (int) Math.ceil((double) cashierInventoryService.getAllCashierInventoryByStaff(
                cashierInventoryFilterByIdStaffRequest.getKeyStaff(),
                cashierInventoryFilterByIdStaffRequest.getStartDate(),
                cashierInventoryFilterByIdStaffRequest.getEndDate()).size() / 5);
        return pageNumber;
    }


    @PostMapping("/filter-cashierInventory")
    public CashierInventoryFilterByIdStaffRequest getFilterCashierInventory(@RequestBody CashierInventoryFilterByIdStaffRequest cashierInventoryFilterByIdStaffRequest2, HttpSession session) {
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

            if(cashierInventoryFilterByIdStaffRequest2.getStartDate() == null) {
                currentDate = new Date();
            }else {
                currentDate = cashierInventoryFilterByIdStaffRequest2.getStartDate();
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
            if(cashierInventoryFilterByIdStaffRequest2.getEndDate() == null) {
                currentDate2 = new Date();
            }else {
                currentDate2 = cashierInventoryFilterByIdStaffRequest2.getEndDate();
            }
            Calendar calendar2 = Calendar.getInstance();
            // Đặt thời gian kết thúc (23:59:59)
            calendar2.setTime(currentDate2);
            calendar2.set(Calendar.HOUR_OF_DAY, 23);
            calendar2.set(Calendar.MINUTE, 59);
            calendar2.set(Calendar.SECOND, 59);
            calendar2.set(Calendar.MILLISECOND, 999);
            Date endDate = calendar2.getTime();

            cashierInventoryFilterByIdStaffRequest = new CashierInventoryFilterByIdStaffRequest(
                    cashierInventoryFilterByIdStaffRequest2.getKeyStaff(),
                    startDate,
                    endDate
            );
            return cashierInventoryFilterByIdStaffRequest;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/infor-detail-cashierInventory/{page}")
    public List<Object[]> getInfoDetailCashierInventory(@PathVariable("page") String page,HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null) {
            return null;
        }
        if(staff.getStatus() != 1) {
            return null;
        }

        try {
            if(cashierInventoryFilterByIdStaffRequest == null) {
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

                    LocalTime currentTime = LocalTime.now();
                    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String currentTimeString = currentTime.format(formatterTime);

                    cashierInventoryFilterByIdStaffRequest = new CashierInventoryFilterByIdStaffRequest(staff.getId(),startDate,endDate,currentTimeString,currentTimeString);
                } catch (Exception e) {
                    return null;
                }
            }
            int pageNumber = Integer.parseInt(page); // Nếu không phải số hợp lệ sẽ ném ra NumberFormatException

            Pageable pageable = PageRequest.of(pageNumber-1,5);
            return convertListToPage(cashierInventoryService.getAllCashierInventoryByIdStaff(
                    cashierInventoryFilterByIdStaffRequest.getIdStaff(),
                    cashierInventoryFilterByIdStaffRequest.getStartDate(),
                    cashierInventoryFilterByIdStaffRequest.getEndDate(),
                    cashierInventoryFilterByIdStaffRequest.getStartTime(),
                    cashierInventoryFilterByIdStaffRequest.getEndTime()),pageable).getContent();
        }catch (NumberFormatException e) {
            System.out.println("Lỗi: Tham số 'page' không phải là số hợp lệ.");
            return null; // Hoặc trả về một thông báo lỗi nếu cần thiết
        }
    }

    @GetMapping("/max-page-cashierInventory-by-idStaff")
    public Integer getTimekeepingMaxPageByIdStaff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null) {
            return null;
        }
        if(staff.getStatus() != 1) {
            return null;
        }

        if(cashierInventoryFilterByIdStaffRequest == null) {
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
                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                String currentTimeString = currentTime.format(formatterTime);

                cashierInventoryFilterByIdStaffRequest = new CashierInventoryFilterByIdStaffRequest(staff.getId(),startDate,endDate,currentTimeString,currentTimeString);
            } catch (Exception e) {
                return null;
            }
        }
        Integer pageNumber = (int) Math.ceil((double) cashierInventoryService.getAllCashierInventoryByIdStaff(
                cashierInventoryFilterByIdStaffRequest.getIdStaff(),
                cashierInventoryFilterByIdStaffRequest.getStartDate(),
                cashierInventoryFilterByIdStaffRequest.getEndDate(),
                cashierInventoryFilterByIdStaffRequest.getStartTime(),
                cashierInventoryFilterByIdStaffRequest.getEndTime()).size() / 5);
        return pageNumber;
    }

    @PostMapping("/filter-cashierInventory-by-idStaff")
    public CashierInventoryFilterByIdStaffRequest getFilterCashierInventoryByIdStaff(@RequestBody CashierInventoryFilterByIdStaffRequest cashierInventoryFilterByIdStaffRequest2, HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null) {
            return null;
        }
        if(staff.getStatus() != 1) {
            return null;
        }

        try {
            // Định dạng để parse chuỗi thành đối tượng Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            // Lấy ngày bat dau
            Date currentDate = null;

            if(cashierInventoryFilterByIdStaffRequest2.getStartDate() == null) {
                currentDate = new Date();
            }else {
                currentDate = cashierInventoryFilterByIdStaffRequest2.getStartDate();
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
            if(cashierInventoryFilterByIdStaffRequest2.getEndDate() == null) {
                currentDate2 = new Date();
            }else {
                currentDate2 = cashierInventoryFilterByIdStaffRequest2.getEndDate();
            }
            Calendar calendar2 = Calendar.getInstance();
            // Đặt thời gian kết thúc (23:59:59)
            calendar2.setTime(currentDate2);
            calendar2.set(Calendar.HOUR_OF_DAY, 23);
            calendar2.set(Calendar.MINUTE, 59);
            calendar2.set(Calendar.SECOND, 59);
            calendar2.set(Calendar.MILLISECOND, 999);
            Date endDate = calendar2.getTime();

            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            String currentTimeString = currentTime.format(formatterTime);

            if(cashierInventoryFilterByIdStaffRequest2.getStartTime() == null) {
                cashierInventoryFilterByIdStaffRequest2.setStartTime(currentTimeString);
            }

            if(cashierInventoryFilterByIdStaffRequest2.getEndTime() == null) {
                cashierInventoryFilterByIdStaffRequest2.setEndTime(currentTimeString);
            }

            cashierInventoryFilterByIdStaffRequest = new CashierInventoryFilterByIdStaffRequest(
                    staff.getId(),
                    startDate,
                    endDate,
                    cashierInventoryFilterByIdStaffRequest2.getStartTime(),
                    cashierInventoryFilterByIdStaffRequest2.getEndTime()
            );
            return cashierInventoryFilterByIdStaffRequest;
        } catch (Exception e) {
            return null;
        }
    }
    ///validate same

    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }
}
