package com.example.shopgiayonepoly.controller.client;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.dto.request.client.PaymentBillRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.repositores.*;
import com.example.shopgiayonepoly.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/onepoly")
public class ClientController extends BaseBill {
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
    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherRepository voucherRepository;

    @GetMapping("/home")
    public String getFormHomeClient(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
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

    @GetMapping("/cart")
    public String getFromCart(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        List<CartResponse> cartResponses = new ArrayList<>();
        List<Cart> cartItems = new ArrayList<>();

        // Kiểm tra nếu người dùng đã đăng nhập
        if (clientLoginResponse != null) {
            Integer customerId = clientLoginResponse.getId();
            cartItems = cartService.getCartItemsForCustomer(customerId);

            for (Cart cartItem : cartItems) {
                ProductDetail productDetail = cartItem.getProductDetail();
                BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetail.getId());

                if (discountedPrice == null) {
                    discountedPrice = productDetail.getPrice();
                }

                CartResponse cartResponse = new CartResponse(
                        cartItem.getId(),
                        cartItem.getCustomer().getId(),
                        productDetail.getId(),
                        productDetail.getProduct().getNameProduct(),
                        productDetail.getColor().getNameColor(),
                        productDetail.getSize().getNameSize(),
                        cartItem.getQuantity(), // Số lượng sẽ được lấy từ CartItem
                        productDetail.getPrice(),
                        discountedPrice
                );
                cartResponses.add(cartResponse);
            }
        } else {
            // Nếu người dùng chưa đăng nhập, lấy cartItems từ session
            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
                    Integer productDetailId = entry.getKey();
                    Integer quantity = entry.getValue();
                    ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);

                    if (productDetail != null) {
                        BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetailId);

                        if (discountedPrice == null) {
                            discountedPrice = productDetail.getPrice();
                        }

                        CartResponse cartResponse = new CartResponse(
                                null,
                                null,
                                productDetail.getId(),
                                productDetail.getProduct().getNameProduct(),
                                productDetail.getColor().getNameColor(),
                                productDetail.getSize().getNameSize(),
                                quantity, // Số lượng lấy từ sessionCart
                                productDetail.getPrice(),
                                discountedPrice
                        );
                        cartResponses.add(cartResponse);
                    }
                }
            }
        }

        // Tính tổng giá trị giỏ hàng
        BigDecimal totalPriceCartItem = BigDecimal.ZERO;
        for (CartResponse item : cartResponses) {
            BigDecimal finalPrice = item.getDiscountedPrice();
            BigDecimal itemTotalPrice = finalPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPriceCartItem = totalPriceCartItem.add(itemTotalPrice);
        }

        // Cập nhật session với tổng giá trị giỏ hàng và các sản phẩm trong giỏ hàng
        session.setAttribute("totalPrice", totalPriceCartItem);
        session.setAttribute("cartItems", cartResponses);
        model.addAttribute("clientLogin", clientLoginResponse);
        model.addAttribute("cartItems", cartResponses);
        model.addAttribute("totalPrice", totalPriceCartItem);

        // Lấy danh sách các voucher áp dụng cho giỏ hàng
        List<Voucher> applicableVouchers = voucherService.findApplicableVouchers(totalPriceCartItem);
        model.addAttribute("applicableVouchers", applicableVouchers);

        // Lấy voucher đã chọn từ session (nếu có)
        VoucherClientResponse selectedVoucher = (VoucherClientResponse) session.getAttribute("selectedVoucher");
        BigDecimal priceReduced = BigDecimal.ZERO;

        if (selectedVoucher != null) {
            model.addAttribute("selectedVoucher", selectedVoucher);
            System.out.println("ID Voucher selected: " + selectedVoucher.getId());

            Integer voucherType = selectedVoucher.getVoucherType();
            Integer idVoucherApply = selectedVoucher.getId();
            BigDecimal discountValue = selectedVoucher.getPriceReduced();

            if (voucherType == 1) {  // Giảm theo %
                priceReduced = totalPriceCartItem.multiply(discountValue.divide(BigDecimal.valueOf(100)));
            } else if (voucherType == 2) {  // Giảm theo số tiền cố định
                priceReduced = discountValue;
            }

            session.setAttribute("priceReduced", priceReduced);
            model.addAttribute("priceReducedShow", priceReduced);
            session.setAttribute("idVoucherApply", idVoucherApply);
            session.setAttribute("selectedVoucher", selectedVoucher);
            System.out.println("Session priceReduced :" + priceReduced);
            System.out.println("Session idVoucherApply :" + idVoucherApply);
        } else {
            System.out.println("Không có voucher selected.");
        }

        return "client/cart";
    }

    @GetMapping("/payment")
    public String getFormPayment(HttpSession session, Model model) {
        // Lấy giỏ hàng từ session và ép kiểu thành List<CartResponse>
        List<CartResponse> cartItems = Optional.ofNullable((List<CartResponse>) session.getAttribute("cartItems")).orElseGet(ArrayList::new);

        Integer idVoucherApply = (Integer) session.getAttribute("idVoucherApply");
        BigDecimal priceReduced = (BigDecimal) session.getAttribute("priceReduced");
        BigDecimal totalPrice = (BigDecimal) session.getAttribute("totalPrice");
        VoucherClientResponse selectedVoucher = (VoucherClientResponse) session.getAttribute("selectedVoucher");
        Double weight = 0.0;

        if (totalPrice == null) {
            totalPrice = BigDecimal.ZERO;
        }

        BigDecimal calculatedTotalPrice = BigDecimal.ZERO;
        for (CartResponse c : cartItems) {
            // Lấy giá sản phẩm và số lượng từ CartResponse
            BigDecimal price = c.getDiscountedPrice() != null ? c.getDiscountedPrice() : c.getOriginalPrice(); // Lấy giá giảm nếu có
            int quantity = c.getQuantity();
            BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));
            calculatedTotalPrice = calculatedTotalPrice.add(totalAmount);
            ProductDetail productDetail = productDetailRepository.findById(c.getProductDetailId()).get();
            if (productDetail != null) {
                Double itemWeight = quantity * productDetail.getWeight();
                weight += itemWeight;
            }
            System.out.println("Product ID: " + c.getProductDetailId());
            System.out.println("Price item: " + price);
            System.out.println("Weight: " + weight);
            System.out.println("Quantity: " + quantity);
            System.out.println("Total amount for this item: " + totalAmount);
        }

        // So sánh giá trị calculatedTotalPrice với totalPrice trong session
        System.out.println("Total calculated price: " + calculatedTotalPrice);
        System.out.println("Total price in session: " + totalPrice);

        // Thêm thông tin đăng nhập của khách hàng vào model nếu có
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");

        if (clientLoginResponse != null) {
            model.addAttribute("clientLogin", clientLoginResponse);

            // Lấy và chuẩn hóa địa chỉ của khách hàng
            String addressCustomerLogin = clientLoginResponse.getAddRess();
            String[] addressParts = addressCustomerLogin.split(",", 4);

            String idWard = addressParts.length > 0 ? addressParts[0].trim() : "";
            String idDistrict = addressParts.length > 1 ? addressParts[1].trim() : "";
            String idProvince = addressParts.length > 2 ? addressParts[2].trim() : "";
            String originalAddress = addressParts.length > 3 ? addressParts[3].trim() : "";
            String infoCustomer = clientLoginResponse.getFullName() + clientLoginResponse.getNumberPhone();
            model.addAttribute("infoCustomer", infoCustomer);
            model.addAttribute("originalAddress", originalAddress);

            model.addAttribute("IdWard", idWard);
            model.addAttribute("IdDistrict", idDistrict);
            model.addAttribute("IdProvince", idProvince);

            // Lấy danh sách AddressShip và chuẩn hóa
            List<AddressShip> addressList = clientService.getListAddressShipByIDCustomer(clientLoginResponse.getId());
            List<AddressShipReponse> responseListAddress = new ArrayList<>();

            for (AddressShip address : addressList) {
                String specificAddress = address.getSpecificAddress();
                if (specificAddress != null) {
                    String[] parts = specificAddress.split(",");

                    // Kiểm tra xem địa chỉ có đủ các phần như Phường, Quận, Tỉnh không
                    String shipProvince = "", shipDistrict = "", shipWard = "", detailedAddress = "";

                    // Kiểm tra địa chỉ có đúng định dạng (địa chỉ chuẩn như khách hàng hay không)
                    if (parts.length >= 7) {
                        // Địa chỉ có đủ các phần: Phường, Quận, Tỉnh
                        shipProvince = parts.length > 3 ? parts[3].trim() : "";
                        shipDistrict = parts.length > 4 ? parts[4].trim() : "";
                        shipWard = parts.length > 5 ? parts[5].trim() : "";
                        detailedAddress = parts.length > 6
                                ? String.join(", ", Arrays.copyOfRange(parts, 6, parts.length)).trim()
                                : "";
                    } else {
                        // Địa chỉ không có đủ các phần: Phường, Quận, Tỉnh, xử lý đặc biệt
                        shipProvince = "UnknownProvince"; // Gán giá trị mặc định
                        shipDistrict = "UnknownDistrict"; // Gán giá trị mặc định
                        shipWard = "UnknownWard"; // Gán giá trị mặc định
                        detailedAddress = specificAddress.trim(); // Toàn bộ địa chỉ là chi tiết
                    }


                    String formattedShipAddress = String.join(", ", shipProvince, shipDistrict, shipWard, detailedAddress).replaceAll(", $", "");

// Kiểm tra số lượng dấu phẩy trong địa chỉ đã chuẩn hóa
                    long commaCount = formattedShipAddress.chars().filter(ch -> ch == ',').count();

// Nếu địa chỉ có 4 dấu phẩy (có 5 phần khi tách), thì thêm vào danh sách
                    if (commaCount >3) {
                        responseListAddress.add(new AddressShipReponse(formattedShipAddress, detailedAddress));
                        System.out.println("Formatted Ship Address: " + formattedShipAddress);
                    }

                }

        }

        model.addAttribute("listAddress", responseListAddress);
    }

    // Cập nhật lại giỏ hàng và các thuộc tính vào model và session
        model.addAttribute("cartItems",cartItems);
        model.addAttribute("weight",weight);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("priceReducedShow",priceReduced);
        session.setAttribute("idVoucherApply",idVoucherApply);
        session.setAttribute("priceReduced",priceReduced);
        session.setAttribute("selectedVoucher",selectedVoucher);

    // Trả về view
        return"client/bill_payment";
}


    @PostMapping("/payment")
    @ResponseBody
    public String payBill(
            HttpSession session,
            @RequestBody PaymentBillRequest paymentRequest,
            HttpServletRequest request) {
        // Lấy thông tin từ yêu cầu
        String address = paymentRequest.getAddressShip();
        BigDecimal shippingPrice = paymentRequest.getShippingPrice();
        BigDecimal totalAmountBill = paymentRequest.getTotalAmountBill();
        BigDecimal priceVoucher = paymentRequest.getPriceVoucher();
        String noteBill = paymentRequest.getNoteBill();
        Integer payMethod = paymentRequest.getPayMethod();


        Integer idVoucherApply = (Integer) session.getAttribute("idVoucherApply");
        BigDecimal priceReduced = (BigDecimal) session.getAttribute("priceReduced");

        if (priceReduced == null) {
            priceReduced = BigDecimal.ZERO;  // Gán giá trị mặc định nếu priceReduced là null
        }

        VoucherClientResponse voucherApply = (VoucherClientResponse) session.getAttribute("selectedVoucher");
        Voucher voucher = null;
        if (idVoucherApply != null) {
            voucher = voucherRepository.findById(idVoucherApply).orElse(null);
        }
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Bill bill = new Bill();
        List<BillDetail> billDetails = new ArrayList<>();

        if (clientLoginResponse != null) {
            // Thiết lập thông tin khách hàng
            Customer customer = this.customerService.getCustomerByID(clientLoginResponse.getId());

            for (CartResponse cart : cartItems) {
                BillDetail billDetail = new BillDetail();
                ProductDetail productDetail = productDetailRepository.findById(cart.getProductDetailId()).get();
                billDetail.setBill(bill);
                billDetail.setProductDetail(productDetail);
                billDetail.setQuantity(cart.getQuantity());
                BigDecimal price = cart.getDiscountedPrice();
                billDetail.setPriceRoot(cart.getOriginalPrice());
                billDetail.setPrice(cart.getDiscountedPrice());
                BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(cart.getQuantity()));
                billDetail.setTotalAmount(totalAmount);
                billDetail.setStatus(1);
                billDetail.setCreateDate(new Date());
                billDetail.setUpdateDate(new Date());
                billDetails.add(billDetail);

            }
            // Cập nhật thông tin hóa đơn
            bill.setAddRess(customer.getFullName() + "," + customer.getNumberPhone() + "," + customer.getEmail() + "," + customer.getAddRess());
            bill.setCustomer(customer);
            bill.setShippingPrice(shippingPrice);
            bill.setTotalAmount(totalAmountBill);
            bill.setPaymentMethod(payMethod); // Giả định phương thức thanh toán mặc định là 0
            bill.setBillType(2); // Giả định loại hóa đơn mặc định là 1
            bill.setPaymentStatus(0); // Giả định trạng thái thanh toán mặc định là 0
            bill.setNote(noteBill);
            bill.setVoucher(voucher);
            bill.setPriceDiscount(priceReduced);
            bill.setStatus(1); // Trạng thái hóa đơn là 1
            bill.setCreateDate(new Date());
            bill.setUpdateDate(new Date());
        } else {
            // Xử lý với sessionCart
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
                        billDetail.setStatus(1);
                        billDetail.setCreateDate(new Date());
                        billDetail.setUpdateDate(new Date());
                        billDetails.add(billDetail);
                    }
                }
                // Cập nhật thông tin hóa đơn
                bill.setAddRess(address);
                bill.setShippingPrice(shippingPrice);
                bill.setTotalAmount(totalAmountBill);
                bill.setPaymentMethod(payMethod);
                bill.setBillType(2);
                bill.setPaymentStatus(0);
                bill.setVoucher(voucher);
                bill.setPriceDiscount(priceReduced);
                bill.setNote(noteBill);
                bill.setStatus(1);
                bill.setCreateDate(new Date());
                bill.setUpdateDate(new Date());
            }
        }

        // In ra tổng tiền hóa đơn
        System.out.println("Tổng tiền hóa đơn sau giảm giá: " + totalAmountBill);
        System.out.println("Weight: " + totalAmountBill);

        // Lưu hóa đơn
        billRepository.save(bill);
        bill.setCodeBill("HD" + bill.getId()); // Tạo mã hóa đơn
        billRepository.save(bill); // Cập nhật lại hóa đơn với mã
        for (BillDetail billDetail : billDetails) {
            billDetail.setBill(bill);
        }
        billDetailRepository.saveAll(billDetails);

        System.out.println("dang dung phuong thuc thanh toan: " + bill.getPaymentMethod());
        if (bill.getPaymentMethod() == 2) {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            BigDecimal totalPriceFinal = bill.getTotalAmount().subtract(bill.getPriceDiscount()).add(bill.getShippingPrice());
            Integer priceAsInteger = totalPriceFinal.setScale(0, RoundingMode.DOWN).intValue();
            String vnpayUrl = vnPayService.createOrder((priceAsInteger), "chuyenKhoan", baseUrl);
            System.out.println("Thong tin Bill thanh toan bang tien va tk(2)" + bill.toString());
            session.setAttribute("pageReturn", 3);
            session.setAttribute("payBillOrder", bill);
            return vnpayUrl;
        }
        String host = "http://localhost:8080/onepoly/status-bill/" + bill.getId();
        this.setBillStatus(bill.getId(), 0, session);
        this.setBillStatus(bill.getId(), bill.getStatus(), session);

        return "/onepoly/order-success";
    }

    @GetMapping("/order-success")
    public String getFormOderSuccess(HttpSession session) {
        session.removeAttribute("cartItems");
        session.removeAttribute("sessionCart");
        return "client/order-success";
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // thong tin hoa don
    @GetMapping("/status-bill/{id}")
    public String getFormStatusBill(@PathVariable("id") String idBill, Model model, HttpSession session) {
        System.out.println("id bill ben controller: " + idBill);
        try {
            Integer idInteger = Integer.parseInt(idBill);
            session.setAttribute("idCheckStatusBill", idInteger);
            System.out.println("id bill ben controller: " + idInteger);
            return "client/statusBillClient";
        } catch (NumberFormatException e) {
            // Chuyển hướng đến trang 404 nếu không phải là số nguyên
            return "redirect:/404";
        }
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
