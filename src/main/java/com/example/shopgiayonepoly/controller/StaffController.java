package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.baseMethod.BaseEmail;
import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.CustomerRepository;
import com.example.shopgiayonepoly.repositores.StaffRepository;
import com.example.shopgiayonepoly.service.StaffService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController extends BaseEmail {
    @Autowired
    StaffService staffService;

//    @Autowired
//    StaffRepository staffRepository;

//    @Autowired
//    CustomerRepository customerRepository;

    private final int pageSize = 5;

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<StaffResponse> listStaff = staffService.getAllStaff();
//        model.addAttribute("staffList", staffService.getAllStaff());
//        return "Staff/list";
//    }

    @GetMapping("/list")
    public String getListStaffByPage(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber, Model model,HttpSession session) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null || staff.getId() == null) {
            return "redirect:/login";
        }

        Page<StaffResponse> pageStaff = staffService.getAllStaffByPage(pageable, staff.getId());
        model.addAttribute("pageStaff", pageStaff);
        model.addAttribute("staff", new StaffRequest());
        return "Staff/list";
    }

    @GetMapping("/search")
    public String searchStaffByKey(@RequestParam(name = "key") String key, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber, Model model) {
        String trimmedKey = key != null ? key.trim() : null;
        Pageable pageableSearch = PageRequest.of(pageNumber, pageSize);
        Page<StaffResponse> pageStaff = staffService.searchStaffByKeywordPage(trimmedKey, pageableSearch);
//        model.addAttribute("staffList", searchStaff);
        model.addAttribute("pageStaff", pageStaff);
        model.addAttribute("staff", new StaffRequest());
        return "Staff/list";
    }

    @GetMapping("/create")
    public String createStaff(ModelMap modelMap) {
        modelMap.addAttribute("staff", new StaffRequest());
        return "Staff/create";
    }

    @PostMapping("/add")
    public String addStaff(Model model, @Valid @ModelAttribute(name = "staff") StaffRequest staffRequest, BindingResult result) throws IOException {
// Kiểm tra tên
        if (staffRequest.getFullName() == null || staffRequest.getFullName().trim().isEmpty()) {
            result.rejectValue("fullName", "error.staff", "Tên không được để trống!"); // Thông báo nếu tên rỗng hoặc chỉ chứa khoảng trắng
        } else if (staffRequest.getFullName().length() < 2 || staffRequest.getFullName().length() > 50) {
            result.rejectValue("fullName", "error.staff", "Tên phải có độ dài từ 2 đến 50 ký tự!");
        } else if (!staffRequest.getFullName().matches("^[\\p{L} ]+$")) {
            result.rejectValue("fullName", "error.staff", "Tên chỉ được chứa ký tự chữ cái và dấu cách!");
        }
// Kiểm tra mã
        if (staffRequest.getCodeStaff() == null || staffRequest.getCodeStaff().trim().isEmpty()) {
            result.rejectValue("codeStaff", "error.staff", "Mã không được để trống!"); // Thông báo nếu mã rỗng hoặc chỉ chứa khoảng trắng
        } else if (staffRequest.getCodeStaff().length() < 3 || staffRequest.getCodeStaff().length() > 10) {
            result.rejectValue("codeStaff", "error.staff", "Mã phải có độ dài từ 3 đến 10 ký tự!");
        } else if (staffService.existsByCodeStaff(staffRequest.getCodeStaff())) {
            result.rejectValue("codeStaff", "error.staff", "Mã đã tồn tại!"); // Thông báo nếu mã đã tồn tại
        }
// Kiểm tra số điện thoại
        if (staffRequest.getNumberPhone() == null || staffRequest.getNumberPhone().isEmpty()) {
            result.rejectValue("numberPhone", "error.staff", "Số điện thoại không được để trống!");
        } else if (!staffRequest.getNumberPhone().matches("^(0[3|5|7|8|9])+([0-9]{8})$")) {
            result.rejectValue("numberPhone", "error.staff", "Số điện thoại không hợp lệ!");
        }
//        // Kiểm tra email
//        if (staffRequest.getEmail() == null || staffRequest.getEmail().isEmpty()) {
//            result.rejectValue("email", "error.customer", "Email không được để trống!");
//        }
        // Kiểm tra ngày sinh
        if (staffRequest.getBirthDay() == null) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được để trống!");
        } else if (staffRequest.getBirthDay().isAfter(LocalDate.now())) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được lớn hơn ngày hiện tại!");
        }
        // Kiểm tra email
//        if (staffRequest.getEmail() == null || staffRequest.getEmail().isEmpty()) {
//            result.rejectValue("email", "error.staff", "Email không được để trống!");
//        } else if (customerRepository.existsByEmail(staffRequest.getEmail()) || staffRepository.existsByEmail(staffRequest.getEmail())) {
//            result.rejectValue("email", "error.staff", "Email đã được sử dụng!");
//        }
        if (result.hasErrors()) {
            model.addAttribute("mes", "Thêm thất bại");
            // Nếu có lỗi, trả về trang form để người dùng sửa lại
            return "Staff/create"; // Bạn có thể trả về tên view của form nhập liệu
        }
        System.out.println("Du lieu khi them cua staff: " + staffRequest.toString());
        Staff staff = new Staff();
        staff.setCodeStaff(staffRequest.getCodeStaff());
        staff.setFullName(staffRequest.getFullName());
        staff.setAddress(staffRequest.getWard() + "," + staffRequest.getDistrict() + "," + staffRequest.getProvince() + "," + staffRequest.getAddRessDetail());
        staff.setNumberPhone(staffRequest.getNumberPhone());
        staff.setBirthDay(staffRequest.getBirthDay());
        staff.setEmail(staffRequest.getEmail());
        staff.setAcount("");
        staff.setPassword("");
        staff.setGender(staffRequest.getGender());
        staff.setRole(staffRequest.getRole());
        staff.setStatus(staffRequest.getStatus());
        Staff staffSave = this.staffService.save(staff);
        staffSave.setAcount(staffSave.getCodeStaff() + staffSave.getId());
        staff.setPassword("@shoponepoly");
        // Kiểm tra ảnh
        if (staffRequest.getNameImage() != null && !staffRequest.getNameImage().isEmpty()) {
            staff.setImage("fileName");
            staffService.uploadFile(staffRequest.getNameImage(), staffSave.getId());
        } else {
            // Đặt ảnh mặc định nếu không có ảnh được tải lên
            staff.setImage("Ảnh nhân viên");
        }
//        staff.setImage("fileName");
        System.out.println(staff.toString());
        System.out.println("Hello");
//        staffService.uploadFile(staffRequest.getNameImage(),staffSave.getId());
        return "redirect:/staff/list";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

    @GetMapping("/edit/{id}")
    public String editStaff(Model model, @PathVariable("id") Integer id) {
        Staff staff = staffService.getOne(id);
        StaffRequest staffRequest = new StaffRequest();
        staffRequest.setId(staff.getId());
        staffRequest.setCodeStaff(staff.getCodeStaff());
        staffRequest.setFullName(staff.getFullName());
        String[] part = staff.getAddress().split(",\\s*");
        staffRequest.setProvince(part[2]);
        staffRequest.setDistrict(part[1]);
        staffRequest.setWard(part[0]);
        staffRequest.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
        staffRequest.setGender(staff.getGender());
        staffRequest.setBirthDay(staff.getBirthDay());
        staffRequest.setNumberPhone(staff.getNumberPhone());
        staffRequest.setEmail(staff.getEmail());
        staffRequest.setRole(staff.getRole());
        staffRequest.setStatus(staff.getStatus());
        staffRequest.setImageString(staff.getImage());
        System.out.println(staffRequest.toString());
        model.addAttribute("staff", staffRequest);
        return "Staff/update";
    }

    @PostMapping("/update")
    public String updateStaff(Model model, @Valid @ModelAttribute(name = "staff") StaffRequest staffRequest, BindingResult result) throws IOException {
// Kiểm tra tên
        if (staffRequest.getFullName() == null || staffRequest.getFullName().trim().isEmpty()) {
            result.rejectValue("fullName", "error.staff", "Tên không được để trống!"); // Thông báo nếu tên rỗng hoặc chỉ chứa khoảng trắng
        } else if (staffRequest.getFullName().length() < 2 || staffRequest.getFullName().length() > 50) {
            result.rejectValue("fullName", "error.staff", "Tên phải có độ dài từ 2 đến 50 ký tự!");
        } else if (!staffRequest.getFullName().matches("^[\\p{L} ]+$")) {
            result.rejectValue("fullName", "error.staff", "Tên chỉ được chứa ký tự chữ cái và dấu cách!");
        }
// Kiểm tra số điện thoại
        if (staffRequest.getNumberPhone() == null || staffRequest.getNumberPhone().isEmpty()) {
            result.rejectValue("numberPhone", "error.staff", "Số điện thoại không được để trống!");
        } else if (!staffRequest.getNumberPhone().matches("^(0[3|5|7|8|9])+([0-9]{8})$")) {
            result.rejectValue("numberPhone", "error.staff", "Số điện thoại không hợp lệ!");
        }
        // Kiểm tra email
//        if (staffRequest.getEmail() == null || staffRequest.getEmail().isEmpty()) {
//            result.rejectValue("email", "error.staff", "Email không được để trống!");
//        } else if (customerRepository.existsByEmail(staffRequest.getEmail()) || staffRepository.existsByEmail(staffRequest.getEmail())) {
//            result.rejectValue("email", "error.staff", "Email đã được sử dụng!");
//        }
        // Kiểm tra ngày sinh
        if (staffRequest.getBirthDay() == null) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được để trống!");
        } else if (staffRequest.getBirthDay().isAfter(LocalDate.now())) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được lớn hơn ngày hiện tại!");
        }
        if (result.hasErrors()) {
            model.addAttribute("mes", "Thêm thất bại");
            // Nếu có lỗi, trả về trang form để người dùng sửa lại
            return "Staff/update"; // Bạn có thể trả về tên view của form nhập liệu
        }
        Staff staff = staffService.getStaffByID(staffRequest.getId());
        staffRequest.setCreateDate(staff.getCreateDate());
        staff.setCodeStaff(staffRequest.getCodeStaff());
        staff.setFullName(staffRequest.getFullName());
        staff.setAddress(staffRequest.getWard() + "," + staffRequest.getDistrict() + "," + staffRequest.getProvince() + "," + staffRequest.getAddRessDetail());
        staff.setNumberPhone(staffRequest.getNumberPhone());
        staff.setBirthDay(staffRequest.getBirthDay());
        staff.setEmail(staffRequest.getEmail());
        staff.setGender(staffRequest.getGender());
        staff.setRole(staffRequest.getRole());
        staff.setStatus(staffRequest.getStatus());
        this.staffService.save(staff);
        // Kiểm tra ảnh
        if (staffRequest.getNameImage() != null && !staffRequest.getNameImage().isEmpty()) {
            staff.setImage("fileName");
            staffService.uploadFile(staffRequest.getNameImage(), staff.getId());
        } else {
            // Đặt ảnh mặc định nếu không có ảnh được tải lên
            staff.setImage("Ảnh nhân viên");
        }
        return "redirect:/staff/list";
    }

    @GetMapping("/detail/{id}")
    public String detailStaff(Model model, @PathVariable("id") Integer id) {
        Staff staff = staffService.getOne(id);
        StaffRequest staffRequest = new StaffRequest();
        staffRequest.setId(staff.getId());
        staffRequest.setCodeStaff(staff.getCodeStaff());
        staffRequest.setFullName(staff.getFullName());
        String[] part = staff.getAddress().split(",\\s*");
        staffRequest.setProvince(part[2]);
        staffRequest.setDistrict(part[1]);
        staffRequest.setWard(part[0]);
        staffRequest.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
        staffRequest.setGender(staff.getGender());
        staffRequest.setBirthDay(staff.getBirthDay());
        staffRequest.setNumberPhone(staff.getNumberPhone());
        staffRequest.setEmail(staff.getEmail());
        staffRequest.setRole(staff.getRole());
        staffRequest.setStatus(staff.getStatus());
        staffRequest.setImageString(staff.getImage());
        System.out.println(staffRequest.toString());
        model.addAttribute("staff", staffRequest);
        return "Staff/detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(RedirectAttributes ra, @PathVariable("id") Integer id) {
        staffService.deleteStaff(id);
        ra.addFlashAttribute("mes", "Xóa thành công nhan vien với ID là: " + id);
        return "redirect:/staff/list";
    }

    @GetMapping("/exchange-pass-word/{id}")
    public String getExchangePassWord(@PathVariable("id") String id) {
        Staff staff = this.staffService.getStaffByID(Integer.parseInt(id));

        setUpToken(Integer.parseInt(id),"staff",staff.getEmail());
        return "redirect:/staff/list";
    }

}
