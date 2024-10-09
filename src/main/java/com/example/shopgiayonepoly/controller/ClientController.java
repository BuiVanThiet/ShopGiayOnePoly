package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.implement.CustomerRegisterImplement;
import com.example.shopgiayonepoly.repositores.ClientSecurityResponsetory;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/onepoly")
public class ClientController {
    @Autowired
    ClientSecurityResponsetory clientLoginResponse;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerRegisterImplement customerRegisterImplement;
    @Autowired
    CustomerRegisterRepository customerRegisterRepository;
    @GetMapping("/home")
    public String getFormHomeClient(HttpSession session, Model model){
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            model.addAttribute("loginInfoClient", clientLoginResponse);
        } else {
            session.removeAttribute("clientInfo");
            return "redirect:/onepoly/login";
        }
        return "client/homepage";
    }
    @GetMapping("/base")
    public String getFormBaseClient(HttpSession session, Model model){
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            model.addAttribute("loginInfoClient", clientLoginResponse);
        } else {
            session.removeAttribute("clientInfo");
            return "redirect:/onepoly/login";
        }
        return "client/base";
    }
    @GetMapping("/products")
    public String getFormProduct(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            model.addAttribute("loginInfoClient", clientLoginResponse);
        } else {
            session.removeAttribute("clientInfo");
            return "redirect:/onepoly/login";
        }
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

        // In thông tin ra để debug 'nhập sai tk thì lỗi nên comment'
//        System.out.println(clientLoginResponse.toString());
//        System.out.println(username + password);

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

    @GetMapping("/register")
    public String formRegister(Model model, HttpSession session) {
        // Lấy dữ liệu từ session nếu có
        String acount = (String) session.getAttribute("acount");
        String email = (String) session.getAttribute("email");

        // Đưa dữ liệu từ session vào model
        model.addAttribute("acount", acount != null ? acount : "");
        model.addAttribute("email", email != null ? email : "");
        model.addAttribute("errorMessage", session.getAttribute("errorMessage"));

        // Xóa session sau khi dùng xong
        session.removeAttribute("acount");
        session.removeAttribute("email");
        session.removeAttribute("errorMessage");

        return "client/register";
    }


    @PostMapping("/register")
    public String register(RegisterRequest registerRequest, Model model, HttpSession session) {
        // Lưu thông tin vào session để giữ lại khi có lỗi
        session.setAttribute("acount", registerRequest.getAcount());
        session.setAttribute("email", registerRequest.getEmail());

        // Kiểm tra email đã tồn tại
        if (customerRegisterRepository.existsByEmail(registerRequest.getEmail())) {
            session.setAttribute("errorMessage", "Email đã tồn tại. Vui lòng chọn email khác.");
            return "redirect:/onepoly/register";  // Chuyển hướng lại trang đăng ký
        }
        if (customerRegisterRepository.existsByAcount(registerRequest.getAcount())) {
            session.setAttribute("errorMessage", "Tên đăng nhập đã tồn tại. Vui lòng chọn Tên đăng nhập khác.");
            return "redirect:/onepoly/register";  // Chuyển hướng lại trang đăng ký
        }

        // Xử lý đăng ký thành công
        Customer customer = new Customer();
        customer.setAcount(registerRequest.getAcount());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(registerRequest.getPassword());

        // Gán các giá trị mặc định cho các trường khác
        customer.setFullName(" ");
        customer.setNumberPhone(" ");
        customer.setGender(1);
        customer.setStatus(1);

        String message = customerRegisterImplement.registerCustomer(customer);

        // Kiểm tra thông điệp phản hồi từ dịch vụ
        if (message.equals("Đăng ký thành công!")) {
            session.setAttribute("successMessage", message);
            session.removeAttribute("acount");
            session.removeAttribute("email");
            session.removeAttribute("errorMessage");
            return "redirect:/onepoly/login";  // Chuyển hướng đến trang đăng nhập
        } else {
            session.setAttribute("errorMessage", message);
            return "redirect:/onepoly/register";  // Trả về trang đăng ký với thông báo lỗi
        }
    }
    @GetMapping("/userProfile")
    public String formProfile( Model model, HttpSession session){
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            model.addAttribute("clientInfo", clientLoginResponse);
        } else {
            session.removeAttribute("clientInfo");
            return "redirect:/onepoly/login";
        }
        return "client/UserProfile";
    }
}
