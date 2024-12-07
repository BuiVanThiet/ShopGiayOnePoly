package com.example.shopgiayonepoly.restController.client;

import com.example.shopgiayonepoly.baseMethod.BaseEmail;
import com.example.shopgiayonepoly.dto.request.bill.SearchBillByStatusRequest;
import com.example.shopgiayonepoly.dto.request.client.AddressForCustomerRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.bill.BillResponseManage;
import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.implement.CustomerRegisterImplement;
import com.example.shopgiayonepoly.repositores.*;
import com.example.shopgiayonepoly.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-client")
public class ClientRestController extends BaseEmail {
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
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CartRepository cartRepository;
    @Autowired
    InvoiceStatusService invoiceStatusService;
    @Autowired
    BillService billService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    AddressShipRepository addressShipRepository;
    @Autowired
    protected PdfTemplateService pdfTemplateService;

    String messages = "";
    String check = "";

    @GetMapping("/products/top12-highest")
    public List<ProductIClientResponse> getTop12ProductHighest() {
        return clientService.GetTop12ProductWithPriceHighest();
    }

    @GetMapping("/products/top12-lowest")
    public List<ProductIClientResponse> getTop12ProducLowest() {
        return clientService.GetTop12ProductWithPriceLowest();
    }

    @GetMapping("/products/product-detail")
    public ProductDetailClientRespone getProductDetail(@RequestParam Integer productId, @RequestParam Integer colorId, @RequestParam Integer sizeId) {
        ProductDetailClientRespone productDetail = clientService.findByProductDetailColorAndSizeAndProductId(colorId, sizeId, productId);
        if (productDetail != null) {
            return productDetail;
        }
        return null;
    }

    @GetMapping("/selected-voucher/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> VoucherResponseByID(@PathVariable("id") Integer idVoucher, HttpSession session) {
        Map<String, Object> messages = new HashMap<>();
        VoucherClientResponse selectedVoucher = clientService.findVoucherApplyByID(idVoucher);

        if (selectedVoucher == null) {
            System.out.println("Voucher không tồn tại");
            messages.put("message", "Voucher không tồn tại hoặc đã hết hạn.");
            messages.put("check", "0");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }

        // Lưu voucher vào session
        session.setAttribute("selectedVoucher", selectedVoucher);
        System.out.println("Voucher đã chọn: " + selectedVoucher);

        // Trả về thông tin voucher cùng với check và message
        messages.put("message", "Áp dụng voucher thành công!");
        messages.put("check", "1");
        messages.put("voucherType", selectedVoucher.getVoucherType());
        messages.put("priceReduced", selectedVoucher.getPriceReduced());

        return ResponseEntity.ok(messages);
    }


    @GetMapping("un-apply-voucher")
    public ResponseEntity<Map<String, String>> unApplyVoucherForCart(Model model, HttpSession session) {
        Map<String, String> messages = new HashMap<>();

        session.removeAttribute("idVoucherApply");
        session.removeAttribute("selectedVoucher");
        session.removeAttribute("priceReduced");

        model.addAttribute("typeVoucherApply", null);
        model.addAttribute("priceReducedShow", null);
        model.addAttribute("priceReduced", null);
        model.addAttribute("finalPrice", null);
        model.addAttribute("selectedVoucher", null);


        messages.put("message", "Hủy áp dụng voucher thành công!");
        messages.put("check", "1");

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/quantity/{id}")
    public ResponseEntity<Integer> getQuantity(@PathVariable("id") Integer idProductDetail) {
        Integer quantity = clientService.getQuantityProductDetailByID(idProductDetail);
        return ResponseEntity.ok(quantity);
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Integer> requestData, Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer productDetailId = requestData.get("productDetailId");
        Integer quantity = requestData.get("quantity");
        Map<String, Object> response = new HashMap<>();

        if (quantity == null || quantity <= 0) {
            response.put("success", false);
            response.put("message", "Số lượng sản phẩm không hợp lệ.");
            return ResponseEntity.badRequest().body(response);
        }
        if (quantity != Math.floor(quantity)) {
            response.put("success", false);
            response.put("message", "Số lượng sản phẩm phải là số nguyên.");
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println("Số lượng mua: " + quantity);

        // Kiểm tra sản phẩm có tồn tại không
        ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);
        if (productDetail == null) {
            response.put("success", false);
            response.put("message", "Sản phẩm không tồn tại.");
            return ResponseEntity.badRequest().body(response);
        }

        BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetailId);
        BigDecimal originalPrice = productDetail.getPrice();

        int availableQuantity = productDetail.getQuantity();
        if (quantity > availableQuantity) {
            response.put("success", false);
            response.put("message", "Số lượng sản phẩm trong kho không đủ.");
            return ResponseEntity.badRequest().body(response);
        }

        if (clientLoginResponse != null) {
            // Xử lý giỏ hàng cho người dùng đã đăng nhập
            Integer customerId = clientLoginResponse.getId();
            Customer customer = customerRepository.findById(customerId).orElse(null);

            if (customer == null) {
                response.put("success", false);
                response.put("message", "Khách hàng không tồn tại.");
                return ResponseEntity.badRequest().body(response);
            }

            Cart existingCartItem = cartService.findByCustomerIDAndProductDetail(customerId, productDetailId);
            int currentQuantity = (existingCartItem != null) ? existingCartItem.getQuantity() : 0;
            int newTotalQuantity = currentQuantity + quantity;

            if (newTotalQuantity > 10) {
                response.put("success", false);
                response.put("message", "Không thể thêm quá 10 sản phẩm cùng loại vào giỏ hàng.");
                return ResponseEntity.badRequest().body(response);
            }

            if (existingCartItem != null) {
                existingCartItem.setQuantity(newTotalQuantity);
                existingCartItem.setUpdateDate(new Date());
                cartRepository.save(existingCartItem);
            } else {
                Cart newCartItem = new Cart();
                newCartItem.setCustomer(customer);
                newCartItem.setProductDetail(productDetail);
                newCartItem.setQuantity(quantity);
                newCartItem.setStatus(1);
                newCartItem.setCreateDate(new Date());
                cartRepository.save(newCartItem);
            }
            session.setAttribute("cartItems", getCartResponsesForCustomer(customerId));
        } else {
            // Xử lý giỏ hàng cho người dùng chưa đăng nhập
            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
            if (sessionCart == null) {
                sessionCart = new HashMap<>();
            }

            int currentQuantity = sessionCart.getOrDefault(productDetailId, 0);
            int newTotalQuantity = currentQuantity + quantity;

            if (newTotalQuantity > 10) {
                response.put("success", false);
                response.put("message", "Không thể thêm quá 10 sản phẩm cùng loại vào giỏ hàng.");
                return ResponseEntity.badRequest().body(response);
            }

            sessionCart.put(productDetailId, newTotalQuantity);
            session.setAttribute("sessionCart", sessionCart);

            List<CartResponse> cartResponses = sessionCart.entrySet().stream()
                    .map(entry -> {
                        ProductDetail detail = productDetailRepository.findById(entry.getKey()).orElse(null);
                        if (detail != null) {
                            BigDecimal sessionDiscountedPrice = clientService.findDiscountedPriceByProductDetailId(detail.getId());
                            return new CartResponse(
                                    null,
                                    null,
                                    detail.getId(),
                                    detail.getProduct().getNameProduct(),
                                    productDetail.getColor().getNameColor(),
                                    productDetail.getSize().getNameSize(),
                                    entry.getValue(),
                                    originalPrice,
                                    discountedPrice,
                                    productDetail.getProduct().getImages()
                            );
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            session.setAttribute("cartItems", cartResponses);
        }

        model.addAttribute("clientLogin", clientLoginResponse);
        response.put("success", true);
        response.put("message", "Thêm sản phẩm vào giỏ hàng thành công.");
        return ResponseEntity.ok(response);
    }


    public List<CartResponse> getCartResponsesForCustomer(Integer customerId) {
        List<Cart> cartItems = clientService.findListCartByIdCustomer(customerId);

        return cartItems.stream().map(cartItem -> {
            ProductDetail productDetail = cartItem.getProductDetail();
            BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetail.getId());
            return new CartResponse(
                    cartItem.getId(),
                    cartItem.getCustomer().getId(),
                    productDetail.getId(),
                    productDetail.getProduct().getNameProduct(),
                    productDetail.getColor().getNameColor(),
                    productDetail.getSize().getNameSize(),
                    cartItem.getQuantity(),
                    productDetail.getPrice(),
                    discountedPrice,
                    productDetail.getProduct().getImages()
            );
        }).collect(Collectors.toList());
    }

    private List<CartResponse> convertToCartResponseList(List<Cart> cartItems) {
        List<CartResponse> responses = new ArrayList<>();
        for (Cart cart : cartItems) {
            ProductDetail productDetail = cart.getProductDetail();
            BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetail.getId());
            if (discountedPrice == null) {
                discountedPrice = productDetail.getPrice();
            }
            CartResponse response = new CartResponse(
                    cart.getId(),
                    cart.getCustomer().getId(),
                    productDetail.getId(),
                    productDetail.getProduct().getNameProduct(),
                    productDetail.getColor().getNameColor(),
                    productDetail.getSize().getNameSize(),
                    cart.getQuantity(),
                    productDetail.getPrice(),
                    discountedPrice,
                    productDetail.getProduct().getImages()
            );
            responses.add(response);
        }
        return responses;
    }

    @PostMapping("/update-from-cart/{idProductDetailFromCart}")
    public ResponseEntity<Map<String, String>> updateProductDetailFromCart(
            HttpSession session,
            @PathVariable("idProductDetailFromCart") Integer idProductDetailFromCart,
            @RequestBody Map<String, Integer> request) {

        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer quantityItem = request.get("quantityItem");
        Map<String, String> messages = new HashMap<>();
        List<CartResponse> cartItemResponse = (List<CartResponse>) session.getAttribute("cartItems");
        Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
        boolean isUpdated = false;

        if (clientLoginResponse != null) {
            Integer customerID = clientLoginResponse.getId();
            Customer customer = customerService.getCustomerByID(customerID);
            if (customer == null) {
                messages.put("messages", "Khách hàng chưa đăng nhập");
                return ResponseEntity.ok(messages);
            }
            List<Cart> cartItems = clientService.findListCartByIdCustomer(customerID);
            for (Cart cart : cartItems) {
                if (cart.getProductDetail().getId().equals(idProductDetailFromCart)) {
                    if (quantityItem <= 0) {
                        messages.put("messages", "Số lượng sản phẩm phải lớn hơn 0");
                        return ResponseEntity.ok(messages);
                    }
                    if (quantityItem > 10) {
                        messages.put("messages", "Số lượng mua tối đa là 10 sản phẩm");
                        return ResponseEntity.ok(messages);
                    }

                    cart.setQuantity(quantityItem);
                    cartRepository.save(cart);
                    System.out.println("Cập nhật số lượng khi đăng nhập thành công");

                    isUpdated = true;
                    break;
                }
            }

            if (isUpdated) {
                // Cập nhật lại giỏ hàng trong session
                cartItemResponse = convertToCartResponseList(cartItems);
                session.setAttribute("cartItems", cartItemResponse);

                // Cập nhật lại tổng giá trị giỏ hàng
                BigDecimal totalPriceCartItem = calculateTotalPrice(cartItemResponse);
                session.setAttribute("totalPrice", totalPriceCartItem);

                messages.put("message", "Số lượng sản phẩm đã được cập nhật.");
                messages.put("cartItems", cartItemResponse.toString());
                messages.put("totalPrice", totalPriceCartItem.toString());
            } else {
                messages.put("messages", "Sản phẩm không được tìm thấy trong giỏ hàng.");
            }
        } else {
            if (sessionCart.containsKey(idProductDetailFromCart)) {
                if (quantityItem <= 0) {
                    messages.put("messages", "Số lượng sản phẩm phải lớn hơn 0");
                    return ResponseEntity.ok(messages);
                }
                if (quantityItem > 10) {
                    messages.put("messages", "Số lượng mua tối đa là 10 sản phẩm");
                    return ResponseEntity.ok(messages);
                }

                sessionCart.put(idProductDetailFromCart, quantityItem);
                session.setAttribute("sessionCart", sessionCart);
            }

            if (cartItemResponse == null || cartItemResponse.isEmpty()) {
                messages.put("messages", "Không có sản phẩm nào trong giỏ hàng");
                return ResponseEntity.ok(messages);
            }

            for (CartResponse item : cartItemResponse) {
                if (item.getProductDetailId().equals(idProductDetailFromCart)) {
                    item.setQuantity(quantityItem);
                    isUpdated = true;
                    break;
                }
            }

            if (isUpdated) {
                session.setAttribute("cartItems", cartItemResponse);
                messages.put("message", "Số lượng sản phẩm đã được cập nhật.");
            } else {
                messages.put("messages", "Sản phẩm không được tìm thấy trong giỏ hàng.");
            }
        }

        return ResponseEntity.ok(messages);
    }


    @PostMapping("/remove-from-cart/{idProductDetailFromCart}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteProductDetailFromCart(
            HttpSession session,
            @PathVariable("idProductDetailFromCart") Integer idProductDetailFromCart) {

        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Map<String, Object> response = new HashMap<>();

        if (clientLoginResponse != null) {
            Integer customerId = clientLoginResponse.getId();
            Customer customer = customerRepository.findById(customerId).orElse(null);
            if (customer == null) {
                response.put("message", "Khách hàng không tồn tại.");
                return ResponseEntity.badRequest().body(response);
            }

            List<Cart> cartItems = cartService.getCartItemsForCustomer(customerId);

            boolean removed = cartItems.removeIf(cart -> cart.getProductDetail().getId().equals(idProductDetailFromCart));
            if (removed) {
                clientService.deleteCartByCustomerIdAndProductDetailId(customerId, idProductDetailFromCart);
                cartItems = cartService.getCartItemsForCustomer(customerId);
                List<CartResponse> cartResponses = convertToCartResponseList(cartItems);
                session.setAttribute("cartItems", cartResponses);

                // Cập nhật tổng giá trị giỏ hàng
                BigDecimal totalPriceCartItem = calculateTotalPrice(cartResponses);
                session.setAttribute("totalPrice", totalPriceCartItem);

                response.put("message", "Sản phẩm đã được xóa khỏi giỏ hàng.");
                response.put("cartItems", cartResponses);
                response.put("totalPrice", totalPriceCartItem.toString());
            } else {
                response.put("message", "Không tìm thấy sản phẩm trong giỏ hàng.");
            }
        } else {
            // Người dùng chưa đăng nhập
            List<CartResponse> cartItemsResponse = (List<CartResponse>) session.getAttribute("cartItems");
            if (cartItemsResponse != null) {
                cartItemsResponse.removeIf(item -> item.getProductDetailId().equals(idProductDetailFromCart));

                BigDecimal totalPriceCartItem = calculateTotalPrice(cartItemsResponse);
                response.put("totalPrice", totalPriceCartItem.toString());
                response.put("cartItems", cartItemsResponse);

                Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
                if (sessionCart != null) {
                    sessionCart.remove(idProductDetailFromCart);
                    session.setAttribute("sessionCart", sessionCart);
                }
                session.setAttribute("cartItems", cartItemsResponse);
            } else {
                response.put("message", "Giỏ hàng trống.");
            }
        }

        return ResponseEntity.ok(response);
    }

    public BigDecimal calculateTotalPrice(List<CartResponse> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartResponse item : cartItems) {
            BigDecimal itemPrice = item.getDiscountedPrice(); // Giá của sản phẩm
            BigDecimal itemQuantity = BigDecimal.valueOf(item.getQuantity()); // Số lượng sản phẩm
            BigDecimal itemTotal = itemPrice.multiply(itemQuantity);

            totalPrice = totalPrice.add(itemTotal);
        }

        return totalPrice;
    }

    @PostMapping("/new-address-customer")
    public ResponseEntity<Map<String, String>> createNewAddressForCustomer(HttpSession session,
                                                                           @RequestBody AddressForCustomerRequest addressForCustomerRequest) {
        String addressForCustomer = String.valueOf(addressForCustomerRequest.getAddressCustomer());
        Map<String, String> response = new HashMap<>();
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer idCustomerLogin = clientLoginResponse.getId();
        if (idCustomerLogin != null) {
            AddressShip addressShip = new AddressShip();
            Customer customer = customerService.getCustomerByID(idCustomerLogin);
            addressShip.setCustomer(customer);
            addressShip.setSpecificAddress(addressForCustomer);
            addressShip.setCreateDate(new Date());
            addressShip.setUpdateDate(new Date());
            addressShip.setStatus(1);
            addressShipRepository.save(addressShip);
            response.put("message", "Thêm địa chỉ mới thành công");
            response.put("check", "1");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Thêm địa chỉ mới thất bại");
        response.put("check", "3");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/edit-address-customer/{idAddress}")
    public ResponseEntity<AddressShip> getAddressDetails(@PathVariable("idAddress") Integer idAddress) {
        AddressShip addressShip = addressShipRepository.findById(idAddress).orElse(null);
        if (addressShip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(addressShip);
    }

    @PostMapping("/update-address-customer/{idAddress}")
    public ResponseEntity<Map<String, String>> updateAddressForCustomer(HttpSession session,
                                                                        @PathVariable("idAddress") Integer idAddress,
                                                                        @RequestBody AddressForCustomerRequest addressForCustomerRequest) {
        Map<String, String> response = new HashMap<>();
        String addressForCustomer = String.valueOf(addressForCustomerRequest.getAddressCustomer());
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer idCustomerLogin = clientLoginResponse.getId();
        if (idCustomerLogin != null) {
            AddressShip addressShip = addressShipRepository.findById(idAddress).orElse(null);

            if (addressShip == null) {
                response.put("message", "Địa chỉ không được để trống");
                response.put("check", "2");
                return ResponseEntity.ok(response);
            }

            Customer customer = customerService.getCustomerByID(idCustomerLogin);

            if (customer == null) {
                response.put("message", "Khách hàng không tồn tại");
                response.put("check", "2");
                return ResponseEntity.ok(response);
            }
            addressShip.setCustomer(customer);
            addressShip.setSpecificAddress(addressForCustomer);
            addressShip.setCreateDate(addressShip.getCreateDate());
            addressShip.setUpdateDate(new Date());
            addressShip.setStatus(1);

            addressShipRepository.save(addressShip);
            System.out.println("Cập nhật địa chỉ thành công");
            response.put("message", "Cập nhật địa chỉ thành công");
            response.put("check", "1");
            return ResponseEntity.ok(response);
        }
        System.out.println("Cập nhật địa chỉ thất bại");
        response.put("message", "Bạn cần đăng nhập để cập nhật địa chỉ");
        response.put("check", "2");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/delete/address-customer/{idAddress}")
    public ResponseEntity<Map<String, String>> deleteAddressForCustomer(@PathVariable("idAddress") Integer idAddress) {
        Map<String, String> messages = new HashMap<>();
        addressShipRepository.deleteById(idAddress);
        messages.put("message", "Xóa địa chỉ giao hàng thành công!");
        messages.put("check", "1");
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/list-address-for-customer")
    public List<AddressShipReponse> getListAddressShipForCustomer(HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse == null) {
            return null;
        }
        List<AddressShip> addressList = clientService.getListAddressShipByIDCustomer(clientLoginResponse.getId());

        List<AddressShipReponse> responseListAddress = new ArrayList<>();
        for (AddressShip address : addressList) {
            String specificAddress = address.getSpecificAddress();
            if (specificAddress != null) {
                String[] parts = specificAddress.split(",");
                // Khai báo các thành phần địa chỉ
                String shipProvince = "", shipDistrict = "", shipWard = "", detailedAddress = "";
                String fullName = "", phoneNumber = "", mail = "";
                if (parts.length >= 7) {
                    // Gán giá trị từ các phần tử mảng
                    fullName = parts[0].trim();
                    phoneNumber = parts[1].trim();
                    mail = parts[2].trim();
                    shipProvince = parts[3].trim();
                    shipDistrict = parts[4].trim();
                    shipWard = parts[5].trim();
                    detailedAddress = String.join(", ", Arrays.copyOfRange(parts, 6, parts.length)).trim();
                } else {
                    // Xử lý nếu không đủ định dạng
                    shipProvince = "UnknownProvince";
                    shipDistrict = "UnknownDistrict";
                    shipWard = "UnknownWard";
                    detailedAddress = specificAddress.trim();
                }
                // Tạo chuỗi địa chỉ hoàn chỉnh
                String nameAndPhoneNumber = fullName + ", " + phoneNumber.trim() + ", " + mail;
                String formattedShipAddress = String.join(", ", shipProvince, shipDistrict, shipWard, detailedAddress).replaceAll(", $", "");

                // Đếm số dấu phẩy để kiểm tra
                long commaCount = formattedShipAddress.chars().filter(ch -> ch == ',').count();

                if (commaCount > 3) {
                    responseListAddress.add(new AddressShipReponse(
                            address.getId(),
                            nameAndPhoneNumber,
                            formattedShipAddress,
                            detailedAddress,
                            specificAddress
                    ));
                    System.out.println("Formatted Ship Address: " + formattedShipAddress);
                }
            }
        }
        return responseListAddress;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // danh sach hoa don cua khach hang
    protected SearchBillByStatusRequest searchBillByStatusRequest;
    protected String keyBillmanage = "";
    protected Date keyStartDate;
    protected Date keyEndDate;

    @GetMapping("/list-bill-client/{page}")
    public List<BillResponseManage> getAllBillDistStatus0(@PathVariable("page") String page, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, 5);
        if (searchBillByStatusRequest == null) {
            searchBillByStatusRequest = new SearchBillByStatusRequest(null);
        }
        System.out.println(searchBillByStatusRequest.getStatusSearch());
        return this.clientService.getAllBillByStatusDiss0(clientLoginResponse.getId(), keyBillmanage, searchBillByStatusRequest, keyStartDate, keyEndDate, pageable).getContent();
    }

    @GetMapping("/list-bill-max-page")
    public Integer getMaxPageBillManage(HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (searchBillByStatusRequest == null) {
            searchBillByStatusRequest = new SearchBillByStatusRequest();
        }

        Integer page = (int) Math.ceil((double) this.clientService.getAllBillByStatusDiss0(clientLoginResponse.getId(), keyBillmanage, searchBillByStatusRequest, keyStartDate, keyEndDate).size() / 5);
        System.out.println("so trang toi da cua quan ly hoa don " + page);
        return page;
    }

    @PostMapping("/status-bill-client")
    public ResponseEntity<?> getClickStatusBill(@RequestBody SearchBillByStatusRequest status, HttpSession session) {
        System.out.println(status.toString());
        this.searchBillByStatusRequest = status;
        return ResponseEntity.ok("done");
    }

    @PostMapping("/bill-client-search")
    public ResponseEntity<?> getSearchBillManage(@RequestBody Map<String, String> billSearch, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if (staffLogin.getStatus() != 1) {
            return null;
        }
        String keyword = billSearch.get("keywordBill");
        this.keyBillmanage = keyword;
        String startDateStr = billSearch.get("startDate");
        String endDateStr = billSearch.get("endDate");

        System.out.println("du lieu loc vc " + keyword);
        System.out.println("starDate-bill-manage: " + billSearch.get("startDate"));
        System.out.println("endDate-bill-manage: " + billSearch.get("endDate"));
        try {
            // Định dạng để parse chuỗi thành đối tượng Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            // Chuyển đổi chuỗi startDateStr thành Date và đặt thời gian bắt đầu của ngày
            Date startDate = formatter.parse(startDateStr);
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            this.keyStartDate = calendar.getTime();

            // Chuyển đổi chuỗi endDateStr thành Date và đặt thời gian kết thúc của ngày
            Date endDate = formatter.parse(endDateStr);
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            this.keyEndDate = calendar.getTime();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use 'yyyy-MM-dd'.");
        }
        return ResponseEntity.ok("done");
    }

    @GetMapping("/show-status-bill")
    public List<InvoiceStatus> getShowInvoiceStatus(HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        System.out.println("id bill tim thay la: " + idBill);
        List<InvoiceStatus> invoiceStatuses = this.invoiceStatusService.getALLInvoiceStatusByBill(idBill);
        for (InvoiceStatus invoiceStatus : invoiceStatuses) {
            System.out.println(invoiceStatus.toString());
        }
        return invoiceStatuses;
    }

    @GetMapping("/show-product-buy-status-bill/{pageNumber}")
    public List<Object[]> getShowProductBuyStatusBill(@PathVariable("pageNumber") String page, HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        try {
            Integer pageNumber = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            return null;
        }
        System.out.println("id bill tim thay la: " + idBill);
        Pageable pageable = PageRequest.of((Integer.parseInt(page) - 1), 2);
        List<Object[]> listProductBuy = this.invoiceStatusService.getAllProductBuyClient(idBill);
        return convertListToPage(listProductBuy, pageable).getContent();
    }

    @GetMapping("/max-page-bill-status")
    public Integer getMaxPageBillStatus(HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        List<Object[]> listProductBuy = this.invoiceStatusService.getAllProductBuyClient(idBill);
        Integer pageNumber = (int) Math.ceil((double) listProductBuy.size() / 2);
        return pageNumber;
    }

    @GetMapping("/show-total-status-bill")
    public Object[] getTotalStatusBill(HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        List<Object[]> bill = this.invoiceStatusService.getBillClient(idBill);
        return bill.get(0);
    }

    @GetMapping("/show-information-status-bill")
    public Object[] getInformationStatusBill(HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        List<Object[]> bill = this.invoiceStatusService.getInformationBillStatusClient(idBill);
        return bill.get(0);
    }

    @PostMapping("/send-mail-request-bill")
    public String getSendMailRequestBill(HttpSession session, @RequestBody Map<String, String> data) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        String emailSend = data.get("emailSend"); // Lấy giá trị từ JSON
        System.out.println(emailSend);
        Bill bill = this.billService.findById(idBill).orElse(null);
        String ht = "http://localhost:8080/api-client/bill-pdf/" + bill.getId();
        this.templateRequestBill(emailSend, ht, bill.getCodeBill());
        return "Đã gửi yêu cầu!";
    }

    @GetMapping("/bill-pdf/{idBill}")
    public ResponseEntity<byte[]> getBillPDF(@PathVariable("idBill") String idBill, HttpSession session) throws Exception {

        // Lấy chi tiết hóa đơn và thông tin hóa đơn từ service
        List<Object[]> billDetails = this.billService.getBillDetailByIdBillPDF(Integer.parseInt(idBill));
        List<Object[]> billInfoList = this.billService.getBillByIdCreatePDF(Integer.parseInt(idBill));

        // Kiểm tra dữ liệu
        if (billInfoList == null || billDetails.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        Object[] billInfo = billInfoList.get(0);

        // Tạo PDF từ template
        ByteArrayOutputStream pdfStream = pdfTemplateService.fillPdfTemplate(billInfo, billDetails);
        byte[] pdfBytes = pdfStream.toByteArray();

        // Trả về phản hồi thành công
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Chỉ định rằng file sẽ được tải về và lưu vào thư mục Downloads
        headers.setContentDispositionFormData("attachment", billInfo[0] + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }


}