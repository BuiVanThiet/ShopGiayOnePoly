package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.CustomerRequest;
import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
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
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    private final int pageSize = 4;

//    @GetMapping("/list")
//    public String getFormList(Model model) {
//        List<CustomerResponse> listCustomer = customerService.getAllCustomer();
//        model.addAttribute("customerList", customerService.getAllCustomer());
//        return "Customer/list";
//    }

    @GetMapping("/list")
    public String getListCustomrByPage(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,Model model) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Customer> pageCustomer = customerService.getAllCustomerByPage(pageable);
        model.addAttribute("pageCustomer", pageCustomer);
        model.addAttribute("customer", new CustomerRequest());
        return "Customer/list";
    }

    @GetMapping("/search")
    public String searchCustomerByKey(@RequestParam(name = "key") String key, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber, Model model) {
        Pageable pageableSearch = PageRequest.of(pageNumber, pageSize);
        Page<Customer> pageCustomer = customerService.searchCustomerByKeywordPage(key, pageableSearch);
//        model.addAttribute("customerList", searchCustomer);
        model.addAttribute("pageCustomer", pageCustomer);
        model.addAttribute("customer", new CustomerRequest());
        return "Customer/list";
    }

    @GetMapping("/create")
    public String createCustomer(ModelMap modelMap){
        modelMap.addAttribute("customer",new CustomerRequest());
        return "Customer/create";
    }

    @PostMapping("/add")
    public String addCustomer(Model model, @Valid @ModelAttribute(name="customer") CustomerRequest customerRequest, BindingResult result) throws IOException {
        // Kiểm tra tên
        if (customerRequest.getFullName() == null || customerRequest.getFullName().trim().isEmpty()) {
            result.rejectValue("fullName", "error.customer", "Tên không được để trống!"); // Thông báo nếu tên rỗng hoặc chỉ chứa khoảng trắng
        } else if (customerRequest.getFullName().length() < 2 || customerRequest.getFullName().length() > 50) {
            result.rejectValue("fullName", "error.customer", "Tên phải có độ dài từ 2 đến 50 ký tự!");
        } else if (!customerRequest.getFullName().matches("^[\\p{L} ]+$")) {
            result.rejectValue("fullName", "error.customer", "Tên chỉ được chứa ký tự chữ cái và dấu cách!");
        }
        // Kiểm tra số điện thoại
        if (customerRequest.getNumberPhone() == null || customerRequest.getNumberPhone().isEmpty()) {
            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không được để trống!");
        } else if (!customerRequest.getNumberPhone().matches("^(0[3|5|7|8|9])+([0-9]{8})$")) {
            result.rejectValue("numberPhone", "error.customer", "Số điện thoại không hợp lệ!");
        }
        if (result.hasErrors()) {
            model.addAttribute("mes", "Thêm thất bại");
            // Nếu có lỗi, trả về trang form để người dùng sửa lại
            return "Customer/create"; // Bạn có thể trả về tên view của form nhập liệu
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
        customer.setAddRess(customerRequest.getWard() + "," + customerRequest.getDistrict() + "," + customerRequest.getProvince() + "," +customerRequest.getAddRessDetail());
        customer.setStatus(customerRequest.getStatus());
        Customer customerSave = this.customerService.save(customer);
        customerSave.setAcount(customerSave.getFullName()+customerSave.getId());
        customer.setPassword("@shoponepoly");
//        this.customerService.save(customerSave);
        customer.setImage("fileName");
        System.out.println(customer.toString());
        customerService.uploadFile(customerRequest.getNameImage(),customerSave.getId());
        return "redirect:/customer/list";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(Model model, @PathVariable("id") Integer id) {
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

    @GetMapping("/detail/{id}")
    public String detailCustomer(Model model, @PathVariable("id") Integer id) {
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
    public String deleteCustomer(RedirectAttributes ra, @PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
        ra.addFlashAttribute("mes", "Xóa thành công Khach hang với ID là: " + id);
        return "redirect:/customer/list";
    }
}
