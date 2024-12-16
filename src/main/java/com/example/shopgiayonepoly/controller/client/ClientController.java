package com.example.shopgiayonepoly.controller.client;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.dto.request.client.PaymentBillRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.repositores.*;
import com.example.shopgiayonepoly.service.*;
import com.example.shopgiayonepoly.service.attribute.CategoryService;
import com.example.shopgiayonepoly.service.attribute.ManufacturerService;
import com.example.shopgiayonepoly.service.attribute.MaterialService;
import com.example.shopgiayonepoly.service.attribute.OriginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    MaterialService materialService;

    @Autowired
    ManufacturerService manufacturerService;

    @Autowired
    OriginService originService;

    @RequestMapping
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        model.addAttribute("errorCode", statusCode);
        if (statusCode != null && statusCode == 404) {
            return "error-404";
        }
        return "client/error";
    }

    @GetMapping("/products")
    public String getFormProduct(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("loginInfoClient", clientLoginResponse);
        model.addAttribute("listProduct", clientService.getAllProduct());
        model.addAttribute("listCategory", categoryService.getCategoryNotStatus0());
        model.addAttribute("listMaterial", materialService.getMaterialNotStatus0());
        model.addAttribute("listManufacturer", manufacturerService.getManufacturerNotStatus0());
        model.addAttribute("listOrigin", originService.getOriginNotStatus0());

        return "client/product";
    }

    @PostMapping("/filter")
    @ResponseBody
    public List<ProductIClientResponse> filterProducts(@RequestBody FilterResponse filterRequest) {
        List<ProductIClientResponse> products = clientService.filterProducts(
                filterRequest.getCategories(),
                filterRequest.getManufacturers(),
                filterRequest.getMaterials(),
                filterRequest.getOrigins(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getPriceSort()
        );
        return products;
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        List<ProductIClientResponse> products = clientService.searchProducts(keyword);
        model.addAttribute("listProduct", products);
        model.addAttribute("listCategory", categoryService.getCategoryNotStatus0());
        model.addAttribute("listMaterial", materialService.getMaterialNotStatus0());
        model.addAttribute("listManufacturer", manufacturerService.getManufacturerNotStatus0());
        model.addAttribute("listOrigin", originService.getOriginNotStatus0());
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");
        model.addAttribute("cartItems", cartItems);
        return "client/product";
    }

    @GetMapping("/home")
    public String getFormHomeClient(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        List<ProductIClientResponse> listProductHighest = clientService.GetTop12ProductWithPriceHighest();
        List<ProductIClientResponse> listProductLowest = clientService.GetTop12ProductWithPriceLowest();
        model.addAttribute("listProductHighest", listProductHighest);
        model.addAttribute("listProductLowest", listProductLowest);
        model.addAttribute("clientLogin", clientLoginResponse);
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");
        model.addAttribute("cartItems", cartItems);
        return "client/homepage";
    }

    @GetMapping("/base")
    public String getFormBaseClient(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");
        model.addAttribute("cartItems", cartItems);
        return "client/base";
    }

    @GetMapping("/address")
    public String getPriceByGHN(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        return "client/address";
    }

    @GetMapping("/product-detail/{productID}")
    public String getFormProductDetail(@PathVariable("productID") Integer productId,
                                       HttpSession session,
                                       Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        List<ProductDetailClientRespone> productDetailClientRespones = clientService.findProductDetailByProductId(productId);

        Set<ColorClientResponse> uniqueColors = new HashSet<>();
        Set<SizeClientResponse> uniqueSizes = new HashSet<>();

        for (ProductDetailClientRespone detail : productDetailClientRespones) {
            uniqueColors.addAll(clientService.findDistinctColorsByProductDetailId(detail.getProductDetailId()));
            uniqueSizes.addAll(clientService.findDistinctSizesByProductDetailId(detail.getProductDetailId()));
        }

        SaleProduct saleProductNew = saleProductService.getSaleProductNew();

        model.addAttribute("productDetail", productDetailClientRespones);
        model.addAttribute("listImage", productService.findById(productId));
        model.addAttribute("colors", new ArrayList<>(uniqueColors));
        model.addAttribute("sizes", new ArrayList<>(uniqueSizes));
        model.addAttribute("productID", productId);
        model.addAttribute("clientLogin", clientLoginResponse);
        model.addAttribute("saleProductNew", saleProductNew);
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");
        model.addAttribute("cartItems", cartItems);

        return "client/product_detail";
    }


    @GetMapping("/cart")
    public String getFromCart(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        List<CartResponse> cartResponses = new ArrayList<>();
        List<Cart> cartItems = new ArrayList<>();
        if (clientLoginResponse != null) {
            Integer customerId = clientLoginResponse.getId();
            cartItems = cartService.getCartItemsForCustomer(customerId);

            for (Cart cartItem : cartItems) {
                ProductDetail productDetail = cartItem.getProductDetail();
                BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetail.getId());

                if (discountedPrice == null) {
                    discountedPrice = productDetail.getPrice();
                }

                int availableQuantity = productDetail.getQuantity();
                int cartQuantity = cartItem.getQuantity();
                if (cartQuantity > availableQuantity) {
                    cartItem.setQuantity(availableQuantity);
                    cartService.updateCartItem(cartItem);
                    cartQuantity = availableQuantity;
                }
                if (cartQuantity == 0) {
                    cartService.deleteCartItem(cartItem.getId());
                    continue;
                }

                if (cartItem.getProductDetail().getStatus() == 1) {
                    CartResponse cartResponse = new CartResponse(
                            cartItem.getId(),
                            cartItem.getCustomer().getId(),
                            productDetail.getId(),
                            productDetail.getProduct().getNameProduct(),
                            productDetail.getColor().getNameColor(),
                            productDetail.getSize().getNameSize(),
                            cartQuantity,
                            productDetail.getPrice(),
                            discountedPrice,
                            productDetail.getProduct().getImages()
                    );
                    cartResponses.add(cartResponse);
                }
            }
        } else {
            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
                    Integer productDetailId = entry.getKey();
                    Integer quantity = entry.getValue();
                    ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);
                    if (productDetail != null) {
                        BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetailId);

                        if (productDetail.getStatus() == 1) {
                            int availableQuantity = productDetail.getQuantity();
                            if (quantity > availableQuantity) {
                                quantity = availableQuantity;
                            }
                            if (quantity == 0) {
                                sessionCart.remove(productDetailId);
                                continue;
                            }
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
                                    quantity,
                                    productDetail.getPrice(),
                                    discountedPrice,
                                    productDetail.getProduct().getImages()
                            );
                            cartResponses.add(cartResponse);
                        }
                    }
                }
            }
        }

        BigDecimal totalPriceCartItem = BigDecimal.ZERO;
        for (CartResponse item : cartResponses) {
            BigDecimal finalPrice = item.getDiscountedPrice();
            BigDecimal itemTotalPrice = finalPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPriceCartItem = totalPriceCartItem.add(itemTotalPrice);
        }
        session.setAttribute("totalPrice", totalPriceCartItem);
        session.setAttribute("cartItems", cartResponses);
        model.addAttribute("clientLogin", clientLoginResponse);
        model.addAttribute("cartItems", cartResponses);
        model.addAttribute("totalPrice", totalPriceCartItem);

        List<Voucher> applicableVouchers = voucherService.findApplicableVouchers(totalPriceCartItem);
        model.addAttribute("applicableVouchers", applicableVouchers);

        VoucherClientResponse selectedVoucher = (VoucherClientResponse) session.getAttribute("selectedVoucher");
        BigDecimal priceReduced = BigDecimal.ZERO;
        if (selectedVoucher != null) {
            Voucher freshVoucher = voucherRepository.findById(selectedVoucher.getId())
                    .orElse(null);
            if (freshVoucher == null || freshVoucher.getStatus() != 1) {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);

            }
            if (freshVoucher == null || freshVoucher.getQuantity() <= 0) {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);
            }

            if (freshVoucher == null || freshVoucher.getPriceReduced().compareTo(BigDecimal.ZERO) < 0) {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);
            }

            BigDecimal minValueApply = selectedVoucher.getApplyPrice();
            if (totalPriceCartItem.compareTo(minValueApply) >= 0) {
                model.addAttribute("selectedVoucher", selectedVoucher);
                Integer voucherType = selectedVoucher.getVoucherType();
                Integer idVoucherApply = selectedVoucher.getId();
                BigDecimal discountValue = selectedVoucher.getPriceReduced();

                if (voucherType == 1) {
                    priceReduced = totalPriceCartItem.multiply(discountValue.divide(BigDecimal.valueOf(100)));
                } else if (voucherType == 2) {
                    priceReduced = discountValue;
                }

                BigDecimal finalPrice = totalPriceCartItem.subtract(priceReduced);
                model.addAttribute("finalPrice", finalPrice);
                session.setAttribute("priceReduced", priceReduced);
                model.addAttribute("priceReducedShow", priceReduced);
                model.addAttribute("typeVoucherApply", selectedVoucher.getVoucherType());
                model.addAttribute("applyPrice", selectedVoucher.getApplyPrice());
                session.setAttribute("idVoucherApply", idVoucherApply);
            } else {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);
            }
        }

        return "client/cart";
    }

    @GetMapping("/payment")
    public String getFormPayment(HttpSession session, Model model) {
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
            BigDecimal price = c.getDiscountedPrice() != null ? c.getDiscountedPrice() : c.getOriginalPrice();
            int quantity = c.getQuantity();
            BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));
            calculatedTotalPrice = calculatedTotalPrice.add(totalAmount);
            ProductDetail productDetail = productDetailRepository.findById(c.getProductDetailId()).get();
            if (productDetail != null) {
                Double itemWeight = quantity * productDetail.getWeight();
                weight += itemWeight;
            }
        }

        if (selectedVoucher != null) {
            Voucher freshVoucher = voucherRepository.findById(selectedVoucher.getId())
                    .orElse(null);
            if (freshVoucher == null || freshVoucher.getStatus() != 1) {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);
            }
            if (freshVoucher == null || freshVoucher.getQuantity() <= 0) {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);
            }

            if (freshVoucher == null || freshVoucher.getPriceReduced().compareTo(BigDecimal.ZERO) < 0) {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);
            }
            BigDecimal minValueApply = selectedVoucher.getApplyPrice();
            if (calculatedTotalPrice.compareTo(minValueApply) < 0) {
                session.removeAttribute("idVoucherApply");
                session.removeAttribute("selectedVoucher");
                session.removeAttribute("priceReduced");

                model.addAttribute("typeVoucherApply", null);
                model.addAttribute("priceReducedShow", null);
                model.addAttribute("priceReduced", null);
                model.addAttribute("finalPrice", null);
                model.addAttribute("selectedVoucher", null);
            } else {
                model.addAttribute("selectedVoucher", selectedVoucher);
                model.addAttribute("priceReducedShow", priceReduced);
            }
        }

        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");

        if (clientLoginResponse != null) {
            model.addAttribute("clientLogin", clientLoginResponse);
            String addressCustomerLogin = clientLoginResponse.getAddRess();
            String[] addressParts = addressCustomerLogin.split(",", 4);

            String idWard = addressParts.length > 0 ? addressParts[0].trim() : "";
            String idDistrict = addressParts.length > 1 ? addressParts[1].trim() : "";
            String idProvince = addressParts.length > 2 ? addressParts[2].trim() : "";
            String originalAddress = addressParts.length > 3 ? addressParts[3].trim() : "";

            String infoCustomer = clientLoginResponse.getFullName() + ", " + clientLoginResponse.getNumberPhone() + ", " + clientLoginResponse.getEmail();
            String fullAddressCustomerLogin = infoCustomer.trim() + ", " + idProvince.trim() + "," + idDistrict.trim() + "," + idWard + "," + originalAddress.trim();
            model.addAttribute("infoCustomer", infoCustomer);
            model.addAttribute("originalAddress", originalAddress);
            model.addAttribute("fullAddressCustomerLogin", fullAddressCustomerLogin);
            model.addAttribute("IdWard", idWard);
            model.addAttribute("IdDistrict", idDistrict);
            model.addAttribute("IdProvince", idProvince);

            List<AddressShip> addressList = clientService.getListAddressShipByIDCustomer(clientLoginResponse.getId());
            List<AddressShipReponse> responseListAddress = new ArrayList<>();
            for (AddressShip address : addressList) {
                String specificAddress = address.getSpecificAddress();
                if (specificAddress != null) {
                    String[] parts = specificAddress.split(",");
                    String shipProvince = "", shipDistrict = "", shipWard = "", detailedAddress = "";
                    String fullName = "", phoneNumber = "", mail = "";
                    if (parts.length >= 7) {
                        fullName = parts[0].trim();
                        phoneNumber = parts[1].trim();
                        mail = parts[2].trim();
                        shipProvince = parts[3].trim();
                        shipDistrict = parts[4].trim();
                        shipWard = parts[5].trim();
                        detailedAddress = String.join(", ", Arrays.copyOfRange(parts, 6, parts.length)).trim();
                    } else {
                        shipProvince = "UnknownProvince";
                        shipDistrict = "UnknownDistrict";
                        shipWard = "UnknownWard";
                        detailedAddress = specificAddress.trim();
                    }
                    String nameAndPhoneNumber = fullName + ", " + phoneNumber.trim() + ", " + mail;
                    String formattedShipAddress = String.join(", ", shipProvince, shipDistrict, shipWard, detailedAddress).replaceAll(", $", "");

                    long commaCount = formattedShipAddress.chars().filter(ch -> ch == ',').count();

                    if (commaCount > 3) {
                        responseListAddress.add(new AddressShipReponse(
                                address.getId(),
                                nameAndPhoneNumber,
                                formattedShipAddress,
                                detailedAddress,
                                specificAddress
                        ));
                    }
                }
            }
            model.addAttribute("listAddress", responseListAddress);
        }
        if (calculatedTotalPrice.compareTo(BigDecimal.ZERO) < 0) {
            calculatedTotalPrice = BigDecimal.ZERO;
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("weight", weight);
        model.addAttribute("totalPrice", calculatedTotalPrice);
        model.addAttribute("priceReducedShow", priceReduced);
        session.setAttribute("idVoucherApply", idVoucherApply);
        session.setAttribute("priceReduced", priceReduced);
        session.setAttribute("selectedVoucher", selectedVoucher);

        if (clientLoginResponse != null) {
            model.addAttribute("accountLogin", clientLoginResponse.getAcount());
        }

        return "client/bill_payment";
    }


    @PostMapping("/payment")
    @ResponseBody
    public String payBill(
            HttpSession session,
            Model model,
            @RequestBody PaymentBillRequest paymentRequest,
            HttpServletRequest request) {
        String address = paymentRequest.getAddressShip();
        BigDecimal shippingPrice = paymentRequest.getShippingPrice();
        BigDecimal totalAmountBill = paymentRequest.getTotalAmountBill();
        BigDecimal priceVoucher = paymentRequest.getPriceVoucher();
        String noteBill = paymentRequest.getNoteBill();
        Integer payMethod = paymentRequest.getPayMethod();
        Integer idVoucherApply = (Integer) session.getAttribute("idVoucherApply");
        BigDecimal priceReduced = (BigDecimal) session.getAttribute("priceReduced");
        String errorAddress = "";
        String errorTotalAmountBill = "";
        String errorPayMethod = "";

        if (address == null || address.trim().isEmpty()) {
            errorAddress = "* Địa chỉ không được để trống.";
        }

        if (noteBill == null || noteBill.trim().isEmpty()) {
            noteBill = "Đặt hàng";
        }

        if (totalAmountBill == null || totalAmountBill.compareTo(BigDecimal.ZERO) <= 0) {
            errorTotalAmountBill = "* Tổng tiền hóa đơn không hợp lệ.";
        }

        List<Integer> validPayMethods = Arrays.asList(1, 2);
        if (payMethod == null || !validPayMethods.contains(payMethod)) {
            errorPayMethod = "* Bạn cần chọn một phương thức thanh toán hợp lệ.";
        }

        if (payMethod != null) {
            if (payMethod == 1) {
                if (totalAmountBill.compareTo(BigDecimal.valueOf(100000000)) > 0) {
                    errorTotalAmountBill = "* Tổng tiền phải nhỏ hơn 100 triệu cho phương thức COD.";
                }
            } else if (payMethod == 2) {
                if (totalAmountBill.compareTo(BigDecimal.valueOf(20000000)) > 0) {
                    errorTotalAmountBill = "* Tổng tiền phải nhỏ hơn 20 triệu cho phương thức VNPAY.";
                }
            } else {
                errorTotalAmountBill = "* Phương thức thanh toán không hợp lệ.";
            }
        } else {
            errorTotalAmountBill = "* Bạn cần chọn một phương thức thanh toán hợp lệ.";
        }

        if (priceReduced == null) {
            priceReduced = BigDecimal.ZERO;
        }
        if (totalAmountBill.compareTo(BigDecimal.ZERO) < 0) {
            totalAmountBill = BigDecimal.ZERO;
        }


        VoucherClientResponse voucherApply = (VoucherClientResponse) session.getAttribute("selectedVoucher");
        Voucher voucher = null;
        if (idVoucherApply != null) {
            voucher = voucherRepository.findById(idVoucherApply).orElse(null);
        }
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer customerId = null;
        if (clientLoginResponse != null) {
            customerId = clientLoginResponse.getId();
        }
        Bill bill = new Bill();
        List<BillDetail> billDetails = new ArrayList<>();
        if (clientLoginResponse != null) {
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
            bill.setAddRess(address);
            bill.setCustomer(customer);
            bill.setShippingPrice(shippingPrice);
            bill.setTotalAmount(totalAmountBill);
            bill.setPaymentMethod(payMethod);
            bill.setBillType(2);
            bill.setPaymentStatus(0);
            bill.setNote(noteBill);
            bill.setVoucher(voucher);
            bill.setPriceDiscount(priceReduced);
            bill.setStatus(1);
            bill.setCreateDate(new Date());
            bill.setUpdateDate(new Date());
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
                        billDetail.setPrice(getPriceAfterDiscount(productDetail));
                        BigDecimal totalAmount = getPriceAfterDiscount(productDetail).multiply(BigDecimal.valueOf(quantity));
                        billDetail.setTotalAmount(totalAmount);
                        billDetail.setStatus(1);
                        billDetail.setCreateDate(new Date());
                        billDetail.setUpdateDate(new Date());
                        billDetails.add(billDetail);
                    }
                }
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

        if (!errorAddress.isEmpty() || !errorTotalAmountBill.isEmpty() || !errorPayMethod.isEmpty()) {
            model.addAttribute("errorAddress", errorAddress);
            model.addAttribute("errorTotalAmountBill", errorTotalAmountBill);
            model.addAttribute("errorPayMethod", errorPayMethod);
            removeSesion(session, model);
            return "client/bill_payment";
        }

        bill.setStatus(1);
        billRepository.save(bill);
        bill.setCodeBill("HD" + bill.getId());
        billRepository.save(bill);
        for (BillDetail billDetail : billDetails) {
            billDetail.setBill(bill);
        }
        billDetailRepository.saveAll(billDetails);
        List<Cart> cartItemsForCustomer = cartService.getCartItemsForCustomer(customerId);
        for (Cart cart : cartItemsForCustomer) {
            if (cart.getCustomer().getId() == customerId) {
                cartService.deleteCartByCustomerID(customerId);
            }
        }
        if (bill.getPaymentMethod() == 2) {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            BigDecimal totalPriceFinal = bill.getTotalAmount().subtract(bill.getPriceDiscount()).add(bill.getShippingPrice());
            Integer priceAsInteger = totalPriceFinal.setScale(0, RoundingMode.DOWN).intValue();
            String vnpayUrl = vnPayService.createOrder((priceAsInteger), "Chuyển khoản", baseUrl);
            session.setAttribute("pageReturn", 3);
            session.setAttribute("payBillOrder", bill);
            return vnpayUrl;
        }
        String[] parts = address.split(",");
        String mailSend = parts.length > 2 ? parts[2].trim() : "Không có mail";
        String host = "http://localhost:8080/onepoly/status-bill/" + bill.getId();
        this.setBillStatus(bill.getId(), 0, session);
        this.setBillStatus(bill.getId(), bill.getStatus(), session);
        this.templateCreateBillClient(mailSend, host, bill.getCodeBill());
        session.setAttribute("codeOrder", bill.getCodeBill());
        session.setAttribute("hostSuccess", host);
        return "/onepoly/order-success";
    }

    @GetMapping("/order-success")
    public String getFormOderSuccess(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        String codeOrder = (String) session.getAttribute("codeOrder");
        String hostSuccess = (String) session.getAttribute("hostSuccess");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        model.addAttribute("codeOrder", codeOrder);
        model.addAttribute("hostSuccess", hostSuccess);
        removeSesion(session, model);
        return "client/order-success";
    }

    public void removeSesion(HttpSession session, Model model) {
        if (session != null) {
            if (session.getAttribute("idVoucherApply") != null) {
                session.removeAttribute("idVoucherApply");
            }
            if (session.getAttribute("selectedVoucher") != null) {
                session.removeAttribute("selectedVoucher");
            }
            if (session.getAttribute("priceReduced") != null) {
                session.removeAttribute("priceReduced");
            }
            if (session.getAttribute("cartItems") != null) {
                session.removeAttribute("cartItems");
            }
            if (session.getAttribute("totalPrice") != null) {
                session.removeAttribute("totalPrice");
            }
            if (session.getAttribute("sessionCart") != null) {
                session.removeAttribute("sessionCart");
            }
        }
        if (model.getAttribute("typeVoucherApply") != null) {
            model.addAttribute("typeVoucherApply", null);
        }
        if (model.getAttribute("priceReducedShow") != null) {
            model.addAttribute("priceReducedShow", null);
        }
        if (model.getAttribute("priceReduced") != null) {
            model.addAttribute("priceReduced", null);
        }
        if (model.getAttribute("finalPrice") != null) {
            model.addAttribute("finalPrice", null);
        }
        if (model.getAttribute("selectedVoucher") != null) {
            model.addAttribute("selectedVoucher", null);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/listBillByClient/{id}")
    public String getFormListBillByClient(HttpSession session, Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        removeSesion(session, model);
        return "client/listBillByClient";
    }

    @GetMapping("/policy-exchange-return-bill")
    public String getFormPolicyExchangeReturnBill(Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        return "client/policyExchangeReturnBill";
    }

    @GetMapping("/search-bill-by-code-bill")
    public String getFormSearchBill(Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        return "client/searchBillNotLogin";
    }

    @PostMapping("/search-bill")
    public String getSearchBill(
            @RequestParam(name = "codeBill") String codeBill,
            @RequestParam(name = "emailBill") String emailBill,
            Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        model.addAttribute("clientLogin", clientLoginResponse);
        Bill billSearch = this.billRepository.getBillByCodeBill(codeBill);
        if (billSearch != null) {
            String[] part = billSearch.getAddRess().split(",\\s*");
            String emailCheck = part[2];
            if (!emailCheck.equals(emailBill)) {
                model.addAttribute("error", "Không tìm thấy hóa đơn!");
                return "client/searchBillNotLogin";
            }
            return "redirect:/onepoly/status-bill/" + billSearch.getId();
        } else {
            model.addAttribute("error", "Không tìm thấy hóa đơn!");
            return "client/searchBillNotLogin";
        }
    }

    @GetMapping("/status-bill/{id}")
    public String getFormStatusBill(@PathVariable("id") String idBill, Model model, HttpSession session) {
        System.out.println("id bill ben controller: " + idBill);
        try {
            ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
            model.addAttribute("clientLogin", clientLoginResponse);
            Integer idInteger = Integer.parseInt(idBill);
            session.setAttribute("idCheckStatusBill", idInteger);
            System.out.println("id bill ben controller: " + idInteger);
            return "client/statusBillClient";
        } catch (NumberFormatException e) {
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
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        String codeOrder = (String) session.getAttribute("codeOrder");
        String hostSuccess = (String) session.getAttribute("hostSuccess");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        model.addAttribute("codeOrder", codeOrder);
        model.addAttribute("hostSuccess", hostSuccess);
        removeSesion(session, model);
        return "client/homepage";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session, Model model) {

        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("usernameError", "*Tên tài khoản không được để trống");
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("passwordError", "*Mật khẩu không được để trống");
        }

        if (model.containsAttribute("usernameError") || model.containsAttribute("passwordError")) {
            model.addAttribute("usernameLogin", username);
            return "login/loginClient";
        }

        ClientLoginResponse clientLoginResponse = this.clientLoginResponse.getCustomerByEmailAndAcount(username, username);
        if (clientLoginResponse != null && passwordEncoder.matches(password, passwordEncoder.encode(clientLoginResponse.getPassword()))) {
            session.setAttribute("clientLogin", clientLoginResponse);
            session.setMaxInactiveInterval(24 * 60 * 60);
            System.out.println(clientLoginResponse.toString());
            String codeOrder = (String) session.getAttribute("codeOrder");
            String hostSuccess = (String) session.getAttribute("hostSuccess");
            model.addAttribute("loginInfoClient", clientLoginResponse);
            model.addAttribute("codeOrder", codeOrder);
            model.addAttribute("hostSuccess", hostSuccess);
            removeSesion(session, model);
            return "redirect:/onepoly/home";
        } else {
            String codeOrder = (String) session.getAttribute("codeOrder");
            String hostSuccess = (String) session.getAttribute("hostSuccess");
            model.addAttribute("loginInfoClient", clientLoginResponse);
            model.addAttribute("codeOrder", codeOrder);
            model.addAttribute("hostSuccess", hostSuccess);
            removeSesion(session, model);
            model.addAttribute("usernameLogin", username);
            model.addAttribute("errorMessage", "*Sai tên tài khoản hoặc mật khẩu");
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
        model.addAttribute("registerRequest", registerRequest);
        model.addAttribute("errorMessage", session.getAttribute("errorMessage"));
        session.removeAttribute("acount");
        session.removeAttribute("email");
        session.removeAttribute("errorMessage");

        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        String codeOrder = (String) session.getAttribute("codeOrder");
        String hostSuccess = (String) session.getAttribute("hostSuccess");
        model.addAttribute("loginInfoClient", clientLoginResponse);
        model.addAttribute("codeOrder", codeOrder);
        model.addAttribute("hostSuccess", hostSuccess);
        removeSesion(session, model);
        return "client/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") @Valid RegisterRequest registerRequest,
                           BindingResult bindingResult, Model model, HttpSession session) {
        session.setAttribute("acount", registerRequest.getAcount());
        session.setAttribute("email", registerRequest.getEmail());

        if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "error.registerRequest", "*Email không được để trống");
        } else if (!registerRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            bindingResult.rejectValue("email", "error.registerRequest", "*Email không hợp lệ");
        } else if (registerRequest.getEmail().length() > 100) {
            bindingResult.rejectValue("email", "error.registerRequest", "*Email không vượt quá 100 ký tự");
        }

        if (registerRequest.getAcount() == null || registerRequest.getAcount().trim().isEmpty()) {
            bindingResult.rejectValue("acount", "error.registerRequest", "*Tên tài khoản không được để trống");
        } else if (registerRequest.getAcount().length() > 100) {
            bindingResult.rejectValue("acount", "error.registerRequest", "*Tên tài khoản không vượt quá 100 ký tự");
        }

        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "error.registerRequest", "*Mật khẩu không được để trống");
        } else if (registerRequest.getPassword().length() > 100) {
            bindingResult.rejectValue("password", "error.registerRequest", "*Mật khẩu không vượt quá 100 ký tự");
        }

        if (registerRequest.getConfirmPassword() == null || registerRequest.getConfirmPassword().trim().isEmpty()) {
            bindingResult.rejectValue("confirmPassword", "error.registerRequest", "*Xác nhận mật khẩu không được để trống");
        } else if (!registerRequest.getConfirmPassword().equals(registerRequest.getPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.registerRequest", "*Xác nhận mật khẩu không khớp với mật khẩu");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("registerRequest", registerRequest);
            return "client/register";
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

}
