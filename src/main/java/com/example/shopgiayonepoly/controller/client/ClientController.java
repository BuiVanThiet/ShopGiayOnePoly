package com.example.shopgiayonepoly.controller.client;

import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.dto.request.UserProfileUpdateRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone;
import com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.implement.CustomerRegisterImplement;
import com.example.shopgiayonepoly.repositores.ClientSecurityResponsetory;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import com.example.shopgiayonepoly.service.ClientService;
import com.example.shopgiayonepoly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    ClientService clientService;

    @GetMapping("/home")
    public String getFormHomeClient(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        session.removeAttribute("clientInfo");
        return "client/homepage";
    }

    @GetMapping("/base")
    public String getFormBaseClient(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        return "client/base";
    }

    @GetMapping("/products")
    public String getFormProduct(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        List<ProductIClientResponse> listProduct = clientService.getAllProduct();
        model.addAttribute("listProduct", listProduct);
        return "client/product";
    }

    //    Test api address
    @GetMapping("/address")
    public String getPriceByGHN(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        return "client/address";
    }

    //Hiển thị 10 sản phẩm giá cao nhất
    @GetMapping("/products-highest")
    public ResponseEntity<List<ProductIClientResponse>> getTop12ProductWithPriceHighest(HttpSession session,
                                                                                        Model model) {

        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        List<ProductIClientResponse> productsHighest = clientService.GetTop12ProductWithPriceHighest();
        return ResponseEntity.ok(productsHighest);

    }

    @GetMapping("/product-detail/{productID}")
    public ResponseEntity<List<ProductDetailClientRespone>> getFormProductDetail(@PathVariable("productID") Integer id,
                                                                                 HttpSession session,
                                                                                 Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        List<ProductDetailClientRespone> productDetailClientRespone = clientService.findProductDetailByProductId(id);
        if (productDetailClientRespone == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(productDetailClientRespone);
    }


    //    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    public String getFormLoginClient() {
        return "login/loginClient";
    }

    @GetMapping("/logout")
    public String getLogoutClient(HttpSession session, Model model) {
        session.removeAttribute("clientLogin");
        model.addAttribute("errorMessage", "");
        return "login/loginClient";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = this.clientLoginResponse.getCustomerByEmailAndAcount(username, username);
        if (clientLoginResponse != null && passwordEncoder.matches(password, passwordEncoder.encode(clientLoginResponse.getPassword()))) {
            session.setAttribute("clientLogin", clientLoginResponse);
            return "redirect:/onepoly/home";
        } else {
            model.addAttribute("usernameLogin", username);
            model.addAttribute("errorMessage", "Sai tên tài khoản hoặc mật khẩu");
            return "login/loginClient";
        }
    }

    @GetMapping("/register")
    public String formRegister(Model model, HttpSession session) {
        String acount = (String) session.getAttribute("acount");
        String email = (String) session.getAttribute("email");
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

        if (customerRegisterRepository.existsByEmail(registerRequest.getEmail())) {
            session.setAttribute("errorMessage", "Email đã tồn tại. Vui lòng chọn email khác.");
            return "redirect:/onepoly/register";
        }
        if (customerRegisterRepository.existsByAcount(registerRequest.getAcount())) {
            session.setAttribute("errorMessage", "Tên đăng nhập đã tồn tại. Vui lòng chọn Tên đăng nhập khác.");
            return "redirect:/onepoly/register";
        }

        Customer customer = new Customer();
        customer.setAcount(registerRequest.getAcount());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(registerRequest.getPassword());
        customer.setFullName(" ");
        customer.setNumberPhone(" ");
        customer.setGender(1);
        customer.setStatus(1);

        String message = customerRegisterImplement.registerCustomer(customer);

        if (message.equals("Đăng ký thành công!")) {
            session.setAttribute("successMessage", message);
            session.removeAttribute("acount");
            session.removeAttribute("email");
            session.removeAttribute("errorMessage");
            return "redirect:/onepoly/login";
        } else {
            session.setAttribute("errorMessage", message);
            return "redirect:/onepoly/register";
        }
    }

    @GetMapping("/userProfile")
    public String formProfile(Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            String acount = clientLoginResponse.getAcount();

            Customer customer = customerRegisterRepository.findByAcount(acount);
            // Kiểm tra nếu tìm thấy thông tin khách hàng
            if (customer != null) {
                // Cập nhật thông tin vào UserProfileUpdateRequest
                UserProfileUpdateRequest userProfile = new UserProfileUpdateRequest();
                userProfile.setAccount(customer.getAcount());
                userProfile.setPassword(customer.getPassword());
                userProfile.setFullName(customer.getFullName());
                userProfile.setEmail(customer.getEmail());
                userProfile.setNumberPhone(customer.getNumberPhone());
                userProfile.setGender(customer.getGender());
                userProfile.setBirthDay(customer.getBirthDay());

                String[] part = customer.getAddRess().split(",\\s*");
                userProfile.setProvince(part[2]);
                userProfile.setDistrict(part[1]);
                userProfile.setWard(part[0]);
                userProfile.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
                userProfile.setImageString(customer.getImage());

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
                model.addAttribute("clientLogin", clientLoginResponse);
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
    public String updateProfile(@Valid UserProfileUpdateRequest userProfile, BindingResult bindingResult,
                                HttpSession session, @RequestParam("nameImage") MultipartFile nameImage, Model model) throws IOException {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (bindingResult.hasErrors()) {
            model.addAttribute("userProfile", userProfile);
            return "client/userProfile";
        }
        if (clientLoginResponse != null) {
            String acount = clientLoginResponse.getAcount();
            Customer customer = customerRegisterRepository.findByAcount(acount);
            if (customer != null) {
                // Cập nhật thông tin người dùng
                customer.setFullName(userProfile.getFullName());
                customer.setPassword(userProfile.getPassword());
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
                model.addAttribute("clientLogin", clientLoginResponse);
                model.addAttribute("userProfile", userProfile);
                model.addAttribute("clientLogin", clientLoginResponse);
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
