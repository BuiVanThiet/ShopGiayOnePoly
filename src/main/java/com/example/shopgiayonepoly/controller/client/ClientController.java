package com.example.shopgiayonepoly.controller.client;

import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.repositores.*;
import com.example.shopgiayonepoly.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/onepoly")
public class ClientController {
    @Autowired
    ClientSecurityResponsetory clientLoginResponse;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerRegisterService customerRegisterService;
    @Autowired
    StaffRegisterService staffRegisterService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ClientService clientService;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    BillDetailRepository billDetailRepository;

    @GetMapping("/home")
    public String getFormHomeClient(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
//        model.addAttribute("clientLogin", clientLoginResponse);
//        session.removeAttribute("clientInfo");
        List<ProductIClientResponse> listProductHighest = clientService.GetTop12ProductWithPriceHighest();
        List<ProductIClientResponse> listProductLowest = clientService.GetTop12ProductWithPriceLowest();
        model.addAttribute("listProductHighest", listProductHighest);
        model.addAttribute("listProductLowest", listProductLowest);
        model.addAttribute("clientLogin", clientLoginResponse);
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
    public List<ProductIClientResponse> getTop12ProductWithPriceHighest(HttpSession session,
                                                                        Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        return clientService.GetTop12ProductWithPriceHighest();
    }

    //Hiển thị 10 sản phẩm giá thấp nhất
    @GetMapping("/products-lowest")
    public List<ProductIClientResponse> getTop12ProductWithPriceLowest(HttpSession session,
                                                                       Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        return clientService.GetTop12ProductWithPriceLowest();

    }

    @GetMapping("/product-detail/{productID}")
    public String getFormProductDetail(@PathVariable("productID") Integer id,
                                       HttpSession session,
                                       Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        List<ProductDetailClientRespone> productDetailClientRespones = clientService.findProductDetailByProductId(id);
        List<ColorClientResponse> colors = clientService.findDistinctColorsByProductId(id);
        List<SizeClientResponse> sizes = clientService.findDistinctSizesByProductId(id);
        model.addAttribute("productDetail", productDetailClientRespones);
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);
        model.addAttribute("productID", id);
        model.addAttribute("clientLogin", clientLoginResponse);
        return "client/product_detail";
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(
            @RequestBody Map<String, Integer> requestData,
            Model model,
            HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer productDetailId = requestData.get("productDetailId");
        Integer quantity = requestData.get("quantity");
        Map<String, Object> response = new HashMap<>();
        if (quantity <= 0) {
            response.put("success", false);
            response.put("message", "Số lượng sản phẩm không hợp lệ.");
            return ResponseEntity.badRequest().body(response);
        }

        // Kiểm tra sản phẩm có tồn tại không
        ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);
        if (productDetail == null) {
            response.put("success", false);
            response.put("message", "Sản phẩm không tồn tại.");
            return ResponseEntity.badRequest().body(response);
        }

        // Lấy thông tin đăng nhập của khách hàng từ session
        ClientLoginResponse clientLogin = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLogin != null) {
            Integer idCustomerLogin = clientLogin.getId();
            Customer customer = customerRepository.findById(idCustomerLogin).orElse(null);

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            Cart existingCartItem = cartService.findByCustomerIDAndProductDetail(idCustomerLogin, productDetailId);

            if (existingCartItem != null) {
                // Cập nhật số lượng nếu sản phẩm đã tồn tại trong giỏ hàng
                existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                Cart cart = new Cart();
                cart.setId(existingCartItem.getId());
                cart.setCustomer(customer);
                cart.setProductDetail(productDetail);
                cart.setQuantity(existingCartItem.getQuantity()); // Cập nhật số lượng mới
                cart.setStatus(1);
                cart.setCreateDate(new Date());
                cart.setUpdateDate(existingCartItem.getUpdateDate()); //
                cartRepository.save(cart);
            } else {
                // Tạo mới sản phẩm trong giỏ hàng nếu chưa tồn tại
                Cart newCartItem = new Cart();
                newCartItem.setCustomer(customer);
                newCartItem.setProductDetail(productDetail);
                newCartItem.setQuantity(quantity);
                newCartItem.setStatus(1);
                cartRepository.save(newCartItem);
            }
        } else {
            // Nếu chưa đăng nhập, lưu giỏ hàng vào session
            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
            if (sessionCart == null) {
                sessionCart = new HashMap<>();
            }

            // Cập nhật số lượng sản phẩm trong session
            sessionCart.put(productDetailId, sessionCart.getOrDefault(productDetailId, 0) + quantity);
            session.setAttribute("sessionCart", sessionCart);
        }
        model.addAttribute("clientLogin", clientLoginResponse);
        response.put("success", true);
        response.put("message", "Thêm sản phẩm vào giỏ hàng thành công.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    public String getFromCart(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        List<Cart> cartItems = new ArrayList<>();

        if (clientLoginResponse != null) {
            Integer customerId = clientLoginResponse.getId();
            cartItems = cartService.getCartItemsForCustomer(customerId);
        } else {
            // Lấy giỏ hàng từ session nếu chưa đăng nhập
            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
                    Integer productDetailId = entry.getKey();
                    Integer quantity = entry.getValue();
                    ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);

                    if (productDetail != null) {
                        cartItems.add(new Cart(null, productDetail, quantity));
                    }
                }
            }
        }

        BigDecimal totalPriceCartItem = BigDecimal.ZERO;
        for (Cart item : cartItems) {
            BigDecimal itemTotalPrice = item.getProductDetail().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPriceCartItem = totalPriceCartItem.add(itemTotalPrice);
        }

        session.setAttribute("totalPrice", totalPriceCartItem);
        session.setAttribute("cartItems", cartItems);
        model.addAttribute("clientLogin", clientLoginResponse);
        model.addAttribute("cartItems", cartItems);
        return "client/cart";
    }

    @GetMapping("/payment")
    public String getFormPayment(HttpSession session, Model model) {
        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
        BigDecimal totalPrice = (BigDecimal) session.getAttribute("totalPrice");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        model.addAttribute("clientLogin", session.getAttribute("clientLogin"));
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "client/bill_payment";
    }

    @PostMapping("/payment")
    public String payBill(
            HttpSession session,
            Model model,
            @RequestParam("addressShip") String address,
            @RequestParam("shippingPrice") BigDecimal shippingPrice,
            @RequestParam("priceVoucher") BigDecimal priceVoucher,
            @RequestParam("noteBill") String noteBill) {

        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Bill bill = new Bill();
        List<BillDetail> billDetails = new ArrayList<>();
        if (clientLoginResponse != null) {
            Customer customer = new Customer();
            customer.setId(clientLoginResponse.getId());
            customer.setFullName(clientLoginResponse.getFullName());
            customer.setNumberPhone(clientLoginResponse.getNumberPhone());
            customer.setBirthDay(clientLoginResponse.getBirthDay());
            customer.setImage(clientLoginResponse.getImage());
            customer.setEmail(clientLoginResponse.getEmail());
            customer.setAcount(clientLoginResponse.getAcount());
            customer.setPassword(clientLoginResponse.getPassword());
            customer.setGender(clientLoginResponse.getGender());
            customer.setAddRess(clientLoginResponse.getAddRess());

            for (Cart cart : cartItems) {
                BillDetail billDetail = new BillDetail();
                billDetail.setBill(bill);
                billDetail.setProductDetail(cart.getProductDetail());
                billDetail.setQuantity(cart.getQuantity());
                billDetail.setPriceRoot(cart.getProductDetail().getPrice());
                billDetail.setPrice(cart.getProductDetail().getPrice());

                BigDecimal totalAmount = cart.getProductDetail().getPrice()
                        .multiply(BigDecimal.valueOf(cart.getQuantity()));
                billDetail.setTotalAmount(totalAmount);
                billDetails.add(billDetail);
            }

            BigDecimal totalAmountBill = billDetails.stream()
                    .map(bd -> bd.getPrice().multiply(BigDecimal.valueOf(bd.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            bill.setAddRess(customer.getAddRess());
            bill.setCodeBill("HD" + bill.getId());
            bill.setCustomer(customer);
            bill.setStaff(null);
            bill.setVoucher(null);
            bill.setShippingPrice(shippingPrice);
            bill.setTotalAmount(totalAmountBill);
            bill.setPaymentMethod(0);
            bill.setBillType(1);
            bill.setPaymentStatus(0);
            bill.setSurplusMoney(null);
            bill.setPriceDiscount(null);
            bill.setNote(noteBill);
        } else {
            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
                    Integer productDetailId = entry.getKey();
                    Integer quantity = entry.getValue();

                    ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);
                    if (productDetail != null) {
                        BillDetail billDetail = new BillDetail();
                        billDetail.setBill(bill);
                        billDetail.setProductDetail(productDetail);
                        billDetail.setQuantity(quantity);
                        billDetail.setPriceRoot(productDetail.getPrice());
                        billDetail.setPrice(productDetail.getPrice());

                        BigDecimal totalAmount = productDetail.getPrice().multiply(BigDecimal.valueOf(quantity));
                        billDetail.setTotalAmount(totalAmount);
                        billDetails.add(billDetail);
                    }
                }

                BigDecimal totalAmountBill = billDetails.stream()
                        .map(bd -> bd.getPrice().multiply(BigDecimal.valueOf(bd.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                bill.setCodeBill("HD" + bill.getId().toString());
                bill.setCustomer(null);
                bill.setStaff(null);
                bill.setAddRess(address);
                bill.setVoucher(null);
                bill.setShippingPrice(shippingPrice);
                bill.setTotalAmount(totalAmountBill);
                bill.setPaymentMethod(0);
                bill.setBillType(1);
                bill.setPaymentStatus(0);
                bill.setSurplusMoney(null);
                bill.setPriceDiscount(null);
                bill.setNote(noteBill);
            }
        }

        billRepository.save(bill);
        billDetailRepository.saveAll(billDetails);

        session.removeAttribute("cartItems");
        session.removeAttribute("sessionCart");

        return "client/bill_customer";
    }


    @GetMapping("/delete/product-cart/{idProductDetailFromCart}")
    public String deleteProductDetailFromCart(HttpSession session,
                                              Model model,
                                              @PathVariable("idProductDetailFromCart") Integer idProductDetailFromCart) {
        List<CartItemResponse> cartItems = (List<CartItemResponse>) model.getAttribute("cartItems");
        for (CartItemResponse item : cartItems) {
            if (item.getProductDetailId() == idProductDetailFromCart) {
                cartItems.remove(item);
            }
        }
        return "redirect:/onepoly/cart";
    }

    @PostMapping("/update/product-cart/{idProductDetailFromCart}")
    public String updateProductDetailFromCart(HttpSession session,
                                              Model model,
                                              @PathVariable("idProductDetailFromCart") Integer idProductDetailFromCart,
                                              @RequestParam("quantity") Integer quantityFormCart) {
        List<CartItemResponse> cartItems = (List<CartItemResponse>) session.getAttribute("cartItems");

        if (cartItems != null) {
            for (CartItemResponse item : cartItems) {
                if (item.getProductDetailId().equals(idProductDetailFromCart)) {
                    item.setQuantity(quantityFormCart);
                    item.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(quantityFormCart)));
                    break;
                }
            }
        }
        session.setAttribute("cartItems", cartItems);
        return "redirect:/onepoly/cart";
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
//        return "login/loginClient";
        return "client/homepage";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session, Model model) {
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("usernameError", "Tên tài khoản không được để trống");
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("passwordError", "Mật khẩu không được để trống");
        }

        // Nếu có lỗi, trả về trang đăng nhập với các thông báo lỗi
        if (model.containsAttribute("usernameError") || model.containsAttribute("passwordError")) {
            model.addAttribute("usernameLogin", username); // Giữ lại giá trị username
            return "login/loginClient"; // Trả về trang đăng nhập
        }

        ClientLoginResponse clientLoginResponse = this.clientLoginResponse.getCustomerByEmailAndAcount(username, username);
        if (clientLoginResponse != null && passwordEncoder.matches(password, passwordEncoder.encode(clientLoginResponse.getPassword()))) {
            session.setAttribute("clientLogin", clientLoginResponse);
            System.out.println(clientLoginResponse.toString());
            return "redirect:/onepoly/home";
        } else {
            model.addAttribute("usernameLogin", username);
            model.addAttribute("errorMessage", "Sai tên tài khoản hoặc mật khẩu");
            return "login/loginClient";
        }
    }

    @GetMapping("/register")
    public String formRegister(Model model, HttpSession session) {
        RegisterRequest registerRequest = new RegisterRequest();
        // Lấy giá trị từ session và set vào RegisterRequest
        String acount = (String) session.getAttribute("acount");
        String email = (String) session.getAttribute("email");
        if (acount != null) {
            registerRequest.setAcount(acount);
        }
        if (email != null) {
            registerRequest.setEmail(email);
        }

        // Thêm đối tượng registerRequest vào model
        model.addAttribute("registerRequest", registerRequest);
        model.addAttribute("errorMessage", session.getAttribute("errorMessage"));

        // Xóa session sau khi dùng xong
        session.removeAttribute("acount");
        session.removeAttribute("email");
        session.removeAttribute("errorMessage");
        return "client/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") @Valid RegisterRequest registerRequest,
                           BindingResult bindingResult, Model model, HttpSession session) {

        session.setAttribute("acount", registerRequest.getAcount());
        session.setAttribute("email", registerRequest.getEmail());

        if (customerRegisterService.existsByAcount(registerRequest.getAcount())) {
            model.addAttribute("errorMessage", "Tên đăng nhập  đã tồn tại.");
            return "client/register";
        }

        if (staffRegisterService.existsByAcount(registerRequest.getAcount())) {
            model.addAttribute("errorMessage", "Tên đăng nhập đã tồn tại.");
            return "client/register";
        }

        if (customerRegisterService.existsByEmail(registerRequest.getEmail())) {
            model.addAttribute("errorMessage", "Email đã tồn tại.");
            return "client/register";
        }

        if (staffRegisterService.existsByEmail(registerRequest.getEmail())) {
            model.addAttribute("errorMessage", "Email đã tồn tại.");
            return "client/register";
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Mật khẩu và xác nhận mật khẩu không trùng khớp.");
            return "client/register";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("registerRequest", registerRequest);
            return "client/register";  // Trả về template trực tiếp, không dùng redirect
        }

        Customer customer = new Customer();
        customer.setAcount(registerRequest.getAcount());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(registerRequest.getPassword());
        customer.setFullName(" ");
        customer.setNumberPhone(" ");
        customer.setGender(1);
        customer.setStatus(1);
        customerRegisterService.save(customer);

        session.removeAttribute("acount");
        session.removeAttribute("email");
        session.removeAttribute("errorMessage");
        return "redirect:/onepoly/login";
    }


//    @GetMapping("/userProfile")
//    public String formProfile(Model model, HttpSession session) {
////        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
//        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
//        if (clientLoginResponse != null) {
//            String acount = clientLoginResponse.getAcount();
//
//            Customer customer = customerRegisterRepository.findByAcount(acount);
//            // Kiểm tra nếu tìm thấy thông tin khách hàng
//            if (customer != null) {
//                // Cập nhật thông tin vào UserProfileUpdateRequest
//                UserProfileUpdateRequest userProfile = new UserProfileUpdateRequest();
//                userProfile.setAccount(customer.getAcount());
//                userProfile.setPassword(customer.getPassword());
//                userProfile.setFullName(customer.getFullName());
//                userProfile.setEmail(customer.getEmail());
//                userProfile.setNumberPhone(customer.getNumberPhone());
//                userProfile.setGender(customer.getGender());
//                userProfile.setBirthDay(customer.getBirthDay());
//
//                String[] part = customer.getAddRess().split(",\\s*");
//                userProfile.setProvince(part[2]);
//                userProfile.setDistrict(part[1]);
//                userProfile.setWard(part[0]);
//                userProfile.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
//                userProfile.setImageString(customer.getImage());
//
//                // Lấy thông tin ngày sinh
//                LocalDate birthDay = customer.getBirthDay(); // Giả sử birthDay là kiểu LocalDate
//                if (birthDay != null) {
//                    model.addAttribute("birthDayDay", birthDay.getDayOfMonth());
//                    model.addAttribute("birthDayMonth", birthDay.getMonthValue());
//                    model.addAttribute("birthDayYear", birthDay.getYear());
//                } else {
//                    // Gán giá trị mặc định nếu không có thông tin ngày sinh
//                    model.addAttribute("birthDayDay", "");
//                    model.addAttribute("birthDayMonth", "");
//                    model.addAttribute("birthDayYear", "");
//                }
//
//                // Đưa DTO vào model để hiển thị lên form
//                model.addAttribute("userProfile", userProfile);
//                model.addAttribute("clientLogin", clientLoginResponse);
//                model.addAttribute("loginInfoClient", clientLoginResponse);
//            } else {
//                // Nếu không tìm thấy, đưa ra thông báo lỗi
//                model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
//            }
//        } else {
//            // Nếu người dùng chưa đăng nhập, chuyển hướng về trang login
//            return "redirect:/onepoly/login";
//        }
//
//        // Trả về view userProfile để hiển thị thông tin
//        return "client/UserProfile";
//    }
//
//    @PostMapping("/userProfileUpdate")
//    public String updateProfile(UserProfileUpdateRequest userProfile,
//                                HttpSession session, @RequestParam("nameImage") MultipartFile nameImage, Model model) throws IOException {
//        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
//        if (clientLoginResponse != null) {
//            String acount = clientLoginResponse.getAcount();
//            Customer customer = customerRegisterRepository.findByAcount(acount);
//            if (customer != null) {
//                // Cập nhật thông tin người dùng
//                customer.setFullName(userProfile.getFullName());
//                customer.setPassword(userProfile.getPassword());
//                customer.setEmail(userProfile.getEmail());
//                customer.setNumberPhone(userProfile.getNumberPhone());
//                customer.setGender(userProfile.getGender());
//                customer.setBirthDay(userProfile.getBirthDay());
//                customer.setAddRess(userProfile.getWard() + "," + userProfile.getDistrict() + "," + userProfile.getProvince() + "," + userProfile.getAddRessDetail());
//
//                // Kiểm tra nếu người dùng có nhập ảnh không
//                if (!nameImage.isEmpty()) {
//                    customer.setImage(nameImage.getOriginalFilename()); // Lưu tên file
//                    customerService.uploadFile(nameImage, customer.getId()); // Tải file lên
//                }
//                model.addAttribute("clientLogin", clientLoginResponse);
//                model.addAttribute("userProfile", userProfile);
//                model.addAttribute("clientLogin", clientLoginResponse);
//                customerRegisterRepository.save(customer);
//                model.addAttribute("successMessage", "Cập nhật thông tin thành công.");
//            } else {
//                model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
//            }
//        } else {
//            return "redirect:/onepoly/login";
//        }
//
//        return "redirect:/onepoly/UserProfile";
//    }

}
