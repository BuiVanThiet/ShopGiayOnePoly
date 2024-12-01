package com.example.shopgiayonepoly.restController.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.bill.BillDetailAjax;
import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckMark2Request;
import com.example.shopgiayonepoly.dto.request.bill.ReturnBillDetailRequest;
import com.example.shopgiayonepoly.dto.response.bill.ExchangeBillDetailResponse;
import com.example.shopgiayonepoly.dto.response.bill.InfomationReturnBillResponse;
import com.example.shopgiayonepoly.dto.response.bill.ReturnBillDetailResponse;
import com.example.shopgiayonepoly.entites.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/return-exchange-bill-api")
public class ReturnExchangeBillRestController extends BaseBill {

    @GetMapping("/bill-detail/{page}")
    public List<BillDetail> getListBillDetailByIdBill(@PathVariable("page") String page,HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }

        String validatePage = validateInteger(page);
        if (!validatePage.trim().equals("")) {
            return null;
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,2);
        System.out.println("session la " + session.getAttribute("IdBill"));
        if(billDetailList == null) {
            System.out.println("neu list null vao day");
            billDetailList = this.billDetailService.getBillDetailByIdBill(idBill);
        }
        return getConvertListToPageBillDetail(billDetailList,pageable).getContent();
    }
    @GetMapping("/max-page-bill-detail")
    public Integer getMaxPageBillDetail(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }

        System.out.println("session la " + session.getAttribute("IdBill"));
        if(billDetailList == null) {
            System.out.println("neu list null vao day");
            billDetailList = this.billDetailService.getBillDetailByIdBill(idBill);
        }
        Integer page = (int) Math.ceil((double) this.billDetailList.size() / 2);
        return page;
    }

    @GetMapping("/bill-return-detail/{page}")
    public List<ReturnBillDetailResponse> getListReturnBillDetail(@PathVariable("page") String pageNumber,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }


        String validatePage = validateInteger(pageNumber);
        if (!validatePage.trim().equals("")) {
            return null;
        }

        returnBillDetailResponses = (List<ReturnBillDetailResponse>) session.getAttribute("returnBillDetailResponses");
        if(returnBillDetailResponses == null) {
            returnBillDetailResponses =  new ArrayList<>();
        }
        if(returnBillDetailResponses.size() <= 0) {
            System.out.println("khong co san pham tra!");
            for (int i = 0; i < this.exchangeBillDetailResponses.size(); i++) {
                ExchangeBillDetailResponse responseProductExchange = this.exchangeBillDetailResponses.get(i);
                getReduceProductDetail(responseProductExchange.getProductDetail().getId(),-responseProductExchange.getQuantityExchange());
            }
            totalExchange = BigDecimal.valueOf(0);
            exchangeAndReturnFee = BigDecimal.valueOf(0);
            discountedAmount =  BigDecimal.valueOf(0);

            session.setAttribute("exchangeBillDetailResponses", null);
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber)-1,2);
        return getConvertListToPageReturnBill(returnBillDetailResponses,pageable).getContent();
    }

    @GetMapping("/max-page-return-bill")
    public Integer getMaxPageReturnBill(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        returnBillDetailResponses = (List<ReturnBillDetailResponse>) session.getAttribute("returnBillDetailResponses");
        if(returnBillDetailResponses == null) {
            returnBillDetailResponses = new ArrayList<>();
        }
        Integer page = (int) Math.ceil((double) this.returnBillDetailResponses.size() / 2);
        return page;
    }

    @PostMapping("/add-product-in-return-bill")
    public ResponseEntity<Map<String, String>> getAddProductInReturnBill(@RequestBody ReturnBillDetailRequest request, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        ProductDetail productDetail = this.billDetailService.getProductDetailById(request.getIdProductDetail());
        if(productDetail == null || productDetail.getId() == null) {
            thongBao.put("message", "Sản phẩm không tồn tại!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        System.out.println("so luong tra la " + request.getQuantityReturn());
        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách trả lại hay chưa
        int index = getReturnBillDetailResponseIndex(productDetail.getId());

        if (index == -1) {
            // Nếu sản phẩm chưa tồn tại, kiểm tra số lượng trước khi thêm sản phẩm mới
            Boolean checkQuantity = returnBillDetailResponse(request.getIdProductDetail(), request.getQuantityReturn());
            if (checkQuantity == true) {
                thongBao.put("message", "Vượt quá số lượng trả!");
                thongBao.put("check", "3");
            } else {
                // Thêm sản phẩm mới vào danh sách trả lại
                ReturnBillDetailResponse newReturnBillDetailResponse = new ReturnBillDetailResponse();
                newReturnBillDetailResponse.setProductDetail(productDetail);
                newReturnBillDetailResponse.setQuantityReturn(request.getQuantityReturn());

                BigDecimal discountRatio = (BigDecimal) session.getAttribute("discountRatioPercentage");
                System.out.println("gia truoc khi giam " + request.getPriceBuy());
                // Tính giá sau khi giảm giá
                BigDecimal priReturn = request.getPriceBuy().multiply(BigDecimal.valueOf(1).subtract(discountRatio));
                // Làm tròn về bội số của 500
                BigDecimal roundedPrice = priReturn.divide(BigDecimal.valueOf(500), 0)
                        .multiply(BigDecimal.valueOf(500));
                System.out.println("gia sau khi giam " + roundedPrice);
                // Set giá trị đã làm tròn vào đối tượng response
                newReturnBillDetailResponse.setPriceBuy(roundedPrice);
                System.out.println("gia giam cho moi san pham la " + request.getPriceBuy() + "-" + roundedPrice + "=" + request.getPriceBuy().subtract(roundedPrice));
                newReturnBillDetailResponse.setPriceDiscount(request.getPriceBuy().subtract(roundedPrice));
                newReturnBillDetailResponse.setTotalReturn(roundedPrice.multiply(BigDecimal.valueOf(request.getQuantityReturn())));
                newReturnBillDetailResponse.setCreateDate(LocalDateTime.now());
                System.out.println(newReturnBillDetailResponse.toString());
                this.returnBillDetailResponses.add(newReturnBillDetailResponse);
                session.setAttribute("returnBillDetailResponses", returnBillDetailResponses); // Cập nhật session

                thongBao.put("message", "Chọn sản phẩm thành công!");
                thongBao.put("check", "1");
                idProductDetail = productDetail.getId();
                quantity = newReturnBillDetailResponse.getQuantityReturn();
            }
        } else {
            // Nếu sản phẩm đã tồn tại, tính tổng số lượng mới trước khi cập nhật
            ReturnBillDetailResponse existingReturnBillDetailResponse = returnBillDetailResponses.get(index);
            int newQuantity = existingReturnBillDetailResponse.getQuantityReturn() + request.getQuantityReturn();
            System.out.println("so luong tra vao hoa don la " + newQuantity);
            // Kiểm tra số lượng mới trước khi cộng dồn
            Boolean checkQuantity = returnBillDetailResponse(existingReturnBillDetailResponse.getProductDetail().getId(), quantity);
            if (checkQuantity == true) {
                thongBao.put("message", "Vượt quá số lượng trả!");
                thongBao.put("check", "3");
            } else {
                // Cập nhật số lượng và tổng tiền của sản phẩm
                existingReturnBillDetailResponse.setQuantityReturn(newQuantity);
                existingReturnBillDetailResponse.setTotalReturn(existingReturnBillDetailResponse.getPriceBuy().multiply(BigDecimal.valueOf(newQuantity)));
                existingReturnBillDetailResponse.setCreateDate(LocalDateTime.now());
                // Cập nhật lại phần tử trong danh sách
                returnBillDetailResponses.set(index, existingReturnBillDetailResponse);
                thongBao.put("message", "Cập nhật số lượng sản phẩm thành công!");
                thongBao.put("check", "1");
                idProductDetail = productDetail.getId();
                quantity = request.getQuantityReturn();
            }
        }
        int indexUpdateBill = getBillDetailResponseIndex(idProductDetail);
        for (BillDetail billDetail: billDetailList) {
            if(billDetail.getProductDetail().getId() == idProductDetail) {
                if(indexUpdateBill != -1) {
                    BillDetail detail = billDetailList.get(indexUpdateBill);
                    detail.setQuantity(detail.getQuantity()-quantity);
                    detail.setTotalAmount(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
                    System.out.println("da vao day de giam so luong");
                    billDetailList.set(indexUpdateBill,detail);
                }
            }
        }
        totalReturn = BigDecimal.valueOf(0);
        for (ReturnBillDetailResponse returnBillDetailResponse : returnBillDetailResponses) {
            totalReturn = totalReturn.add(returnBillDetailResponse.getTotalReturn());
        }
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/remove-product-in-return-bill/{idProduct}/{quantity}")
    public ResponseEntity<Map<String, String>> getRemoveProductInReturnBill(@PathVariable("idProduct") String idProductDetailRequest,@PathVariable("quantity") String quantity, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();

        String validateIdProduct = validateInteger(idProductDetailRequest);
        if (!validateIdProduct.trim().equals("")) {
            thongBao.put("message", "Sai định dạng sản phẩm!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        int index = getReturnBillDetailResponseIndex(Integer.parseInt(idProductDetailRequest));

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(index != -1) {
            this.returnBillDetailResponses.remove(index);
            List<BillDetail> billDetails = this.billDetailService.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"));
            for (BillDetail detail: billDetails) {
                if(detail.getProductDetail().getId() == Integer.parseInt(idProductDetailRequest)) {
                    int indexUpdateBill = getBillDetailResponseIndex(Integer.parseInt(idProductDetailRequest));
                    for (BillDetail billDetail: billDetailList) {
                        if(billDetail.getProductDetail().getId() == idProductDetail) {
                            if(indexUpdateBill != -1) {
                                BillDetail detailUpdate = billDetailList.get(indexUpdateBill);
                                detailUpdate.setQuantity(detail.getQuantity());
                                detailUpdate.setTotalAmount(detail.getTotalAmount());
                                System.out.println("da khoi phuc so luong");
                                thongBao.put("message", "Xóa sản phẩm trả thành công!");
                                thongBao.put("check", "1");
                                billDetailList.set(indexUpdateBill,detailUpdate);
                            }
                        }
                    }
                }
            }

            totalReturn = BigDecimal.valueOf(0);
            for (ReturnBillDetailResponse returnBillDetailResponse : returnBillDetailResponses) {
                totalReturn = totalReturn.add(returnBillDetailResponse.getTotalReturn());
            }
            return ResponseEntity.ok(thongBao);
        }else {
            thongBao.put("message", "Sai định dạng, xóa thất bại!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
    }

    @GetMapping("/infomation-return-bill")
    public InfomationReturnBillResponse getInfomationReturnBill(HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }

        List<Object[]> objects = this.billService.getInfomationBillReturn(idBill);
        InfomationReturnBillResponse response = new InfomationReturnBillResponse();
        for (int i = 0; i<1;i++) {
            Object[] objectSave = objects.get(i);
            response.setCodeBill((String) objectSave[1]);
            response.setNameCustomer((String) objectSave[2]);
            response.setDiscount((BigDecimal) objectSave[3]);
            BigDecimal discountRatio = ((BigDecimal) objectSave[4]).setScale(2, RoundingMode.HALF_UP);
            response.setDiscountRatioPercentage(discountRatio);
            response.setQuantityBuy((Integer) objectSave[5]);
            session.setAttribute("discountRatioPercentage", new BigDecimal(String.valueOf(objectSave[4])).divide(BigDecimal.valueOf(100))); // Reset lại dữ liệu trong session mỗi lần tải trang
        }
//        if (totalReturn.compareTo(BigDecimal.ZERO) == 0) {
//            response.setExchangeAndReturnFee(BigDecimal.valueOf(0));
//            response.setDiscountedAmount(BigDecimal.valueOf(0));
//        }else {
//            response.setExchangeAndReturnFee(exchangeAndReturnFee);
//            response.setDiscountedAmount(discountedAmount);
//        }
        response.setExchangeAndReturnFee(exchangeAndReturnFee);
        response.setDiscountedAmount(discountedAmount);

        response.setTotalReturn(totalReturn);
        response.setTotalExchange(totalExchange);
        System.out.println(response.toString());
        return response;
    }

    @GetMapping("/reset-return-bill-detail")
    public ResponseEntity<?> getResetReturnBill(HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
           return null;
        }

        session.setAttribute("returnBillDetailResponses", null); // Reset lại dữ liệu trong session mỗi lần tải trang
        session.setAttribute("totalMoneyReturn", 0); // Reset lại dữ liệu trong session mỗi lần tải trang
        session.setAttribute("discountRatioPercentage", 0); // Reset lại dữ liệu trong session mỗi lần tải trang
        billDetailList = null;
        quantity = 0;
        idProductDetail = 0;
        totalReturn = BigDecimal.valueOf(0);
        totalExchange = BigDecimal.valueOf(0);
        exchangeAndReturnFee =  BigDecimal.valueOf(0);
        discountedAmount =  BigDecimal.valueOf(0);

        session.setAttribute("exchangeBillDetailResponses", null); // Reset lại dữ liệu trong session mỗi lần tải trang
        session.setAttribute("totalMoneyExchange", 0); // Reset lại dữ liệu trong session mỗi lần tải trang
        productDetails = null;

        return ResponseEntity.ok("done");
    }

    //cong tru so luong tra
    @GetMapping("/increase-or-decrease-product-return/{idProductReturn}/{quantity}/{method}")
    public ResponseEntity<Map<String, String>> getIncreaseOrDecreaseProductReturn(
            @PathVariable("idProductReturn") Integer idProductReturn,
            @PathVariable("quantity") Integer quantityReturn,
            @PathVariable("method") String method,
            HttpSession session
    ) {
        Map<String, String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(!method.equals("cong") && !method.equals("tru")) {
            thongBao.put("message","Sai phương thức!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        int index = getReturnBillDetailResponseIndex(idProductReturn);
        if (index != -1) {
            ReturnBillDetailResponse returnBillDetailResponse = returnBillDetailResponses.get(index);
            int indexDetail = getBillDetailResponseIndex(idProductReturn);
            BillDetail detail = billDetailList.get(indexDetail);

            if (method.equals("cong")) {
                // Tăng số lượng trả về, nhưng kiểm tra nếu số lượng trong hóa đơn < 0
                if (detail.getQuantity() - quantityReturn < 0) {
                    thongBao.put("message", "Sản phẩm trong hóa đơn đã hết, không thể thêm số lượng trả!");
                    thongBao.put("check", "3");
                    return ResponseEntity.ok(thongBao);
                }

                // Nếu số lượng hợp lệ, cập nhật số lượng trả về và số lượng trong hóa đơn
                returnBillDetailResponse.setQuantityReturn(returnBillDetailResponse.getQuantityReturn() + quantityReturn);
                detail.setQuantity(detail.getQuantity() - quantityReturn);

            } else if (method.equals("tru")) {
                // Kiểm tra nếu số lượng trả về <= 1, không cho phép giảm nữa
                if (returnBillDetailResponse.getQuantityReturn() - quantityReturn < 1) {
                    thongBao.put("message", "Số lượng trả về phải luôn lớn hơn 1!");
                    thongBao.put("check", "3");
                    return ResponseEntity.ok(thongBao);
                }

                // Nếu số lượng hợp lệ, cập nhật số lượng trả về và số lượng trong hóa đơn
                returnBillDetailResponse.setQuantityReturn(returnBillDetailResponse.getQuantityReturn() - quantityReturn);
                detail.setQuantity(detail.getQuantity() + quantityReturn);
            }

            // Cập nhật tổng số tiền trả về và tổng số tiền trong hóa đơn
            returnBillDetailResponse.setTotalReturn(returnBillDetailResponse.getPriceBuy().multiply(BigDecimal.valueOf(returnBillDetailResponse.getQuantityReturn())));
            detail.setTotalAmount(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));

            // Cập nhật danh sách sau khi đã qua kiểm tra
            billDetailList.set(indexDetail, detail);
            returnBillDetailResponses.set(index, returnBillDetailResponse);
            totalReturn = BigDecimal.valueOf(0);
            for (ReturnBillDetailResponse returnBillDetailResponse1 : returnBillDetailResponses) {
                totalReturn = totalReturn.add(returnBillDetailResponse1.getTotalReturn());
            }
            thongBao.put("message", "Cập nhật sản phẩm thành công!");
            thongBao.put("check", "1");
            return ResponseEntity.ok(thongBao);
        } else {
            thongBao.put("message", "Sản phẩm không tồn tại!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
    }
    @GetMapping("/create-return-bill/{note}")
    public ResponseEntity<Map<String,String>> getCreateReturnBill(@PathVariable("note") String note, HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            System.out.println("bi vao day do id Bill ko ton tai");
            return ResponseEntity.ok(thongBao);
        }

        ReturnBillExchangeBill returnBill = new ReturnBillExchangeBill();
        if(note.trim().equals("")) {
            System.out.println("bi vao day do chua dien ly do");
            thongBao.put("message", "Chưa điền lý do trả!");
            thongBao.put("check", "1");
            return ResponseEntity.ok(thongBao);
        }
        //goi bill de tra
        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null || bill.getId() == null) {
            System.out.println("bi vao day do bill ko ton tai: " + idBill);
            thongBao.put("message", "Hóa đơn không tồn tại!");
            thongBao.put("check", "1");
            return ResponseEntity.ok(thongBao);
        }
        if(bill.getStatus() != 5 || bill.getPaymentStatus() == 0) {
            System.out.println("bi vao day do id Bill ko hop le");
            thongBao.put("message", "Hóa đơn không hợp lệ!");
            thongBao.put("check", "1");
            return ResponseEntity.ok(thongBao);
        }

        returnBill.setBill(bill);
        returnBill.setCodeReturnBillExchangBill("1");
        returnBill.setCustomerRefund(totalReturn);
        returnBill.setCustomerPayment(totalExchange);

        returnBill.setExchangeAndReturnFee(exchangeAndReturnFee);
        returnBill.setDiscountedAmount(exchangeBillDetailResponses.size() > 0 ? discountedAmount : new BigDecimal(0));
        BigDecimal totalExchange = returnBill.getCustomerPayment().subtract(returnBill.getCustomerRefund().subtract(returnBill.getExchangeAndReturnFee()).add(returnBill.getDiscountedAmount()));
        if(totalExchange.compareTo(new BigDecimal(20000000)) > 0) {
            thongBao.put("message", "Hóa đơn không hợp lệ do số tiền sản phâ đổi trên 20 triệu!");
            thongBao.put("check", "1");
            return ResponseEntity.ok(thongBao);
        }

        returnBill.setReason("hi ae");
        returnBill.setStatus(0);
        returnBill.setReason(note);
        ReturnBillExchangeBill returnBillSave = this.returnBillService.save(returnBill);
        returnBillSave.setCodeReturnBillExchangBill("THD"+returnBillSave.getId());
        this.returnBillService.save(returnBillSave);
        //luu cac chi tiet tra hang
        for (ReturnBillDetailResponse response : returnBillDetailResponses) {
            ReturnBillDetail returnBillDetai = new ReturnBillDetail();
            returnBillDetai.setReturnBill(returnBillSave);
            returnBillDetai.setProductDetail(response.getProductDetail());
            returnBillDetai.setQuantityReturn(response.getQuantityReturn());
            returnBillDetai.setPriceBuy(response.getPriceBuy());
            returnBillDetai.setTotalReturn(response.getTotalReturn());
            returnBillDetai.setStatus(0);
            this.returnBillDetailService.save(returnBillDetai);
        }
        if(exchangeBillDetailResponses.size() > 0) {
            for (ExchangeBillDetailResponse response : exchangeBillDetailResponses) {
                ExchangeBillDetail exchangeBillDetail = new ExchangeBillDetail();
                exchangeBillDetail.setExchangeBill(returnBillSave);
                exchangeBillDetail.setProductDetail(response.getProductDetail());
                exchangeBillDetail.setQuantityExchange(response.getQuantityExchange());
                exchangeBillDetail.setPriceAtTheTimeOfExchange(response.getPriceAtTheTimeOfExchange());
                exchangeBillDetail.setTotalExchange(response.getTotalExchange());
                exchangeBillDetail.setPriceRootAtTime(response.getPriceRootAtTime());
                exchangeBillDetail.setStatus(0);
                ExchangeBillDetail exchangeBillDetailSave = this.exchangeBillDetailService.save(exchangeBillDetail);
                this.getUpdateQuantityProduct(exchangeBillDetailSave.getProductDetail().getId(),exchangeBillDetailSave.getQuantityExchange());
            }
        }
        //luu chi tiet doi hang
        //cap nhat lai bill
        bill.setStatus(7);
        bill.setUpdateDate(new Date());
        this.billService.save(bill);
        this.setBillStatus(bill.getId(),201,session);

        thongBao.put("redirectUrl", "http://localhost:8080/staff/return-bill/create-return-bill");
        return ResponseEntity.ok(thongBao);
    }

    //goi danh sách return bill va return bill detail
    @GetMapping("/infomation-return-bill-from-bill-manage")
    public InfomationReturnBillResponse getReturnBillByIdBill(HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }

        ReturnBillExchangeBill returnBill = this.returnBillService.getReturnBillByIdBill(idBill);
        session.setAttribute("IdReturnBill",returnBill.getId());

        List<Object[]> objects = this.billService.getInfomationBillReturn(idBill);
        InfomationReturnBillResponse response = new InfomationReturnBillResponse();
        for (int i = 0; i<1;i++) {
            Object[] objectSave = objects.get(i);
            response.setCodeBill((String) objectSave[1]);
            response.setNameCustomer((String) objectSave[2]);
            response.setDiscount((BigDecimal) objectSave[3]);
            response.setDiscountRatioPercentage((BigDecimal) objectSave[4]);
            response.setQuantityBuy((Integer) objectSave[5]);
        }
        response.setId(returnBill.getId());
        response.setStatus(returnBill.getStatus());
        response.setNoteReturn(returnBill.getReason());
        response.setTotalReturn(returnBill.getCustomerRefund());
        response.setTotalExchange(returnBill.getCustomerPayment());

        response.setExchangeAndReturnFee(returnBill.getExchangeAndReturnFee());
        response.setDiscountedAmount(returnBill.getDiscountedAmount());
        session.setAttribute("discountRatioPercentage", response.getDiscountRatioPercentage().divide(BigDecimal.valueOf(100))); // Reset lại dữ liệu trong session mỗi lần tải trang

        return response;
    }
    @GetMapping("/infomation-return-bill-detail-from-bill-manage/{page}")
    public List<ReturnBillDetail> getReturnBillDetailByIdReturnBill(@PathVariable("page") String page,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idReturnBill = (Integer) session.getAttribute("IdReturnBill");
        String validateIdReturnBill = validateInteger(idReturnBill != null ? idReturnBill.toString() : "");
        if (!validateIdReturnBill.trim().equals("")) {
            return null;
        }

        String validatePage = validateInteger(page);
        if (!validatePage.trim().equals("")) {
            return null;
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,2);
        System.out.println(session.getAttribute("IdReturnBill"));
        return this.returnBillDetailService.getReturnBillDetailByIdReturnBill(idReturnBill,pageable).getContent();
    }

    @GetMapping("/max-page-return-bill-detail-from-bill-manage")
    public Integer getMaxPageReturnBillDetailByIdReturnBill(HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idReturnBill = (Integer) session.getAttribute("IdReturnBill");
        String validateIdReturnBill = validateInteger(idReturnBill != null ? idReturnBill.toString() : "");
        if (!validateIdReturnBill.trim().equals("")) {
            return null;
        }

        Integer page = (int) Math.ceil((double) this.returnBillDetailService.getReturnBillDetailByIdReturnBill(idReturnBill).size() / 2);
        return page;
    }

    public int getReturnBillDetailResponseIndex(Integer idProduct) {
        for (int i = 0; i < returnBillDetailResponses.size(); i++) {
            if (returnBillDetailResponses.get(i).getProductDetail().getId() == idProduct) {
                System.out.println(i);
                return i; // Trả về chỉ số của sản phẩm nếu tìm thấy
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    public int getBillDetailResponseIndex(Integer idProduct) {
        for (int i = 0; i < billDetailList.size(); i++) {
            if (billDetailList.get(i).getProductDetail().getId() == idProduct) {
                System.out.println(i);
                return i; // Trả về chỉ số của sản phẩm nếu tìm thấy
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    public boolean returnBillDetailResponse(Integer idProduct, Integer quantity) {
        for (BillDetail billDetail : billDetailList) {
            // So sánh ID của sản phẩm
            if (billDetail.getProductDetail().getId().equals(idProduct)) {
                // Kiểm tra nếu số lượng trong kho ít hơn số lượng yêu cầu
                if (billDetail.getQuantity() < quantity) {
                    System.out.println("so luong trong bill " + billDetail.getQuantity());
                    System.out.println("so luong nhap vao " + quantity);
                    System.out.println("sai yeu cau!");
                    return true; // Vượt quá số lượng
                } else {
                    System.out.println("so luong trong bill " + billDetail.getQuantity());
                    System.out.println("so luong nhap vao " + quantity);
                    System.out.println("dung yeu cau tra!");
                    return false; // Số lượng hợp lệ (không vượt quá)
                }
            }
        }
        System.out.println("dung yeu cau tra!");
        // Trường hợp không tìm thấy sản phẩm trong danh sách
        return false;
    }
    protected Page<BillDetail> getConvertListToPageBillDetail(List<BillDetail> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<BillDetail> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

    protected Page<ReturnBillDetailResponse> getConvertListToPageReturnBill(List<ReturnBillDetailResponse> list, Pageable pageable) {
        list.sort(Comparator.comparing(ReturnBillDetailResponse::getCreateDate).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<ReturnBillDetailResponse> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

    //dang cho exhcnage

    private List<Object[]> productDetails;
    @GetMapping("/bill-exchange-detail/{page}")
    public List<ExchangeBillDetailResponse> getListExchangeBillDetail(@PathVariable("page") String pageNumber, HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        String validatePage = validateInteger(pageNumber);
        if (!validatePage.trim().equals("")) {
            return null;
        }

        exchangeBillDetailResponses = (List<ExchangeBillDetailResponse>) session.getAttribute("exchangeBillDetailResponses");
        if(exchangeBillDetailResponses == null) {
            exchangeBillDetailResponses =  new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber)-1,2);
        return getConvertListToPageExchangeBill(exchangeBillDetailResponses,pageable).getContent();
    }

    @GetMapping("/max-page-exchange-bill")
    public Integer getMaxPageExchangeBill(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        exchangeBillDetailResponses = (List<ExchangeBillDetailResponse>) session.getAttribute("exchangeBillDetailResponses");
        if(exchangeBillDetailResponses == null) {
            exchangeBillDetailResponses = new ArrayList<>();
        }
        Integer page = (int) Math.ceil((double) this.exchangeBillDetailResponses.size() / 2);
        return page;
    }

    @GetMapping("/product-detaill-sell/{page}")
    public List<Object[]> getProductDetailSell(@PathVariable("page") String pageNumber, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        String validatePage = validateInteger(pageNumber);
        if (!validatePage.trim().equals("")) {
            return null;
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber)-1,4);
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        if (this.productDetails == null) {
            this.productDetails = this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,(Integer) session.getAttribute("IdBill"));
        }
        System.out.println("Số lượng 1 trang la " + productDetails.size());
        return convertListToPage(productDetails,pageable).getContent();
    }

    @GetMapping("/page-max-product")
    public Integer getMaxPageProduct(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }

        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        System.out.println("Thong tin loc " + productDetailCheckMark2Request.toString());
        Integer maxPageProduct = (int) Math.ceil((double) this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,idBill).size() / 4);
        System.out.println("so trang cua san pham " + maxPageProduct);
        return maxPageProduct;
    }
    @PostMapping("/filter-product-deatail")
    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckMark2Request productDetailCheckRequest2, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        this.productDetailCheckMark2Request = productDetailCheckRequest2;
        return ResponseEntity.ok("Done");
    }
    @PostMapping("/exchange")
    public ResponseEntity<Map<String,String>> getBuyProduct(
            @RequestParam(name = "quantityDetail") String quantity,
            @RequestParam(name = "idProductDetail") String idPDT,
            @RequestParam(name = "priceProductSale") String priceProductSale,
            @RequestParam(name = "priceProductRoot") String priceProductRoot,
            HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateQuantity = validateInteger(quantity);
        if (!validateQuantity.trim().equals("")) {
            thongBao.put("message","Sai định dạng số lượng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateIdPDT= validateInteger(idPDT);
        if (!validateIdPDT.trim().equals("")) {
            thongBao.put("message","Sai định dạng sản phẩm!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validatePriceProductSale= validateBigDecimal(priceProductSale);
        if (!validatePriceProductSale.trim().equals("")) {
            thongBao.put("message","Sai định dạng giá giảm!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validatePriceProductRoot= validateBigDecimal(priceProductRoot);
        if (!validatePriceProductRoot.trim().equals("")) {
            thongBao.put("message","Sai định dạng giá gốc!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(returnBillDetailResponses.size() <= 0) {
            thongBao.put("message","Mời chọn sản phẩm trả trước!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        thongBao.put("message","Thêm sản phẩm vào phiếu đơn đổi thành công!");
        thongBao.put("check","1");
        ExchangeBillDetailResponse exchangeBillDetailResponse;

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(idPDT));
        BigDecimal priceSaleCheck = new BigDecimal(0);
        BigDecimal priceRootCheck = productDetail.getPrice();

        if (productDetail.getSaleProduct() != null) {
            SaleProduct saleProduct = productDetail.getSaleProduct();

            // Kiểm tra tính hợp lệ của ngày áp dụng khuyến mãi
            if (saleProduct.getStartDate() != null && saleProduct.getEndDate() != null) {
                LocalDate today = LocalDate.now();
                boolean isApplicable = (today.isEqual(saleProduct.getStartDate()) || today.isAfter(saleProduct.getStartDate()))
                        && (today.isEqual(saleProduct.getEndDate()) || today.isBefore(saleProduct.getEndDate()));

                if (isApplicable) { // Nếu khuyến mãi còn hiệu lực
                    if (saleProduct.getDiscountType() == 1) { // Phần trăm giảm giá
                        priceSaleCheck = productDetail.getPrice()
                                .subtract(productDetail.getPrice()
                                        .multiply(saleProduct.getDiscountValue())
                                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP));
                    } else { // Giảm giá theo số tiền
                        priceSaleCheck = productDetail.getPrice().subtract(saleProduct.getDiscountValue());
                    }
                } else { // Nếu khuyến mãi hết hạn, giữ nguyên giá gốc
                    priceSaleCheck = productDetail.getPrice();
                }
            } else { // Nếu ngày bắt đầu hoặc kết thúc khuyến mãi không hợp lệ
                priceSaleCheck = productDetail.getPrice();
            }
        } else { // Nếu không có khuyến mãi
            priceSaleCheck = productDetail.getPrice();
        }


        System.out.println("gia giam "+priceSaleCheck);
        System.out.println("gia goc "+priceRootCheck);

        if(priceSaleCheck.compareTo(new BigDecimal(priceProductSale)) != 0) {
            thongBao.put("message","Giá giảm không đúng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(priceRootCheck.compareTo(new BigDecimal(priceProductRoot)) != 0) {
            thongBao.put("message","Giá gốc không đúng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(productDetail == null || productDetail.getId() == null) {
            thongBao.put("message","Sản phẩm không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if((productDetail.getQuantity()-Integer.parseInt(quantity)) < 0) {
            thongBao.put("message","Số lượng sản phẩm đổi lớn hơn trong kho!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer indexExchange = getIndexExchangeBillDetail(Integer.parseInt(idPDT));
        if(indexExchange == -1) {
            exchangeBillDetailResponse = new ExchangeBillDetailResponse();
            exchangeBillDetailResponse.setProductDetail(productDetail);
            exchangeBillDetailResponse.setQuantityExchange(Integer.parseInt(quantity));
            exchangeBillDetailResponse.setPriceAtTheTimeOfExchange(BigDecimal.valueOf(Long.parseLong(priceProductSale)));
            exchangeBillDetailResponse.setTotalExchange(exchangeBillDetailResponse.getPriceAtTheTimeOfExchange().multiply(BigDecimal.valueOf(exchangeBillDetailResponse.getQuantityExchange())));
            exchangeBillDetailResponse.setPriceRootAtTime(BigDecimal.valueOf(Long.parseLong(priceProductRoot)));
            exchangeBillDetailResponse.setCreateDate(LocalDateTime.now());
            System.out.println(exchangeBillDetailResponse.toString()+"them cua exchange");
            this.exchangeBillDetailResponses.add(exchangeBillDetailResponse);
        }else {
            exchangeBillDetailResponse = exchangeBillDetailResponses.get(indexExchange);
            exchangeBillDetailResponse.setQuantityExchange(exchangeBillDetailResponse.getQuantityExchange()+Integer.parseInt(quantity));
            exchangeBillDetailResponse.setTotalExchange(exchangeBillDetailResponse.getPriceAtTheTimeOfExchange().multiply(BigDecimal.valueOf(exchangeBillDetailResponse.getQuantityExchange())));
            exchangeBillDetailResponse.setCreateDate(LocalDateTime.now());
            System.out.println(exchangeBillDetailResponse.toString()+"them cua exchange");
            this.exchangeBillDetailResponses.set(indexExchange,exchangeBillDetailResponse);
        }
        //cap nhat lai so luong san pham
        getReduceProductDetail(productDetail.getId(),Integer.parseInt(quantity));

        session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);
        System.out.println("Số lượng đổi: " + quantity + ", ID sản phẩm chi tiết: " + idPDT);

        session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);

        totalExchange = BigDecimal.valueOf(0);
        for (ExchangeBillDetailResponse response: exchangeBillDetailResponses) {
            totalExchange = totalExchange.add(response.getTotalExchange());
        }
        System.out.println("tien doi la " + totalExchange);

        return ResponseEntity.ok(thongBao);
    }

    //xoa di san pham doi
    @GetMapping("/remove-exchange-product/{id}/{quantity}")
    public ResponseEntity<Map<String,String>> getRemoveProductExchange(
            @PathVariable("id") String idProductDetail,
            @PathVariable("quantity") String quantityExchange,
            HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateIdProductDetail = validateInteger(idProductDetail);
        if (!validateIdProductDetail.trim().equals("")) {
            thongBao.put("message","Sai định dạng sản phẩm đổi!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateQuantityExchange = validateInteger(quantityExchange);
        if (!validateQuantityExchange.trim().equals("")) {
            thongBao.put("message","Sai định dạng số lượng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }


        Integer indexExchange = getIndexExchangeBillDetail(Integer.parseInt(idProductDetail));
        if(indexExchange == -1) {
            thongBao.put("message","Không tìm thấy sản phẩm!");
            thongBao.put("check","3");
        }else {
            ExchangeBillDetailResponse exchangeBillDetailResponse = exchangeBillDetailResponses.get(indexExchange);
            if(Integer.parseInt(quantityExchange) != exchangeBillDetailResponse.getQuantityExchange()) {
                thongBao.put("message","Số lượng khác với thực tế!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
            this.exchangeBillDetailResponses.remove(exchangeBillDetailResponse);

            getReduceProductDetail(Integer.parseInt(idProductDetail),-(Integer.parseInt(quantityExchange)));
            thongBao.put("message","Xóa sản phẩm đổi thành công!");
            thongBao.put("check","1");

            session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);
        }

        totalExchange = BigDecimal.valueOf(0);
        for (ExchangeBillDetailResponse response: exchangeBillDetailResponses) {
            totalExchange = totalExchange.add(response.getTotalExchange());
        }

        return ResponseEntity.ok(thongBao);
    }

    //nut tang giam
    @PostMapping("/increase-or-decrease-product-exchange")
    public ResponseEntity<Map<String,String>> getIncreaseOrDecreaseProductExchange(@RequestBody BillDetailAjax billDetailAjax, HttpSession session) {

        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(!billDetailAjax.getMethod().equals("cong") && !billDetailAjax.getMethod().equals("tru")) {
            thongBao.put("message","Sai phương thức!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer indexExchange = getIndexExchangeBillDetail(billDetailAjax.getId());

        if(indexExchange == -1) {
            thongBao.put("message","Sửa số lượng đổi không thành công!");
            thongBao.put("check","3");
        }else {
            Integer quantityUpdate = (billDetailAjax.getMethod().equals("cong") ? 1 : -1);
            ExchangeBillDetailResponse exchangeBillDetailResponse;
            exchangeBillDetailResponse = exchangeBillDetailResponses.get(indexExchange);

            if((exchangeBillDetailResponse.getQuantityExchange()+quantityUpdate) < 1) {
                thongBao.put("message","Sản phẩm đổi ít nhất là 1!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }

            if((exchangeBillDetailResponse.getQuantityExchange()+quantityUpdate) > 10) {
                thongBao.put("message","Giới hạn mỗi món không quá 10!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }

            exchangeBillDetailResponse.setQuantityExchange(exchangeBillDetailResponse.getQuantityExchange()+quantityUpdate);
            exchangeBillDetailResponse.setTotalExchange(exchangeBillDetailResponse.getPriceAtTheTimeOfExchange().multiply(BigDecimal.valueOf(exchangeBillDetailResponse.getQuantityExchange())));
            this.exchangeBillDetailResponses.set(indexExchange,exchangeBillDetailResponse);
            getReduceProductDetail(billDetailAjax.getId(),quantityUpdate);

            session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);
            thongBao.put("message","Sửa sản phẩm vào phiếu đơn đổi thành công!");
            thongBao.put("check","1");
        }

        System.out.println(billDetailAjax.toString());

        totalExchange = BigDecimal.valueOf(0);
        for (ExchangeBillDetailResponse response: exchangeBillDetailResponses) {
            totalExchange = totalExchange.add(response.getTotalExchange());
        }

        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/infomation-exchange-bill-detail-from-bill-manage/{page}")
    public List<ExchangeBillDetail> getExchangeBillDetailByIdReturnBill(@PathVariable("page") String page,HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        String validatePage = validateInteger(page);
        if (!validatePage.trim().equals("")) {
            return null;
        }

        Integer idReturnBill = (Integer) session.getAttribute("IdReturnBill");
        String validateIdReturnBill = validateInteger(idReturnBill != null ? idReturnBill.toString() : "");
        if (!validateIdReturnBill.trim().equals("")) {
            return null;
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,2);
        System.out.println(session.getAttribute("IdReturnBill"));
        return this.exchangeBillDetailService.getExchangeBillDetailByIdReturnBill(idReturnBill,pageable).getContent();
    }

    @GetMapping("/max-page-exchange-bill-detail-from-bill-manage")
    public Integer getMaxPageExchangeBillDetailByIdReturnBill(HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        Integer idReturnBill = (Integer) session.getAttribute("IdReturnBill");
        String validateIdReturnBill = validateInteger(idReturnBill != null ? idReturnBill.toString() : "");
        if (!validateIdReturnBill.trim().equals("")) {
            return null;
        }

        Integer page = (int) Math.ceil((double) this.exchangeBillDetailService.getExchangeBillDetailByIdReturnBill(idReturnBill).size() / 2);
        System.out.println("so trang tra la " + page);
        return page;
    }

    //Phụ phí
    @GetMapping("/exchangeAndReturnFee/{monney}")
    public ResponseEntity<?> getExchangeAndReturnFee(@PathVariable("monney") String money,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        String validateMoney = validateBigDecimal(money);
        if (!validateMoney.trim().equals("")) {
            return null;
        }

        this.exchangeAndReturnFee = new BigDecimal(money);
        return ResponseEntity.ok("done");
    }
    @GetMapping("/discountedAmount/{monney}")
    public ResponseEntity<?> getDiscountedAmount(@PathVariable("monney") String money, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        String validateMoney = validateBigDecimal(money);
        if (!validateMoney.trim().equals("")) {
            return null;
        }

        this.discountedAmount = new BigDecimal(money);
        return ResponseEntity.ok("done");
    }

    private void getReduceProductDetail(Integer id, Integer quantityExchange) {
        int i = 0;
        for (Object[] objects : this.productDetails) {
            if (id.equals(objects[0])) {
                // Kiểm tra xem objects[11] có phải là Integer hay String
                if (objects[11] instanceof Integer) {
                    objects[11] = (Integer) objects[11] - quantityExchange;
                } else if (objects[11] instanceof String) {
                    objects[11] = Integer.parseInt((String) objects[11]) - quantityExchange;
                }
                this.productDetails.set(i, objects);
                // In ra giá trị các thuộc tính để kiểm tra
                Object[] objects1 = this.productDetails.get(i);
                System.out.println("Object sản phẩm là " + objects1[0] + " " + objects1[1] + " " + objects1[2] + " " + objects1[11]);
            }
            i++;
        }
    }

    private Integer getIndexExchangeBillDetail(Integer id) {
        for (int i = 0; i<this.exchangeBillDetailResponses.size();i++) {
            ExchangeBillDetailResponse objects = this.exchangeBillDetailResponses.get(i);
            if(id == objects.getProductDetail().getId()) {
                return i;
            }
        }
        return -1;
    }


    protected Page<ExchangeBillDetailResponse> getConvertListToPageExchangeBill(List<ExchangeBillDetailResponse> list, Pageable pageable) {
        list.sort(Comparator.comparing(ExchangeBillDetailResponse::getCreateDate).reversed());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<ExchangeBillDetailResponse> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}
