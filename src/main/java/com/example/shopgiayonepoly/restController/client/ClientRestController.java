package com.example.shopgiayonepoly.restController.client;

import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone;
import com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse;
import com.example.shopgiayonepoly.dto.response.client.VoucherClientResponse;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.implement.CustomerRegisterImplement;
import com.example.shopgiayonepoly.repositores.*;
import com.example.shopgiayonepoly.service.CartService;
import com.example.shopgiayonepoly.service.ClientService;
import com.example.shopgiayonepoly.service.CustomerService;
import com.example.shopgiayonepoly.service.InvoiceStatusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api-client")
public class ClientRestController {
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

    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}
