package com.example.shopgiayonepoly.restController.client;

import com.example.shopgiayonepoly.baseMethod.BaseEmail;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.client.CartItemResponse;
import com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone;
import com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse;
import com.example.shopgiayonepoly.dto.response.client.VoucherClientResponse;
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
        System.out.println(selectedVoucher.toString());

        return selectedVoucher;
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Integer> requestData, Model model, HttpSession session) {

        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        Integer productDetailId = requestData.get("productDetailId");
        Integer quantity = requestData.get("quantity");
        String message;
        Map<String, Object> response = new HashMap<>();

        // Kiểm tra số lượng sản phẩm hợp lệ
        if (quantity <= 0) {
            response.put("success", false);
            response.put("message", "Số lượng sản phẩm không hợp lệ.");
            return ResponseEntity.badRequest().body(response);
        }

        // Kiểm tra sản phẩm có tồn tại không
        ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(new ProductDetail());
        if (productDetail == null) {
            response.put("success", false);
            response.put("message", "Sản phẩm không tồn tại.");
            return ResponseEntity.badRequest().body(response);
        }

        BigDecimal finalPrice = clientService.findDiscountedPriceByProductDetailId(productDetailId);
        productDetail.setPrice(finalPrice);

        if (clientLoginResponse != null) {
            // Người dùng đã đăng nhập, kiểm tra giỏ hàng trong cơ sở dữ liệu
            Integer customerId = clientLoginResponse.getId();
            Customer customer = customerRepository.findById(customerId).orElse(null);

            if (customer == null) {
                response.put("success", false);
                response.put("message", "Khách hàng không tồn tại.");
                return ResponseEntity.badRequest().body(response);
            }

            Cart existingCartItem = cartService.findByCustomerIDAndProductDetail(customerId, productDetailId);

            int currentQuantityInCart = (existingCartItem != null) ? existingCartItem.getQuantity() : 0;
            int newTotalQuantity = currentQuantityInCart + quantity;

            // Kiểm tra nếu tổng số lượng vượt quá 10
            if (newTotalQuantity > 10) {
                response.put("success", false);
                response.put("message", "Không thể thêm quá 10 sản phẩm cùng loại vào giỏ hàng.");
                return ResponseEntity.badRequest().body(response);
            }

            if (existingCartItem != null) {
                // Cập nhật số lượng nếu sản phẩm đã tồn tại trong giỏ hàng
                existingCartItem.setQuantity(newTotalQuantity);
                existingCartItem.setUpdateDate(new Date());
                cartRepository.save(existingCartItem);
            } else {
                // Thêm sản phẩm mới vào giỏ hàng
                Cart newCartItem = new Cart();
                newCartItem.setCustomer(customer);
                newCartItem.setProductDetail(productDetail);
                newCartItem.setQuantity(quantity);
                newCartItem.setStatus(1);
                newCartItem.setCreateDate(new Date());
                cartRepository.save(newCartItem);
            }
        } else {
            // Người dùng chưa đăng nhập, lưu giỏ hàng vào session
            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
            if (sessionCart == null) {
                sessionCart = new HashMap<>();
            }

            int currentQuantityInSessionCart = sessionCart.getOrDefault(productDetailId, 0);
            int newTotalQuantity = currentQuantityInSessionCart + quantity;

            // Kiểm tra nếu tổng số lượng vượt quá 10
            if (newTotalQuantity > 10) {
                response.put("success", false);
                response.put("message", "Không thể thêm quá 10 sản phẩm cùng loại vào giỏ hàng.");
                return ResponseEntity.badRequest().body(response);
            }

            // Cập nhật số lượng sản phẩm trong session
            sessionCart.put(productDetailId, newTotalQuantity);
            session.setAttribute("sessionCart", sessionCart);
        }

        model.addAttribute("clientLogin", clientLoginResponse);
        response.put("success", true);
        response.put("finalPrice", finalPrice);
        response.put("message", "Thêm sản phẩm vào giỏ hàng thành công.");
        return ResponseEntity.ok(response);
    }

        @PostMapping("/update-from-cart/{idProductDetailFromCart}")
        public ResponseEntity<?> updateProductDetailFromCart(HttpSession session,
                                                             @PathVariable("idProductDetailFromCart") Integer idProductDetailFromCart,
                                                             @RequestParam("quantityItem") Integer quantityFormCart) {
            if (quantityFormCart == null || quantityFormCart <= 0) {
                return ResponseEntity.badRequest().body("Số lượng không hợp lệ");
            }

            List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
            boolean updated = false;

            if (cartItems != null) {
                for (Cart item : cartItems) {
                    if (item.getProductDetail().getId().equals(idProductDetailFromCart)) {
                        item.setQuantity(quantityFormCart);
                        updated = true;
                        break;
                    }
                }
            }

            session.setAttribute("cartItems", cartItems);

            if (updated) {
                return ResponseEntity.ok("Quantity updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart");
            }
        }


    //////////////////////////////////////////////////////////////////////////////
    @GetMapping("/show-status-bill")
    public List<InvoiceStatus> getShowInvoiceStatus(HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        System.out.println("id bill tim thay la: " + idBill);
        List<InvoiceStatus> invoiceStatuses = this.invoiceStatusService.getALLInvoiceStatusByBill(idBill);
        for (InvoiceStatus invoiceStatus :invoiceStatuses) {
            System.out.println(invoiceStatus.toString());
        }
        return invoiceStatuses;
    }

    @GetMapping("/show-product-buy-status-bill/{pageNumber}")
    public List<Object[]> getShowProductBuyStatusBill(@PathVariable("pageNumber") String page,HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        try {
            Integer pageNumber = Integer.parseInt(page);
        }catch (NumberFormatException e) {
            return null;
        }
        System.out.println("id bill tim thay la: " + idBill);
        Pageable pageable = PageRequest.of((Integer.parseInt(page)-1),2);
        List<Object[]> listProductBuy = this.invoiceStatusService.getAllProductBuyClient(idBill);
        return convertListToPage(listProductBuy,pageable).getContent();
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
    public String getSendMailRequestBill(HttpSession session,@RequestBody Map<String,String> data) {
        Integer idBill = (Integer) session.getAttribute("idCheckStatusBill");
        String emailSend = data.get("emailSend"); // Lấy giá trị từ JSON
        System.out.println(emailSend);
        Bill bill = this.billService.findById(idBill).orElse(null);
        String ht = "http://localhost:8080/api-client/bill-pdf/"+bill.getId();
        this.templateRequestBill(emailSend,ht,bill.getCodeBill());
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