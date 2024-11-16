package com.example.shopgiayonepoly.restController.client;

import com.example.shopgiayonepoly.baseMethod.BaseEmail;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    protected PdfTemplateService pdfTemplateService;
    String messages = "";

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
    public VoucherClientResponse VoucherResponseByID(@PathVariable("id") Integer idVoucher, HttpSession session) {
        VoucherClientResponse selectedVoucher = clientService.findVoucherApplyByID(idVoucher);

        if (selectedVoucher == null) {
            System.out.println("Voucher không tồn tại");
            return null;
        }

        // Lưu voucher vào session
        session.setAttribute("selectedVoucher", selectedVoucher);
        System.out.println("Voucher đã chọn: " + selectedVoucher); // Log voucher đã lưu vào session

        return selectedVoucher;
    }


    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Integer> requestData, Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer productDetailId = requestData.get("productDetailId");
        Integer quantity = requestData.get("quantity");
        Map<String, Object> response = new HashMap<>();

        // Kiểm tra số lượng sản phẩm hợp lệ
        if (quantity == null || quantity <= 0) {
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

        BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetailId);
        BigDecimal originalPrice = productDetail.getPrice(); // Giá gốc

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
                                    originalPrice, // Giá gốc
                                    discountedPrice // Giá giảm
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
                    cartItem.getId(), // ID của mục giỏ hàng
                    cartItem.getCustomer().getId(), // ID khách hàng
                    productDetail.getId(), // ID sản phẩm chi tiết
                    productDetail.getProduct().getNameProduct(), // Tên sản phẩm
                    productDetail.getColor().getNameColor(),
                    productDetail.getSize().getNameSize(),
                    cartItem.getQuantity(), // Số lượng
                    productDetail.getPrice(), // Giá gốc
                    discountedPrice // Giá sau giảm
            );
        }).collect(Collectors.toList());
    }


    @PostMapping("/update-from-cart/{idProductDetailFromCart}")
    public ResponseEntity<Map<String, String>> updateProductDetailFromCart(
            HttpSession session,
            @PathVariable("idProductDetailFromCart") Integer idProductDetailFromCart,
            @RequestParam("quantityItem") Integer quantityFormCart) {

        Map<String, String> messages = new HashMap<>();
        List<CartResponse> cartItems = (List<CartResponse>) session.getAttribute("cartItems");

        // Kiểm tra nếu giỏ hàng trống
        if (cartItems == null || cartItems.isEmpty()) {
            messages.put("messages", "Không có sản phẩm nào trong giỏ hàng");
            return ResponseEntity.ok(messages);
        }

        boolean isUpdated = false;

        // Tìm và cập nhật sản phẩm trong giỏ hàng
        for (CartResponse item : cartItems) {
            if (item.getProductDetailId().equals(idProductDetailFromCart)) {
                // Kiểm tra số lượng hợp lệ
                if (quantityFormCart <= 0) {
                    messages.put("messages", "Số lượng sản phẩm phải lớn hơn 0");
                    return ResponseEntity.ok(messages);
                }
                if (quantityFormCart > 10) {
                    messages.put("messages", "Số lượng mua tối đa là 10 sản phẩm");
                    return ResponseEntity.ok(messages);
                }

                // Cập nhật số lượng sản phẩm
                item.setQuantity(quantityFormCart);
                isUpdated = true;
                break;
            }
        }

        // Xử lý sau khi cập nhật
        if (isUpdated) {
            session.setAttribute("cartItems", cartItems); // Lưu lại giỏ hàng trong session
            messages.put("messages", "Số lượng sản phẩm đã được cập nhật.");
        } else {
            messages.put("messages", "Sản phẩm không được tìm thấy trong giỏ hàng.");
        }

        return ResponseEntity.ok(messages);
    }



    //////////////////////////////////////////////////////////////////////////////
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