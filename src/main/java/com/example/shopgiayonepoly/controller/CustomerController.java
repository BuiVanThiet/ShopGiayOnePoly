package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.CustomerRequest;
import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CustomerService;
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
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    private final int pageSize = 1;

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
    public String searchCustomerByKey(@RequestParam(name = "key") String key, Model model) {
        List<CustomerResponse> searchCustomer = customerService.searchCustomerByKeyword(key);
        model.addAttribute("customerList", searchCustomer);
        model.addAttribute("customer", new CustomerRequest());
        return "customer/list";
    }

    @GetMapping("/create")
    public String createCustomer(ModelMap modelMap){
        modelMap.addAttribute("customer",new CustomerRequest());
        return "Customer/create";
    }

    @PostMapping("/add")
    public String addCustomer(Model model, @ModelAttribute(name="customer") CustomerRequest customerRequest){
        System.out.println("Du lieu khi them cua customer: " + customerRequest.toString());
        Customer customer = new Customer();
        customer.setFullName(customerRequest.getFullName());
        customer.setNumberPhone(customerRequest.getNumberPhone());
        customer.setBirthDay(customerRequest.getBirthDay());
        customer.setImage(customerRequest.getNameImage());
        customer.setEmail(customerRequest.getEmail());
        customer.setAcount("");
        customer.setPassword("");
        customer.setGender(customerRequest.getGender());
        customer.setAddRess(customerRequest.getWard() + "," + customerRequest.getDistrict() + "," + customerRequest.getProvince() + "," +customerRequest.getAddRessDetail());
        customer.setStatus(customerRequest.getStatus());
        Customer customerSave = this.customerService.save(customer);
        customerSave.setAcount(customerSave.getFullName()+customerSave.getId());
        customer.setPassword("@shoponepoly");
        this.customerService.save(customerSave);
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
        BeanUtils.copyProperties(customer, customerRequest);
//        // Xử lý địa chỉ
//        String getAddressDetail = customerRequest.getAddRessDetail();
//
//        if (getAddressDetail != null && !getAddressDetail.isEmpty()) {
//            String[] parts = getAddressDetail.split(",\\s*"); // Cắt chuỗi dựa trên dấu phẩy và khoảng trắng
//
//            // Kiểm tra xem chuỗi có đủ phần không
//            if (parts.length >= 3) {
//                customerRequest.setWard(parts[0]); // Xã/Phường
//                customerRequest.setDistrict(parts[1]); // Quận/Huyện
//                customerRequest.setProvince(parts[2]); // Tỉnh/Thành phố
//
//                // Nếu địa chỉ còn các phần khác, gộp lại
//                if (parts.length > 3) {
//                    customerRequest.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(parts, 3, parts.length)));
//                } else {
//                    customerRequest.setAddRessDetail(""); // Không còn chi tiết địa chỉ khác
//                }
//            }
//        }
        model.addAttribute("customer", customerRequest);
        return "Customer/update";
    }

    @GetMapping("/detail/{id}")
    public String detailCustomer(Model model, @PathVariable("id") Integer id) {
        Customer customer = customerService.getOne(id);
        CustomerRequest customerRequest = new CustomerRequest();
        BeanUtils.copyProperties(customer, customerRequest);
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
