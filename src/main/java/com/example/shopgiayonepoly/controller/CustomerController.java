package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.CustomerRequest;
import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.CustomerRepository;
import com.example.shopgiayonepoly.repositores.StaffRepository;
import com.example.shopgiayonepoly.service.CustomerService;
import com.example.shopgiayonepoly.service.StaffService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    StaffService staffService;

    @Autowired
    CustomerRepository customerRepository;

    String mess = "";
    String check = "";

    private final int pageSize = 5;

//    @GetMapping("/list")
//    public String getFormList(Model model) {
//        List<CustomerResponse> listCustomer = customerService.getAllCustomer();
//        model.addAttribute("customerList", customerService.getAllCustomer());
//        return "Customer/list";
//    }

    @GetMapping("/list")
    public String getListCustomrByPage(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber, Model model, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if (staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CustomerResponse> pageCustomer = customerService.getAllCustomerByPage(pageable);
        model.addAttribute("pageCustomer", pageCustomer);
        model.addAttribute("customer", new CustomerRequest());
        model.addAttribute("message", mess);
        model.addAttribute("check", check);
        mess = "";
        check = "";
        return "Customer/list";
    }

    @GetMapping("/search")
    public String searchCustomerByKey(@RequestParam(name = "key") String key, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber, Model model) {
        String trimmedKey = key != null ? key.trim() : null;
        Pageable pageableSearch = PageRequest.of(pageNumber, pageSize);
        Page<CustomerResponse> pageCustomer = customerService.searchCustomerByKeywordPage(trimmedKey, pageableSearch);
//        model.addAttribute("customerList", searchCustomer);
        model.addAttribute("pageCustomer", pageCustomer);
        model.addAttribute("customer", new CustomerRequest());
        return "Customer/list";
    }

    @GetMapping("/create")
    public String createCustomer(ModelMap modelMap) {
        modelMap.addAttribute("customer", new CustomerRequest());
        return "Customer/create";
    }

    @PostMapping("/add")
    public String addCustomer(Model model, @Valid @ModelAttribute(name = "customer") CustomerRequest customerRequest, BindingResult result, HttpSession session) throws IOException {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if (staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
//        // Kiểm tra tên
//        if (customerRequest.getFullName() == null || customerRequest.getFullName().trim().isEmpty()) {
//            result.rejectValue("fullName", "error.customer", "Tên không được để trống!"); // Thông báo nếu tên rỗng hoặc chỉ chứa khoảng trắng
//        } else if (customerRequest.getFullName().length() < 2 || customerRequest.getFullName().length() > 50) {
//            result.rejectValue("fullName", "error.customer", "Tên phải có độ dài từ 2 đến 50 ký tự!");
//        } else if (!customerRequest.getFullName().matches("^[\\p{L} ]+$")) {
//            result.rejectValue("fullName", "error.customer", "Tên chỉ được chứa ký tự chữ cái và dấu cách!");
//        }
//        // Kiểm tra số điện thoại
//        if (customerRequest.getNumberPhone() == null || customerRequest.getNumberPhone().isEmpty()) {
//            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không được để trống!");
//        } else if (!customerRequest.getNumberPhone().matches("^(0[3|5|7|8|9])+([0-9]{8})$")) {
//            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không hợp lệ!");
//        }
//        // Kiểm tra email
//        if (customerRequest.getEmail() == null || customerRequest.getEmail().isEmpty()) {
//            result.rejectValue("email", "error.customer", "Email không được để trống!");
//        } else if (customerService.existsByEmail(customerRequest.getEmail()) != null || staffService.existsByEmail(customerRequest.getEmail()) != null) {
////            if (customerService.existsByEmail(customerRequest.getEmail()).getId() != customerRequest.getId()) {
//                result.rejectValue("email", "error.customer", "Email đã được sử dụng!");
////            }
//        }
//        // Kiểm tra ngày sinh
//        if (customerRequest.getBirthDay() == null) {
//            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được để trống!");
//        } else if (customerRequest.getBirthDay().isAfter(LocalDate.now())) {
//            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được lớn hơn ngày hiện tại!");
//        }
//        if (result.hasErrors()) {
//            model.addAttribute("mes", "Thêm thất bại");
//            // Nếu có lỗi, trả về trang form để người dùng sửa lại
//            return "Customer/create"; // Bạn có thể trả về tên view của form nhập liệu
//        }
        // Kiểm tra tên khách hàng
        if (customerRequest.getFullName() == null || customerRequest.getFullName().trim().isEmpty()) {
            result.rejectValue("fullName", "error.customer", "Tên không được để trống!");
        } else if (customerRequest.getFullName().length() > 255) {
            result.rejectValue("fullName", "error.customer", "Tên không được vượt quá 255 ký tự!");
        } else if (!customerRequest.getFullName().matches("^[\\p{L} ]+$")) {
            result.rejectValue("fullName", "error.customer", "Tên chỉ được chứa ký tự chữ cái và dấu cách!");
        }

// Kiểm tra số điện thoại
        if (customerRequest.getNumberPhone() == null || customerRequest.getNumberPhone().trim().isEmpty()) {
            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không được để trống!");
        } else if (!customerRequest.getNumberPhone().matches("^(0[3|5|7|8|9])[0-9]{8}$")) {
            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không hợp lệ!");
        }

// Kiểm tra email
        if (customerRequest.getEmail() == null || customerRequest.getEmail().trim().isEmpty()) {
            result.rejectValue("email", "error.customer", "Email không được để trống!");
        } else if (customerRequest.getEmail().length() > 100) {
            result.rejectValue("email", "error.customer", "Email không được vượt quá 100 ký tự!");
        } else if (!customerRequest.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            result.rejectValue("email", "error.customer", "Email không hợp lệ!");
        } else if (customerService.existsByEmail(customerRequest.getEmail()) != null
                || staffService.existsByEmail(customerRequest.getEmail()) != null) {
            result.rejectValue("email", "error.customer", "Email đã được sử dụng!");
        }

// Kiểm tra địa chỉ
        if (customerRequest.getAddRessDetail() == null || customerRequest.getAddRessDetail().trim().isEmpty()) {
            result.rejectValue("addRessDetail", "error.customer", "Địa chỉ cụ thể không được để trống!");
        } else if (customerRequest.getAddRessDetail().length() > 260) {
            result.rejectValue("addRessDetail", "error.customer", "Địa chỉ cụ thể không được vượt quá 260 ký tự!");
        }

// Kiểm tra ngày sinh
        if (customerRequest.getBirthDay() == null) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được để trống!");
        } else if (customerRequest.getBirthDay().isAfter(LocalDate.now())) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được lớn hơn ngày hiện tại!");
        } else if (Period.between(customerRequest.getBirthDay(), LocalDate.now()).getYears() < 15) {
            result.rejectValue("birthDay", "error.customer", "Khách hàng phải đủ 15 tuổi trở lên!");
        }

// Kiểm tra lỗi và trả về thông báo
        if (result.hasErrors()) {
            model.addAttribute("mes", "Thêm thất bại");
            return "Customer/create"; // Trả về view để người dùng chỉnh sửa
        }

        System.out.println("Du lieu khi them cua customer: " + customerRequest.toString());
        Customer customer = new Customer();
        customer.setFullName(customerRequest.getFullName());
        customer.setNumberPhone(customerRequest.getNumberPhone());
        customer.setBirthDay(customerRequest.getBirthDay());
//        customer.setImage(customerRequest.getNameImage());
        customer.setEmail(customerRequest.getEmail());
        customer.setAcount("");
        customer.setPassword("");
        customer.setGender(customerRequest.getGender());
        customer.setAddRess(customerRequest.getWard() + "," + customerRequest.getDistrict() + "," + customerRequest.getProvince() + "," + customerRequest.getAddRessDetail());
        customer.setStatus(customerRequest.getStatus());
        Customer customerSave = this.customerService.save(customer);
// Kiểm tra ảnh
        if (customerRequest.getNameImage() != null && !customerRequest.getNameImage().isEmpty()) {
            customer.setImage("fileName");
            customerService.uploadFile(customerRequest.getNameImage(), customerSave.getId());
        } else {
            // Đặt ảnh mặc định nếu không có ảnh được tải lên
            customer.setImage("Ảnh khách hàng");
        }
        //        this.customerService.save(customerSave);
//        customer.setImage("fileName");
        System.out.println(customer.toString());
        mess = "Thêm khách hàng thành công";
        check = "1";
//        customerService.uploadFile(customerRequest.getNameImage(),customerSave.getId());
        return "redirect:/customer/list";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(Model model, @PathVariable("id") Integer id, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if (staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        Customer customer = customerService.getOne(id);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setId(customer.getId());
        customerRequest.setFullName(customer.getFullName());
        customerRequest.setGender(customer.getGender());
        customerRequest.setStatus(customer.getStatus());
        customerRequest.setBirthDay(customer.getBirthDay());
        customerRequest.setNumberPhone(customer.getNumberPhone());
        customerRequest.setEmail(customer.getEmail());
        String[] part = customer.getAddRess().split(",\\s*");
        customerRequest.setProvince(part[2]);
        customerRequest.setDistrict(part[1]);
        customerRequest.setWard(part[0]);
        customerRequest.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
        customerRequest.setImageString(customer.getImage());
        System.out.println(customerRequest.toString());
        model.addAttribute("customer", customerRequest);
        return "Customer/update";
    }

    @PostMapping("/update")
    public String updateCustomer(Model model, @Valid @ModelAttribute(name = "customer") CustomerRequest customerRequest, BindingResult result, HttpSession session) throws IOException {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if (staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
//        // Kiểm tra tên
//        if (customerRequest.getFullName() == null || customerRequest.getFullName().trim().isEmpty()) {
//            result.rejectValue("fullName", "error.customer", "Tên không được để trống!"); // Thông báo nếu tên rỗng hoặc chỉ chứa khoảng trắng
//        } else if (customerRequest.getFullName().length() < 2 || customerRequest.getFullName().length() > 50) {
//            result.rejectValue("fullName", "error.customer", "Tên phải có độ dài từ 2 đến 50 ký tự!");
//        } else if (!customerRequest.getFullName().matches("^[\\p{L} ]+$")) {
//            result.rejectValue("fullName", "error.customer", "Tên chỉ được chứa ký tự chữ cái và dấu cách!");
//        }
//        // Kiểm tra số điện thoại
//        if (customerRequest.getNumberPhone() == null || customerRequest.getNumberPhone().isEmpty()) {
//            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không được để trống!");
//        } else if (!customerRequest.getNumberPhone().matches("^(0[3|5|7|8|9])+([0-9]{8})$")) {
//            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không hợp lệ!");
//        }
//        // Kiểm tra email
//        if (customerRequest.getEmail() == null || customerRequest.getEmail().isEmpty()) {
//            result.rejectValue("email", "error.customer", "Email không được để trống!");
//        } else {
//            Customer existingCustomer = customerService.existsByEmail(customerRequest.getEmail());
//            Staff existingStaff = staffService.existsByEmail(customerRequest.getEmail());
//            System.out.println(existingCustomer == null ? "Dell co(khach)" : "co(khach)");
//            System.out.println(existingStaff == null ? "Dell co(nhanvien)" : "co(nhanvien)");
//            if (existingCustomer != null && !existingCustomer.getId().equals(customerRequest.getId())) {
//                result.rejectValue("email", "error.customer", "Email đã được sử dụng!");
//            } else if (staffService.existsByEmail(customerRequest.getEmail()) != null) {
//                result.rejectValue("email", "error.customer", "Email đã được sử dụng trong hệ thống nhân viên!");
//            }
//        }
//        // Kiểm tra ngày sinh
//        if (customerRequest.getBirthDay() == null) {
//            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được để trống!");
//        } else if (customerRequest.getBirthDay().isAfter(LocalDate.now())) {
//            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được lớn hơn ngày hiện tại!");
//        }
        // Kiểm tra tên khách hàng
        if (customerRequest.getFullName() == null || customerRequest.getFullName().trim().isEmpty()) {
            result.rejectValue("fullName", "error.customer", "Tên không được để trống!");
        } else if (customerRequest.getFullName().length() > 255) {
            result.rejectValue("fullName", "error.customer", "Tên không được vượt quá 255 ký tự!");
        } else if (!customerRequest.getFullName().matches("^[\\p{L} ]+$")) {
            result.rejectValue("fullName", "error.customer", "Tên chỉ được chứa ký tự chữ cái và dấu cách!");
        }

// Kiểm tra số điện thoại
        if (customerRequest.getNumberPhone() == null || customerRequest.getNumberPhone().trim().isEmpty()) {
            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không được để trống!");
        } else if (!customerRequest.getNumberPhone().matches("^(0[3|5|7|8|9])[0-9]{8}$")) {
            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không hợp lệ!");
        }

// Kiểm tra email
        if (customerRequest.getEmail() == null || customerRequest.getEmail().isEmpty()) {
            result.rejectValue("email", "error.customer", "Email không được để trống!");
        } else if (customerRequest.getEmail().length() > 100) {
            result.rejectValue("email", "error.customer", "Email không được vượt quá 100 ký tự!");
        } else if (!customerRequest.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            result.rejectValue("email", "error.customer", "Email không hợp lệ!");
        } else{
            Customer existingCustomer = customerService.existsByEmail(customerRequest.getEmail());
            Staff existingStaff = staffService.existsByEmail(customerRequest.getEmail());
            System.out.println(existingCustomer == null ? "Dell co(khach)" : "co(khach)");
            System.out.println(existingStaff == null ? "Dell co(nhanvien)" : "co(nhanvien)");
            if (existingCustomer != null && !existingCustomer.getId().equals(customerRequest.getId())) {
                result.rejectValue("email", "error.customer", "Email đã được sử dụng!");
            } else if (staffService.existsByEmail(customerRequest.getEmail()) != null) {
                result.rejectValue("email", "error.customer", "Email đã được sử dụng trong hệ thống nhân viên!");
            }
        }

// Kiểm tra địa chỉ
        if (customerRequest.getAddRessDetail() == null || customerRequest.getAddRessDetail().trim().isEmpty()) {
            result.rejectValue("addRessDetail", "error.customer", "Địa chỉ cụ thể không được để trống!");
        } else if (customerRequest.getAddRessDetail().length() > 260) {
            result.rejectValue("addRessDetail", "error.customer", "Địa chỉ cụ thể không được vượt quá 260 ký tự!");
        }

// Kiểm tra ngày sinh
        if (customerRequest.getBirthDay() == null) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được để trống!");
        } else if (customerRequest.getBirthDay().isAfter(LocalDate.now())) {
            result.rejectValue("birthDay", "error.customer", "Ngày sinh không được lớn hơn ngày hiện tại!");
        } else if (Period.between(customerRequest.getBirthDay(), LocalDate.now()).getYears() < 15) {
            result.rejectValue("birthDay", "error.customer", "Khách hàng phải đủ 15 tuổi trở lên!");
        }
        if (result.hasErrors()) {
            model.addAttribute("mes", "Thêm thất bại");
            return "Customer/update";
        }
        Customer customer = customerService.getCustomerByID(customerRequest.getId());
        customerRequest.setCreateDate(customer.getCreateDate());
        customer.setFullName(customerRequest.getFullName());
        customer.setNumberPhone(customerRequest.getNumberPhone());
        customer.setBirthDay(customerRequest.getBirthDay());
//        customer.setImage(customerRequest.getNameImage());
        customer.setEmail(customerRequest.getEmail());
        customer.setAcount("");
        customer.setPassword("");
        customer.setGender(customerRequest.getGender());
        customer.setAddRess(customerRequest.getWard() + "," + customerRequest.getDistrict() + "," + customerRequest.getProvince() + "," + customerRequest.getAddRessDetail());
        customer.setStatus(customerRequest.getStatus());
        this.customerService.save(customer);
        // Kiểm tra và cập nhật ảnh
        if (customerRequest.getNameImage() != null && !customerRequest.getNameImage().isEmpty()) {
            customer.setImage("fileName");
            customerService.uploadFile(customerRequest.getNameImage(), customer.getId());
        } else {
            // Đặt ảnh mặc định nếu không có ảnh được tải lên
            customer.setImage("Ảnh khách hàng");
        }
        mess = "Sửa khách hàng thành công";
        check = "1";
        return "redirect:/customer/list";
    }

    @GetMapping("/detail/{id}")
    public String detailCustomer(Model model, @PathVariable("id") Integer id, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if (staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        Customer customer = customerService.getOne(id);
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setId(customer.getId());
        customerRequest.setFullName(customer.getFullName());
        customerRequest.setGender(customer.getGender());
        customerRequest.setStatus(customer.getStatus());
        customerRequest.setBirthDay(customer.getBirthDay());
        customerRequest.setNumberPhone(customer.getNumberPhone());
        customerRequest.setEmail(customer.getEmail());
        String[] part = customer.getAddRess().split(",\\s*");
        customerRequest.setProvince(part[2]);
        customerRequest.setDistrict(part[1]);
        customerRequest.setWard(part[0]);
        customerRequest.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
        customerRequest.setImageString(customer.getImage());
        System.out.println(customerRequest.toString());
        model.addAttribute("customer", customerRequest);
        return "Customer/detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(RedirectAttributes ra, @PathVariable("id") Integer id, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return "redirect:/login";
        }
        if (staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        customerService.deleteCustomer(id);
        ra.addFlashAttribute("mes", "Xóa thành công Khach hang với ID là: " + id);
        return "redirect:/customer/list";
    }
}
