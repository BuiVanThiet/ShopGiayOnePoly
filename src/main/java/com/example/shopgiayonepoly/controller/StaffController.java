package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.service.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffService staffService;

    private final int pageSize = 1;

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<StaffResponse> listStaff = staffService.getAllStaff();
//        model.addAttribute("staffList", staffService.getAllStaff());
//        return "Staff/list";
//    }

    @GetMapping("/list")
    public String getListStaffByPage(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,Model model) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Staff> pageStaff = staffService.getAllStaffByPage(pageable);
        model.addAttribute("pageStaff", pageStaff);
        model.addAttribute("staff", new StaffRequest());
        return "Staff/list";
    }

    @GetMapping("/search")
    public String searchStaffByKey(@RequestParam(name = "key") String key, Model model) {
        List<StaffResponse> searchStaff = staffService.searchStaffByKeyword(key);
        model.addAttribute("staffList", searchStaff);
        model.addAttribute("staff", new StaffRequest());
        return "Staff/list";
    }

    @GetMapping("/create")
    public String createStaff(ModelMap modelMap){
        modelMap.addAttribute("staff",new StaffRequest());
        return "Staff/create";
    }

    @PostMapping("/add")
    public String addStaff(Model model, @ModelAttribute(name="staff") StaffRequest staffRequest){
        System.out.println("Du lieu khi them cua staff: " + staffRequest.toString());
        Staff staff = new Staff();
        staff.setCodeStaff(staffRequest.getCodeStaff());
        staff.setFullName(staffRequest.getFullName());
        staff.setAddress(staffRequest.getWard() + "," + staffRequest.getDistrict() + "," + staffRequest.getProvince() + "," +staffRequest.getAddRessDetail());
        staff.setNumberPhone(staffRequest.getNumberPhone());
        staff.setBirthDay(staffRequest.getBirthDay());
        staff.setImage(staffRequest.getNameImage());
        staff.setEmail(staffRequest.getEmail());
        staff.setAcount("");
        staff.setPassword("");
        staff.setGender(staffRequest.getGender());
        staff.setRole(staffRequest.getRole());
        staff.setStatus(staffRequest.getStatus());
        Staff staffSave = this.staffService.save(staff);
        staffSave.setAcount(staffSave.getCodeStaff()+staffSave.getId());
        staff.setPassword("@shoponepoly");
        this.staffService.save(staffSave);
        return "redirect:/staff/list";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

    @GetMapping("/edit/{id}")
    public String editStaff(Model model, @PathVariable("id") Integer id) {
        Staff staff = staffService.getOne(id);
        StaffRequest staffRequest = new StaffRequest();
        BeanUtils.copyProperties(staff, staffRequest);
                // Xử lý địa chỉ
        String getAddressDetail = staffRequest.getAddRessDetail();

        if (getAddressDetail != null && !getAddressDetail.isEmpty()) {
            String[] parts = getAddressDetail.split(",\\s*"); // Cắt chuỗi dựa trên dấu phẩy và khoảng trắng

            // Kiểm tra xem chuỗi có đủ phần không
            if (parts.length >= 3) {
                staffRequest.setWard(parts[0]); // Xã/Phường
                staffRequest.setDistrict(parts[1]); // Quận/Huyện
                staffRequest.setProvince(parts[2]); // Tỉnh/Thành phố

                // Nếu địa chỉ còn các phần khác, gộp lại
                if (parts.length > 3) {
                    staffRequest.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(parts, 3, parts.length)));
                } else {
                    staffRequest.setAddRessDetail(""); // Không còn chi tiết địa chỉ khác
                }
            }
        }
        model.addAttribute("staff", staffRequest);
        return "Staff/update";
    }

    @GetMapping("/detail/{id}")
    public String detailStaff(Model model, @PathVariable("id") Integer id) {
        Staff staff = staffService.getOne(id);
        StaffRequest staffRequest = new StaffRequest();
        BeanUtils.copyProperties(staff, staffRequest);
        model.addAttribute("staff", staffRequest);
        return "Staff/detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(RedirectAttributes ra, @PathVariable("id") Integer id) {
        staffService.deleteStaff(id);
        ra.addFlashAttribute("mes", "Xóa thành công nhan vien với ID là: " + id);
        return "redirect:/staff/list";
    }

}
