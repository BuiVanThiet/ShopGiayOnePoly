package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.dto.request.UserProfileUpdateRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.implement.CustomerRegisterImplement;
import com.example.shopgiayonepoly.repositores.ClientSecurityResponsetory;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import com.example.shopgiayonepoly.repositores.CustomerRepository;
import com.example.shopgiayonepoly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    @Autowired
    CustomerService customerService;
    @GetMapping("/home")
    public String getFormHomeClient(HttpSession session, Model model){
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
//        if (clientLoginResponse != null) {
            model.addAttribute("clientLogin", clientLoginResponse);
//        } else {
            session.removeAttribute("clientInfo");
//            return "redirect:/onepoly/login";
//        }
        return "client/homepage";
    }
    @GetMapping("/base")
    public String getFormBaseClient(HttpSession session, Model model){
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
            model.addAttribute("clientLogin", clientLoginResponse);
        return "client/base";
    }
    @GetMapping("/products")
    public String getFormProduct(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
//        if (clientLoginResponse != null) {
            model.addAttribute("loginInfoClient", clientLoginResponse);
//        } else {
//            session.removeAttribute("clientInfo");
//            return "redirect:/onepoly/login";
//        }
        return "client/product";
    }
    @GetMapping("/address")
    public String getPriceByGHN(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        return "client/address";
    }
    @GetMapping("/product_detail")
    public String testMenu(HttpSession session, Model model){
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
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
    public String formProfile(Model model, HttpSession session) {
        // Lấy thông tin đăng nhập từ session
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        // Kiểm tra nếu người dùng đã đăng nhập
        if (clientLoginResponse != null) {
            // Lấy thông tin acount từ session
            String acount = clientLoginResponse.getAcount();
            // Tìm thông tin khách hàng dựa trên acount
            Customer customer = customerRegisterRepository.findByAcount(acount);
            // Kiểm tra nếu tìm thấy thông tin khách hàng
            if (customer != null) {
                // Cập nhật thông tin vào UserProfileUpdateRequest
                UserProfileUpdateRequest userProfile = new UserProfileUpdateRequest();
                userProfile.setAccount(customer.getAcount());
                userProfile.setFullName(customer.getFullName());
                userProfile.setEmail(customer.getEmail());
                userProfile.setNumberPhone(customer.getNumberPhone());
                userProfile.setGender(customer.getGender());
                userProfile.setBirthDay(customer.getBirthDay());
                userProfile.setImageString(customer.getImage());

                // Giả sử customer.getAddRess() trả về chuỗi địa chỉ đã gộp
                String fullAddress = customer.getAddRess();

                // Kiểm tra nếu địa chỉ không rỗng hoặc không null
                if (fullAddress != null && !fullAddress.trim().isEmpty()) {
                    String[] addressParts = fullAddress.split(",");

                    // Gán các trường địa chỉ
                    if (addressParts.length >= 4) {
                        userProfile.setWard(addressParts[2].trim());         // Tham số thứ 3 là xã/phường/thị trấn
                        userProfile.setDistrict(addressParts[1].trim());     // Tham số thứ 2 là quận/huyện
                        userProfile.setProvince(addressParts[0].trim());      // Tham số thứ 1 là tỉnh/thành phố

                        // Lấy tất cả các phần tử từ chỉ mục 3 trở đi và nối chúng lại
                        StringBuilder detailAddressBuilder = new StringBuilder();
                        for (int i = 3; i < addressParts.length; i++) {
                            detailAddressBuilder.append(addressParts[i].trim());
                            if (i < addressParts.length - 1) {
                                detailAddressBuilder.append(", "); // Thêm dấu phẩy nếu không phải phần tử cuối
                            }
                        }
                        userProfile.setAddRessDetail(detailAddressBuilder.toString());
                    } else {

                    }
                } else {
                    // Nếu địa chỉ không có giá trị, không làm gì cả
                    userProfile.setAddRessDetail("");
                    userProfile.setWard("");
                    userProfile.setDistrict("");
                    userProfile.setProvince("");
                }

                // Lấy thông tin ngày sinh
                LocalDate birthDay = customer.getBirthDay(); // Giả sử birthDay là kiểu LocalDate
                if (birthDay != null) {
                    model.addAttribute("birthDayDay", birthDay.getDayOfMonth());
                    model.addAttribute("birthDayMonth", birthDay.getMonthValue());
                    model.addAttribute("birthDayYear", birthDay.getYear());
                } else {
                    // Gán giá trị mặc định nếu không có thông tin ngày sinh
                    model.addAttribute("birthDayDay", "");
                    model.addAttribute("birthDayMonth", "");
                    model.addAttribute("birthDayYear", "");
                }

                // Đưa DTO vào model để hiển thị lên form
                model.addAttribute("userProfile", userProfile);
                model.addAttribute("clientLogin",clientLoginResponse);
                model.addAttribute("loginInfoClient", clientLoginResponse);
            } else {
                // Nếu không tìm thấy, đưa ra thông báo lỗi
                model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
            }
        } else {
            // Nếu người dùng chưa đăng nhập, chuyển hướng về trang login
            return "redirect:/onepoly/login";
        }

        // Trả về view userProfile để hiển thị thông tin
        return "client/UserProfile";
    }

    @PostMapping("/userProfile")
    public String updateProfile(@Valid UserProfileUpdateRequest userProfile, BindingResult result,
                                HttpSession session, @RequestParam("nameImage") MultipartFile nameImage, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại các trường thông tin.");
            return "client/UserProfile"; // Trả về trang hiện tại với thông báo lỗi
        }

        // Tiếp tục xử lý cập nhật như trước
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            String acount = clientLoginResponse.getAcount();
            Customer customer = customerRegisterRepository.findByAcount(acount);
            if (customer != null) {
                // Cập nhật thông tin người dùng
                customer.setAcount(userProfile.getAccount());
                customer.setFullName(userProfile.getFullName());
                customer.setEmail(userProfile.getEmail());
                customer.setNumberPhone(userProfile.getNumberPhone());
                customer.setGender(userProfile.getGender());
                customer.setBirthDay(userProfile.getBirthDay());
                customer.setAddRess(userProfile.getWard() + "," + userProfile.getDistrict() + "," + userProfile.getProvince() + "," + userProfile.getAddRessDetail());

                // Kiểm tra nếu người dùng có nhập ảnh không
                if (!nameImage.isEmpty()) {
                    customer.setImage(nameImage.getOriginalFilename()); // Lưu tên file
                    customerService.uploadFile(nameImage, customer.getId()); // Tải file lên
                }
                model.addAttribute("clientLogin",clientLoginResponse);
                customerRegisterRepository.save(customer);
                model.addAttribute("successMessage", "Cập nhật thông tin thành công.");
            } else {
                model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
            }
        } else {
            return "redirect:/onepoly/login";
        }

        return "client/UserProfile";
    }

}
