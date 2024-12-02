package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.baseMethod.BaseEmail;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Token;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import com.example.shopgiayonepoly.service.CustomerService;
import com.example.shopgiayonepoly.service.StaffService;
import com.example.shopgiayonepoly.service.TokenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping ("/forgotPassword")
public class ForgotPasswordController extends BaseEmail {
    @Autowired
    CustomerRegisterRepository customerRegisterRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    StaffService staffService;

    @GetMapping("/form")
    public String formForgotPassword(ModelMap modelMap){
        modelMap.addAttribute("check",false);
        return "client/forgotPassword";
    }
    @PostMapping("/request")
    public String getRequestEmail(@RequestParam(value = "username",required = false) String email, ModelMap modelMap) {
        System.out.println("thong tin email " + email);
        modelMap.addAttribute("check",false);
        if(email == null) {
            modelMap.addAttribute("mess","Mời nhập email!");
            return "client/forgotPassword";
        }else {
            if(email.trim().equals("")) {
                modelMap.addAttribute("mess","Mời nhập email!");
                return "client/forgotPassword";
            }else {
                Customer customer = this.customerRegisterRepository.getCustomerByEmail(email);
                if (customer == null || customer.getId() == null) {
                    modelMap.addAttribute("mess","Email này không tồn tại!");
                    return "client/forgotPassword";
                }else {
                    modelMap.addAttribute("check",true);
                    modelMap.addAttribute("mess","Đường dẫn đổi mật khẩu đã được gửi, mời bạn kiểm tra email!");
                    setUpToken(customer.getId(),"customer",email);
                    return "client/forgotPassword";
                }
            }
        }
    }

    @GetMapping("/changepassword/{id}")
    public String changePassword(@PathVariable("id") String id, HttpSession session,ModelMap modelMap) {
        try {
            Integer.parseInt(id);
        }catch (NumberFormatException e) {
            e.printStackTrace();
            return "errorTemplate/404";
        }
//        return "client/exchangePassword";

        Token token = this.tokenService.findById(Integer.parseInt(id)).orElse(null);
        if (token == null || token.getStatus() == 0) {
            return "errorTemplate/404";
        }else {
            long timeCreated = token.getCreateDate().getTime();
            long currentTime = System.currentTimeMillis();
            if (currentTime - timeCreated > 10 * 60 * 1000) { // 10 phút
                return "errorTemplate/404"; // Trả về trang 403 nếu token đã hết hạn
            }else {
                if(token.getNameTable().trim().equals("customer")) {
                    Customer customer = this.customerService.getCustomerByID(token.getNameTable().trim().equals("customer") ? token.getIdAccount() : -1);
                    if(customer == null) {
                        return "errorTemplate/404";
                    }else {
                        session.setAttribute("idCustomerChangerPassword",customer.getId());
                        modelMap.addAttribute("action","/forgotPassword/exchange-password/"+token.getId());
                        return "client/exchangePassword";
                    }
                }else {
                    Staff staff = this.staffService.getStaffByID(token.getNameTable().trim().equals("staff") ? token.getIdAccount() : -1);
                    if(staff == null) {
                        return "errorTemplate/404";
                    }else {
                        session.setAttribute("idStaffChangerPassword",staff.getId());
                        modelMap.addAttribute("action","/forgotPassword/exchange-password/"+token.getId());
                        return "Home/exhcnagePassWordStaff";
                    }
                }
            }
        }
    }

    @PostMapping("/exchange-password/{idToken}")
    public String getExchangePassWord(
            @PathVariable("idToken") String idToken,
            @RequestParam(value = "passwordOrder1",required = false) String passwordOrder1,
            @RequestParam(value = "passwordOrder2",required = false) String passwordOrder2,
            HttpSession session,
            ModelMap modelMap
    ) {
        try {
            Integer.parseInt(idToken);
        }catch (NumberFormatException e) {
            e.printStackTrace();
            return "errorTemplate/404";
        }

        Token token = this.tokenService.findById(Integer.parseInt(idToken)).orElse(null);
        modelMap.addAttribute("passwordOrder1",passwordOrder1);
        modelMap.addAttribute("passwordOrder2",passwordOrder2);

        if (token == null || token.getStatus() == 0) {
            return "errorTemplate/404";
        }else {

            long timeCreated = token.getCreateDate().getTime();
            long currentTime = System.currentTimeMillis();
            if (currentTime - timeCreated > 10 * 60 * 1000) { // 10 phút
                return "errorTemplate/404"; // Trả về trang 403 nếu token đã hết hạn
            }else {
                if(token.getNameTable().trim().equals("customer")) {
                    Customer customer = this.customerService.getCustomerByID(token.getNameTable().trim().equals("customer") ? token.getIdAccount() : -1);
                    if(customer == null) {
                        return "errorTemplate/404";
                    }else {
                        if (session.getAttribute("idCustomerChangerPassword") == null) {
                            return "errorTemplate/404";
                        }

                        if (customer == null || customer.getId() == null) {
                            return "errorTemplate/404";
                        }

                        if(passwordOrder1 == null) {
                            modelMap.addAttribute("messPassWord1","Mời nhập mật khẩu cần đổi!");
                            modelMap.addAttribute("messPassWord2","");
                            return "client/exchangePassword";
                        }else {
                            if(passwordOrder1.trim().equals("") || (passwordOrder1.trim().length() < 1 && passwordOrder1.trim().length() > 255)) {
                                modelMap.addAttribute("messPassWord1","Mật khẩu phải từ 1 đến 255 ký tự!");
                                modelMap.addAttribute("messPassWord2","");
                                return "client/exchangePassword";
                            }else {
                                if(!passwordOrder2.trim().equals(passwordOrder1)) {
                                    modelMap.addAttribute("messPassWord1","");
                                    modelMap.addAttribute("messPassWord2","Xác nhận mật khẩu phải giống mới mật khẩu mới!");
                                    return "client/exchangePassword";
                                }else {
                                    modelMap.addAttribute("messPassWord1","");
                                    modelMap.addAttribute("messPassWord2","");
                                    modelMap.addAttribute("passwordOrder1","");
                                    modelMap.addAttribute("passwordOrder2","");
                                    customer.setPassword(passwordOrder1);
                                    customer.setUpdateDate(new Date());
                                    token.setStatus(0);
                                    token.setUpdateDate(new Date());
                                    this.tokenService.saveOrUpdate(token);
                                    this.customerService.save(customer);
                                    return "redirect:/onepoly/login";
                                }
                            }
                        }
                    }
                }else {
                    Staff staff = this.staffService.getStaffByID(token.getNameTable().trim().equals("staff") ? token.getIdAccount() : -1);
                    if (staff == null) {
                        return "errorTemplate/404";
                    }else {
                        if (session.getAttribute("idStaffChangerPassword") == null) {
                            return "errorTemplate/404";
                        }

                        if (staff == null || staff.getId() == null) {
                            return "errorTemplate/404";
                        }
                        if(passwordOrder1 == null) {
                            modelMap.addAttribute("messPassWord1","Mời nhập mật khẩu cần đổi!");
                            modelMap.addAttribute("messPassWord2","");
                            return "Home/exhcnagePassWordStaff";
                        }else {
                            if(passwordOrder1.trim().equals("") || (passwordOrder1.trim().length() < 1 && passwordOrder1.trim().length() > 255)) {
                                modelMap.addAttribute("messPassWord1","Mật khẩu phải từ 1 đến 255 ký tự!");
                                modelMap.addAttribute("messPassWord2","");
                                return "Home/exhcnagePassWordStaff";
                            }else {
                                if(!passwordOrder2.trim().equals(passwordOrder1)) {
                                    modelMap.addAttribute("messPassWord1","");
                                    modelMap.addAttribute("messPassWord2","Xác nhận mật khẩu phải giống mới mật khẩu mới!");
                                    return "Home/exhcnagePassWordStaff";
                                }else {
                                    modelMap.addAttribute("messPassWord1","");
                                    modelMap.addAttribute("messPassWord2","");
                                    modelMap.addAttribute("passwordOrder1","");
                                    modelMap.addAttribute("passwordOrder2","");
                                    staff.setPassword(passwordOrder1);
                                    staff.setUpdateDate(new Date());
                                    token.setStatus(0);
                                    token.setUpdateDate(new Date());
                                    this.tokenService.saveOrUpdate(token);
                                    this.staffService.save(staff);
                                    return "redirect:/login";
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
