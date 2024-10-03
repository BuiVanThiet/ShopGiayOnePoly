package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.ClientSecurityResponsetory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/onepoly")
public class ClientController {
    @Autowired
    ClientSecurityResponsetory clientLoginResponse;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/home")
    public String getFormHomeClient(){
        return "client/homepage";
    }
    @GetMapping("/products")
    public String getFormProduct() {
        return "client/product";
    }
    @GetMapping("/address")
    public String getPriceByGHN() {
        return "client/address";
    }
    @GetMapping("/product_detail")
    public String testMenu(){
        return "client/product_detail";
    }
    @GetMapping("/cerateProduct")
    public String homeManage(Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            model.addAttribute("clientInfo", clientLoginResponse);
        } else {
            session.removeAttribute("clientInfo");
            return "redirect:/onepoly/login";
        }
        return "client/homepage";
    }
    @GetMapping("/login")
    public String getFormLoginClient(){
        return "login/loginClient";
    }
    @GetMapping("/logout")
    public String getLogoutClient(HttpSession session,Model model){
        session.removeAttribute("clientLogin");
        model.addAttribute("errorMessage", "");
        return "login/loginClient";
    }
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session, Model model) {
        // Lấy thông tin người dùng từ cơ sở dữ liệu
        ClientLoginResponse clientLoginResponse = this.clientLoginResponse.getCustomerByEmailAndAcount(username, username);

        // In thông tin ra để debug
        System.out.println(clientLoginResponse.toString());
        System.out.println(username + password);

        // So sánh mật khẩu người dùng nhập vào với mật khẩu trong cơ sở dữ liệu (không mã hóa lại)
        if (clientLoginResponse != null && passwordEncoder.matches(password,passwordEncoder.encode(clientLoginResponse.getPassword()))) {
            // Lưu thông tin vào session và chuyển hướng đến trang home
            session.setAttribute("clientLogin", clientLoginResponse);
            return "redirect:/onepoly/home";
        } else {
            model.addAttribute("usernameLogin", username);
            // Đăng nhập thất bại, chuyển hướng lại trang login với thông báo lỗi
            model.addAttribute("errorMessage", "Sai tên tài khoản hoặc mật khẩu");
            return "login/loginClient";
        }
    }

}
