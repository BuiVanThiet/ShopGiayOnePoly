package com.example.shopgiayonepoly.controller.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.bill.CustomerShortRequest;
import com.example.shopgiayonepoly.dto.response.bill.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.entites.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff/bill")
public class BillController extends BaseBill {
    @GetMapping("/home")
    public String getForm(ModelMap modelMap, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
        }

        session.removeAttribute("pageReturn");
        session.removeAttribute("idBillCheckStatus");
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        Pageable pageable = PageRequest.of(0,10);
        List<Bill> billList =  this.billService.getBillByStatusNew(pageable);
        if(billList != null && !billList.isEmpty()) {
            Bill bill = billList.stream().reduce(billList.get(0),(max,id)->{
                if(id.getId() > max.getId()) {
                    return id;
                }
                return max;
            });
            session.setAttribute("IdBill", bill.getId());
            modelMap.addAttribute("clientInformation",bill.getCustomer());
            if(bill.getCustomer() != null){
                session.setAttribute("IdClient", bill.getCustomer().getId());
            }else {
                session.setAttribute("IdClient", null);
            }
            Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(bill.getId()).size() / 2);
            modelMap.addAttribute("pageNumber", pageNumber);
        }else {
            session.setAttribute("IdBill", null);
            session.setAttribute("IdClient",null);
        }
        session.setAttribute("numberPage",0);

        modelMap.addAttribute("bill",(Integer) session.getAttribute("IdBill") == null ? new Bill() : this.billService.findById((Integer)session.getAttribute("IdBill")).orElse(new Bill()));
        modelMap.addAttribute("client",(Integer)session.getAttribute("IdClient"));
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill") == null ? -1 : (Integer) session.getAttribute("IdBill")).orElse(null);
        if (bill != null) {
            bill.setBillType(1);
            this.billService.save(bill);
        }
        this.mess = "";
        this.colorMess = "";

        System.out.println("nhung san pham  ap dung dot giam gia");
        for (ProductDetail productDetail: saleProductService.getAllProductDetailWithDiscount()) {
            System.out.println("id" + productDetail.getId());
        }

        System.out.println("nhung san pham chua ap dung dot giam gia");
        for (ProductDetail productDetail: saleProductService.getAllProductDetailByPage()) {
            System.out.println("id" + productDetail.getId());
        }
        System.out.println("id bill dau tien la " + session.getAttribute("IdBill"));
        this.productDetailCheckMark2Request = null;
        return "Bill/index";

    }
    @GetMapping("/bill-detail/{idBill}")
    public String getBillDetail(@PathVariable("idBill") String idBill, ModelMap modelMap, HttpSession session) {

        String validateIdBill = validateInteger(idBill);
        if(!validateIdBill.trim().equals("")) {
            return "redirect:"+validateIdBill;
        }

        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null) {
            return "redirect:/login";
        }

        session.setAttribute("IdBill", Integer.parseInt(idBill));
        Bill bill = this.billService.findById(Integer.parseInt(idBill)).orElse(null);

        if(bill == null) {
            return "redirect:/404";
        }

        if(bill.getStatus() != 0) {
            return "redirect:/404";
        }

        bill.setBillType(1);
        this.billService.save(bill);
        if(bill.getCustomer() != null){
            session.setAttribute("IdClient", bill.getCustomer().getId());
        }else {
            session.setAttribute("IdClient", null);
        }

        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(Integer.parseInt(idBill)).size() / 2);
        modelMap.addAttribute("bill",this.billService.findById((Integer)session.getAttribute("IdBill")).orElse(new Bill()));
        modelMap.addAttribute("client",(Integer)session.getAttribute("IdClient"));
        modelMap.addAttribute("clientInformation",bill.getCustomer());

        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        System.out.println("mes hien len la " + mess);
        this.mess = "";
        this.colorMess  = "";

        if(bill.getStatus() == 0) {
            getDeleteVoucherByBill(Integer.parseInt(idBill));
        }

        this.productDetailCheckMark2Request = null;
        return "Bill/index";
    }
    @GetMapping("/create")
    public String getCreateBill(ModelMap modelMap,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(0,10);
        List<Bill> listB = this.billService.getBillByStatusNew(pageable);
        System.out.println(listB.size());
        if(listB.size() >= 10) {
            this.mess = "Thêm hóa đơn thất bại, chỉ được tồn tại 10 hóa đơn mới!";
            this.colorMess = "3";
            return "redirect:/staff/bill/home";
        }
        Bill billSave = new Bill();
        billSave.setStaff(staffLogin);
        billSave.setPaymentStatus(0);
        billSave.setStatus(0);
        billSave.setShippingPrice(BigDecimal.valueOf(0));
        billSave.setCash(BigDecimal.valueOf(0));
        billSave.setAcountMoney(BigDecimal.valueOf(0));
        billSave.setTotalAmount(BigDecimal.valueOf(0));
        billSave.setPaymentStatus(0);
        billSave.setShippingPrice(BigDecimal.valueOf(0));
        billSave.setPaymentStatus(0);
        billSave.setBillType(1);
        billSave.setPaymentMethod(1);
        billSave.setSurplusMoney(BigDecimal.valueOf(0));

        Bill bill = this.billService.save(billSave);

        System.out.printf(bill.toString());
        bill.setCodeBill("HD"+bill.getId().toString());
        bill.setUpdateDate(bill.getUpdateDate());
        bill.setCreateDate(bill.getCreateDate());
        Bill billGetStatus = this.billService.save(bill);
        session.setAttribute("IdBill",bill.getId());
        session.setAttribute("IdClient",null);
        session.setAttribute("notePayment","Tạo đơn hàng!");

        this.setBillStatus(billGetStatus.getId(),billGetStatus.getStatus(),session);

        this.productDetailCheckMark2Request = null;
        this.mess = "Thêm hóa đơn mới thành công!";
        this.colorMess = "1";
        return "redirect:/staff/bill/home";
    }

    @GetMapping("/addClientInBill/{idClient}")
    @ResponseBody
    public ResponseEntity<Map<String,String>> getAddClientInBill(
            @PathVariable("idClient") String idClient,
            HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        String validateIdClient = validateInteger(idClient);
        if(!validateIdClient.trim().equals("")) {
            this.mess = "Lỗi đinh dạng khách hàng!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null) {
            this.mess = "Nhân viên chưa đăng nhập!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            this.mess = "Sai định dạng hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            this.mess = "Sai định dạng hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }
        bill.setStaff(staff);
        bill.setUpdateDate(new Date());
        Customer customer = new Customer();
        this.mess = "Thêm khách hàng vào hóa đơn thành công!";
        this.colorMess = "1";
        customer.setId(Integer.parseInt(idClient));
        bill.setCustomer(customer);
        if(bill.getStatus() == 0) {
            this.billService.save(bill);
        }else {
            this.mess = "Thêm khách hàng vào hóa đơn thất bại!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/removeClientInBill/{idClient}")
    @ResponseBody
    public ResponseEntity<Map<String,String>>  getRemoveClientInBill(@PathVariable("idClient") String idClient, HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        String validateIdClient = validateInteger(idClient);
        if(!validateIdClient.trim().equals("")) {
            this.mess = "Lỗi định dạng khách hàng";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }


        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null) {
            this.mess = "Nhân viên chưa đăng nhập!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            this.mess = "Sai định dạng hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        bill.setStaff(staff);
        if(bill == null) {
            this.mess = "Lỗi định dạng hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        bill.setUpdateDate(new Date());
        if(bill.getCustomer() == null) {
            this.mess = "Không tìm thấy khách hàng trong hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }else {
            this.mess = "Xóa bỏ khách hàng trong hóa đơn thành công!";
            this.colorMess = "1";
            bill.setCustomer(null);
        }
        if(bill.getStatus() == 0) {
            this.billService.save(bill);
        }else {
            this.mess = "Xóa khách hàng vào hóa đơn thất bại!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/pay-ment/{idBill}")
    public String getPayMent(
            @PathVariable("idBill") String id,
            @RequestParam("cash") String cash,
            @RequestParam("note") String note,
            @RequestParam("shipMoney") String shipMoney,
            @RequestParam("surplusMoney") String surplusMoney,
            @RequestParam("cashAccount") String cashAccount,
            @RequestParam("customerShip") String customerShip,
            @RequestParam("priceDiscount") String priceDiscount,
            HttpSession session,
            ModelMap modelMap,
            HttpServletRequest request) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
        }

        String validateIdBill = validateInteger(id);
        if(!validateIdBill.trim().equals("")) {
            return "redirect:"+validateIdBill;
        }

        try {
            if (cash.contains(",")) {
                new BigDecimal(cash.replaceAll(",", ""));
            } else {
                new BigDecimal(cash);
            }
        } catch (NumberFormatException e) {
            return "redirect:/404";
        }

        String validateShipMoney = validateBigDecimal(shipMoney);
        if(!validateShipMoney.trim().equals("")) {
            return "redirect:"+validateShipMoney;
        }

        String validateSurplusMoney = validateBigDecimal(surplusMoney);
        if(!validateSurplusMoney.trim().equals("")) {
            return "redirect:"+validateSurplusMoney;
        }

        String validateCashAccount = validateBigDecimal(cashAccount);
        if(!validateCashAccount.trim().equals("")) {
            return "redirect:"+validateCashAccount;
        }

        if(customerShip.trim().equals("") || customerShip == null) {
            return "redirect:/404";
        }

        String validatePriceDiscount = validateBigDecimal(priceDiscount);
        if(!validatePriceDiscount.trim().equals("")) {
            return "redirect:"+validatePriceDiscount;
        }


        BigDecimal cashNumber;
        if (cash.contains(",")) {
            cashNumber = new BigDecimal(cash.replaceAll(",", ""));
        } else {
            cashNumber = new BigDecimal(cash);
        }

        BigDecimal surplusMoneyNumber = new BigDecimal(surplusMoney);
        BigDecimal shipMoneyNumber = new BigDecimal(shipMoney);

        Bill bill = this.billService.findById(Integer.parseInt(id)).orElse(null);
        if (bill == null) {
            return "redirect:/404";
        }
        if (bill.getStatus() != 0) {
            return "redirect:/404";
        }

        if(customerShip.trim().equals("Không có")) {
            bill.setAddRess(customerShip.trim());
        }else {
            String getAddRessDetail = customerShip.trim();
            String[] part = getAddRessDetail.split(",\\s*");
            String name = part[0];
            String numberPhone = part[1];
            String province = part[3];
            String district = part[4];
            String ward = part[5];
            String addResDetail = String.join(", ", java.util.Arrays.copyOfRange(part, 6, part.length));
            String regexNameCustomer = "[\\p{L}\\p{Nd}\\s]+";

            if (name.matches(regexNameCustomer) == false) {
                return "redirect:/404";
            }

            String validateProvince = validateInteger(province);
            if(!validateProvince.trim().equals("")) {
                return "redirect:"+validateProvince;
            }

            String validateDistrict = validateInteger(district);
            if(!validateDistrict.trim().equals("")) {
                return "redirect:"+validateDistrict;
            }

            String validateWard = validateInteger(ward);
            if(!validateWard.trim().equals("")) {
                return "redirect:"+validateWard;
            }

            String regexNumberPhone = "^(0?)(3[2-9]|5[689]|7[06-9]|8[1-6]|9[0-46-9])[0-9]{7}$";
            if (!numberPhone.trim().matches(regexNumberPhone)) {
                return "redirect:"+validateWard;
            }

            System.out.println("thong tin ship " + name+"-"+numberPhone+"-"+province+"-"+district+"-"+ward+"-"+addResDetail);

            bill.setAddRess(customerShip.trim());
        }

        BillTotalInfornationResponse billTotalInfornationResponse = this.billService.findBillVoucherById(bill.getId());
        BigDecimal cashAll = bill.getCash().add(bill.getAcountMoney().add(bill.getShippingPrice()));
        BigDecimal cashEquals = billTotalInfornationResponse.getFinalAmount().setScale(2, RoundingMode.HALF_UP);

        bill.setUpdateDate(new Date());

        bill.setNote(note);

        bill.setCash(cashNumber.setScale(2,RoundingMode.FLOOR).subtract(surplusMoneyNumber.setScale(2,RoundingMode.FLOOR)));
        bill.setSurplusMoney(surplusMoneyNumber.setScale(2,RoundingMode.FLOOR));
        bill.setShippingPrice(shipMoneyNumber);
        bill.setPriceDiscount(BigDecimal.valueOf(Long.parseLong(priceDiscount)));
        bill.setStaff(staffLogin);
        session.setAttribute("pageReturn",1);

        if(bill.getPaymentMethod() == 1 || bill.getBillType() == 2) {
            bill.setUpdateDate(new Date());
            if(bill.getBillType() == 2) {
                if (bill.getPaymentStatus() == 1) {
                    return "redirect:/404";
                }
                bill.setPaymentStatus(0);
//                su dung cai nay khi can test nhanh ben online
//                bill.setStatus(1);

                bill.setStatus(2);
                bill.setPaymentMethod(1);
                if(bill.getNote().length() < 0 || bill.getNote() == null || bill.getNote().trim().equals("")) {
                    bill.setNote("Giao hàng!");
                }
            }else {
                if (bill.getPaymentStatus() == 1) {
                    return "redirect:/404";
                }
                bill.setPaymentStatus(1);
                bill.setStatus(5);
                if (bill.getCash().compareTo(bill.getTotalAmount().subtract(bill.getPriceDiscount())) < 0) {
                    return "redirect:/404";
                }

                if(bill.getNote().length() < 0 || bill.getNote() == null || bill.getNote().trim().equals("")) {
                    bill.setNote("Thanh toán bằng tiền mặt!");
                }
            }
            System.out.println("Thong tin bill(TM)" + bill.toString());
            if(bill.getBillType() == 1) {
                this.setBillStatus(bill.getId(),101,session);
            }
            this.setBillStatus(bill.getId(),bill.getStatus(),session);
            System.out.println("thong tin hoa don duoc tao " + bill.toString());

            this.billService.save(bill);
            modelMap.addAttribute("title","Tạo hóa đơn thành công!");
            modelMap.addAttribute("url",bill.getId());
            this.mess = "";
            this.colorMess = "";
//            this.getUpdateQuantityProduct(session);
            return "Bill/successBill";
        }else if (bill.getPaymentMethod() == 2) {
            if(bill.getNote().length() < 0 || bill.getNote() == null || bill.getNote().trim().equals("")) {
                bill.setNote("Thanh toán bằng tiền tài khoản!");
            }
            bill.setCash(BigDecimal.valueOf(0));
            bill.setSurplusMoney(BigDecimal.valueOf(0));
            this.billPay = bill;
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder((Integer.parseInt(cashAccount)), "chuyenKhoan", baseUrl);
            return "redirect:" + vnpayUrl;
        }else {
            this.billPay = bill;
            boolean isGreaterThanZero = bill.getSurplusMoney().compareTo(BigDecimal.ZERO) > 0;

            if(isGreaterThanZero == true) {
                bill.setPaymentStatus(1);
                bill.setUpdateDate(new Date());
                System.out.println("Do nhap qua so tien mat nen khong the tao thanh toan online");
                System.out.println("Thong tin Bill thanh toan bang tien va tk(1)" + this.billPay.toString());
                this.billService.save(bill);
                this.setBillStatus(bill.getId(),bill.getStatus(),session);
                this.setBillStatus(bill.getId(),101,session);
                modelMap.addAttribute("title","Tạo hóa đơn thành công!");
                this.mess = "";
                this.colorMess = "";
                modelMap.addAttribute("url",bill.getId());
                return "Bill/successBill";
            }else {
                if(bill.getNote().length() < 0 || bill.getNote() == null || bill.getNote().trim().equals("")) {
                    bill.setNote("Thanh toán bằng tiền tài khoan và tiền mặt!");
                }
                bill.setSurplusMoney(BigDecimal.valueOf(0));
                this.billPay = bill;
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnPayService.createOrder((Integer.parseInt(cashAccount)+Integer.parseInt(shipMoney)), "chuyenKhoan", baseUrl);
                System.out.println("Thong tin Bill thanh toan bang tien va tk(2)" + this.billPay.toString());
                return "redirect:" + vnpayUrl;
            }
        }
    }


    //thanh toan bang vnpay
    @GetMapping("/vnpay-payment")
    public String getVNpay(HttpServletRequest request, Model model, HttpSession session,ModelMap modelMap){
        int paymentStatus =vnPayService.orderReturn(request);
        String vnpTmnCode = request.getParameter("vnp_TmnCode");
        String vnpBankCode = request.getParameter("vnp_BankCode");
        String vnpCardType = request.getParameter("vnp_CardType");
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        String vnpTransactionStatus = request.getParameter("vnp_TransactionStatus");
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        String vnpSecureHashType = request.getParameter("vnp_SecureHashType");

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String totalPrice = request.getParameter("vnp_Amount");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String bankTranNo = request.getParameter("vnp_BankTranNo");
        String vnpRequestId = request.getParameter("vnp_RequestId");
        String vnpSecureHash = request.getParameter("vnp_SecureHash");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);

        // In ra toàn bộ tham số
        // Mã website của merchant trên hệ thống của VNPAY. Ví dụ: 2QXUI4J4
        System.out.println("vnp_TmnCode: " + vnpTmnCode);
        // mã ngân haàng
        System.out.println("vnp_BankCode: " + vnpBankCode);
        // loại thẻ
        System.out.println("vnp_CardType: " + vnpCardType);
        //Mã phản hồi kết quả thanh toán. Quy định mã trả lời 00 ứng với kết quả Thành công cho tất cả các API. Tham khảo thêm tại bảng mã lỗi
        System.out.println("vnp_ResponseCode: " + vnpResponseCode);
        //Mã phản hồi kết quả thanh toán. Tình trạng của giao dịch tại Cổng thanh toán VNPAY.
        //-00: Giao dịch thanh toán được thực hiện thành công tại VNPAY
        //-Khác 00: Giao dịch không thành công tại VNPAY Tham khảo thêm tại bảng mã lỗi
        System.out.println("vnp_TransactionStatus: " + vnpTransactionStatus);
        //Giống mã gửi sang VNPAY khi gửi yêu cầu thanh toán.
        System.out.println("vnp_TxnRef: " + vnpTxnRef);
        //mã băm
        System.out.println("vnp_SecureHashType: " + vnpSecureHashType);
        //Thông tin mô tả nội dung thanh toán
        System.out.println("vnp_OrderInfo: " + orderInfo);
        //Thời gian thanh toán. Định dạng: yyyyMMddHHmmss
        System.out.println("vnp_PayDate: " + paymentTime);
//Số tiền thanh toán. VNPAY phản hồi số tiền nhân thêm 100 lần.
        System.out.println("vnp_Amount: " + totalPrice);
//Mã giao dịch ghi nhận tại hệ thống VNPAY. Ví dụ: 20170829153052
        System.out.println("vnp_TransactionNo: " + transactionNo);
        //Mã giao dịch tại Ngân hàng
        System.out.println("vnp_BankTranNo: " + bankTranNo);

        System.out.println("vnp_RequestId: " + vnpRequestId);

        System.out.println("tien tai khoan " + totalPrice);
        BigDecimal accountMoney = new BigDecimal(totalPrice);

        Integer returnFrom = (Integer) session.getAttribute("pageReturn");
//        1 la cho trang ban hang
//        2 la cho trang thanh toan khi nhan hang
//        3 la thanh toan luon khi dat hang
        if(paymentStatus == 1) {
            TransactionVNPay transactionVNPay = new TransactionVNPay();
            transactionVNPay.setVnpTmnCode(vnpTmnCode);
            transactionVNPay.setVnpBankCode(vnpBankCode);
            transactionVNPay.setVnpCardType(vnpCardType);
            transactionVNPay.setVnpResponseCode(vnpResponseCode);
            transactionVNPay.setVnpTransactionStatus(vnpTransactionStatus);
            transactionVNPay.setVnpTxnRef(vnpTxnRef);
            transactionVNPay.setVnpSecureHashType(vnpSecureHashType);
            transactionVNPay.setVnpOrderInfo(orderInfo);
            transactionVNPay.setVnpPayDate(paymentTime);
            transactionVNPay.setVnpAmount(String.valueOf(accountMoney.divide(BigDecimal.valueOf(100))));
            transactionVNPay.setVnpTransactionNo(transactionNo);
            transactionVNPay.setVnpBankTranNo(bankTranNo);
            transactionVNPay.setVnpSecureHash(vnpSecureHash);
            transactionVNPay.setStatus(1);
            this.mess = "";
            this.colorMess = "";
            this.transactionVNPayService.save(transactionVNPay);
        }

        if(returnFrom == 1) {
            if (billPay == null) {
                System.out.println("day la khong co bill");
                return "redirect:/404";
            }
            if(billPay.getStatus() >= 5) {
                System.out.println("day la trang thai da hoan thanh");
                return "redirect:/404";
            }
            if (billPay.getPaymentStatus() == 1) {
                System.out.println("day la bill da thanh toan");
                return "redirect:/404";
            }
            if(paymentStatus == 1) {
                this.billPay.setAcountMoney(accountMoney.divide(BigDecimal.valueOf(100)));
                this.billPay.setPaymentStatus(1);
                this.billPay.setSurplusMoney(BigDecimal.valueOf(0.00));
                this.billPay.setUpdateDate(new Date());
                this.billPay.setStatus(5);
                this.billPay.setTransactionNo(transactionNo);
                this.billPay.setBankTranNo(bankTranNo);
                modelMap.addAttribute("url",billPay.getId());
                this.billService.save(this.billPay);
                System.out.println("vnpSecureHash: " + vnpSecureHash);

                this.setBillStatus(this.billPay.getId(),101,session);
                this.setBillStatus(this.billPay.getId(),this.billPay.getStatus(),session);
//                this.getUpdateQuantityProduct(session);
                this.mess = "";
                this.colorMess = "";
                modelMap.addAttribute("title","Tạo hóa đơn thành công!");
                return "Bill/successBill" ;
            }else {
                modelMap.addAttribute("title","Tạo hóa đơn thất bại!");
                System.out.println("Bil bo thanh toan " + this.billPay.toString());
                return "Bill/errorBill";
            }
        }else if (returnFrom == 2){
            String checkBil = (String) session.getAttribute("checkBill");
            if(checkBil == null || checkBil.isEmpty()) {
                return "redirect:/404";
            }

            PaymentExchange paymentExchange = null;

            if(checkBil.trim().equals("billShip")) {
                this.billPay = (Bill) session.getAttribute("billPaymentRest");
                if (billPay == null) {
                    System.out.println("day la khong co bill");
                    return "redirect:/404";
                }
                if(billPay.getStatus() >= 5) {
                    System.out.println("day la trang thai da hoan thanh");
                    return "redirect:/404";
                }
                if (billPay.getPaymentStatus() == 1) {
                    System.out.println("day la bill da thanh toan");
                    return "redirect:/404";
                }
            }else if (checkBil.trim().equals("exchangeBill")) {
                paymentExchange = (PaymentExchange) session.getAttribute("exchangeBillPaymentRest");
                if (paymentExchange == null) {
                    System.out.println("day la khong co bill");
                    return "redirect:/404";
                }
            }else {
                return "redirect:/404";
            }

            if(paymentStatus == 1) {
                if(checkBil.trim().equals("billShip")) {
                    this.billPay.setAcountMoney(accountMoney.divide(BigDecimal.valueOf(100)));
                    this.billPay.setPaymentStatus(1);
                    this.billPay.setSurplusMoney(BigDecimal.valueOf(0.00));
                    this.billPay.setUpdateDate(new Date());
                    this.billPay.setPaymentMethod(2);
                    this.billPay.setTransactionNo(transactionNo);
                    this.billPay.setBankTranNo(bankTranNo);
                    this.billService.save(this.billPay);
                    this.setBillStatus(this.billPay.getId(),101,session);
                    this.billPay = null;
                    session.removeAttribute("billPaymentRest");
                    session.removeAttribute("pageReturn");
                    session.removeAttribute("checkBill");
//                this.getUpdateQuantityProduct(session);
                    mess = "Thanh toán thành công!";
                    colorMess = "1";
                    return "redirect:/staff/bill/bill-status-index/"+session.getAttribute("IdBill");
                }else if (checkBil.trim().equals("exchangeBill")) {
                    paymentExchange.setTransactionNo(transactionNo);
                    paymentExchange.setBankTranNo(bankTranNo);
                    this.paymentExchangeService.save(paymentExchange);
                    mess = "Thanh toán thành công!";
                    colorMess = "1";
                    this.setBillStatus(paymentExchange.getExchangeBilll().getBill().getId(),102,session);
                    session.removeAttribute("exchangeBillPaymentRest");
                    session.removeAttribute("pageReturn");
                    session.removeAttribute("checkBill");
                    return "redirect:/staff/bill/bill-status-index/"+session.getAttribute("IdBill");
                }else {
                    session.removeAttribute("billPaymentRest");
                    session.removeAttribute("exchangeBillPaymentRest");
                    session.removeAttribute("pageReturn");
                    session.removeAttribute("checkBill");
                    return "redirect:/404";
                }
            }else {
                session.removeAttribute("billPaymentRest");
                session.removeAttribute("exchangeBillPaymentRest");
                session.removeAttribute("pageReturn");
                session.removeAttribute("checkBill");
                return "redirect:/staff/bill/bill-status-index/"+session.getAttribute("IdBill");
            }
        }else {
            Bill payBillOrder= (Bill) session.getAttribute("payBillOrder");
            if(paymentStatus == 1) {
                payBillOrder.setAcountMoney(accountMoney.divide(BigDecimal.valueOf(100)));
                payBillOrder.setTransactionNo(transactionNo);
                payBillOrder.setBankTranNo(bankTranNo);
                payBillOrder.setPaymentStatus(1);
                payBillOrder.setSurplusMoney(new BigDecimal(0));
                payBillOrder.setCash(new BigDecimal(0));
                session.setAttribute("notePayment",payBillOrder.getNote());
                this.setBillStatus(payBillOrder.getId(), 0, session);
                this.setBillStatus(payBillOrder.getId(), 1, session);
                this.setBillStatus(payBillOrder.getId(),101,session);
                this.billService.save(payBillOrder);

                return "redirect:/onepoly/order-success";
            }else {
                List<BillDetail> billDetails = clientService.getListBillDetailByID(payBillOrder.getId());
                for ( BillDetail billDetail:billDetails) {
                    this.billDetailService.deleteById(billDetail.getId());
                }
                this.billService.deleteById(payBillOrder.getId());
                System.out.println("Delete thanh cong khi chua thanh toan");
                return "redirect:/onepoly/payment";
            }
        }
    }

    @GetMapping("/deleteBillDetail/{id}")
    @ResponseBody
    public  ResponseEntity<Map<String,String>> getDeleteProductDetail(@PathVariable("id") String id, HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateIdBillDetail = validateInteger(id);
        if(!validateIdBillDetail.trim().equals("")) {
            thongBao.put("message","Lỗi xóa sản phẩm mua!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        BillDetail billDetail = this.billDetailService.findById(Integer.parseInt(id)).orElse(null);

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            this.mess = "Sai định dạng hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);

        if(bill == null) {
            thongBao.put("message","Không tìm thấy hóa đơn!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(bill.getStatus() >= 2 || bill.getPaymentStatus() == 1) {
            thongBao.put("message","Không thể xóa sản phẩm!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if (bill.getStatus() == 0) {
            this.getUpdateQuantityProduct(billDetail.getProductDetail().getId(),-billDetail.getQuantity());
        }

        bill.setStaff(staffLogin);
        if(bill.getStatus() == 1) {
            List<BillDetail> checkBillDetailList = this.billDetailService.getBillDetailByIdBill(bill.getId());
            if(checkBillDetailList.size() <= 1) {
                thongBao.put("message","Đơn chờ xác nhận tối thiểu phải có 1 sản phẩm!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
        }
        this.billDetailService.delete(billDetail);
        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
        bill.setUpdateDate(new Date());
        System.out.println("tong tien hoa don khi xoas " + total);
        bill.setTotalAmount(total);

        this.billService.save(bill);

        if(bill.getStatus() == 0) {
            getDeleteVoucherByBill((Integer) session.getAttribute("IdBill"));
        }
        thongBao.put("message","Xóa sản phẩm trong hóa đơn thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/buy-product-detail")
    @ResponseBody
    public ResponseEntity<Map<String,String>> getBuyProduct(
            @RequestParam(name = "quantityDetail") String quantity,
            @RequestParam(name = "idProductDetail") String idPDT,
            @RequestParam(name = "priceProductSale") String priceProductSale,
            @RequestParam(name = "priceProductRoot") String priceProductRoot,
            HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        String validateQuantity = validateInteger(quantity);
        if (!validateQuantity.trim().equals("")) {
            thongBao.put("message","Lỗi định dạng số lượng sản phẩm!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateIdPDT = validateInteger(idPDT);
        if (!validateIdPDT.trim().equals("")) {
            thongBao.put("message","Lỗi định dạng sản phẩm mua!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        thongBao.put("message","Thêm sản phẩm vào hóa đơn thành công!");
        thongBao.put("check","1");

        System.out.println("Số lượng mua: " + quantity + ", ID sản phẩm chi tiết: " + idPDT);

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(idPDT));

        if(productDetail == null || productDetail.getId() == null) {
            thongBao.put("message","Lỗi định dạng sản phẩm mua!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            this.mess = "Sai định dạng hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Bill billById = this.billService.findById(idBill).orElse(null);
        if(billById == null) {
            thongBao.put("message","Lỗi định dạng hóa đơn!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(billById.getStatus() >= 2 || billById.getPaymentStatus() == 1) {
            thongBao.put("message","Không thể mua sản phẩm!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        BillDetail billDetailSave = getBuyProduct(billById,productDetail,Integer.parseInt(quantity));
        if (billDetailSave.getQuantity() > 10) {
            thongBao.put("message","Số lượng mua sản phẩm này không được quá 10 số lượng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        billById.setStaff(staffLogin);
        this.billService.save(billById);

        this.billDetailService.save(billDetailSave);
        this.setTotalAmount(billDetailSave.getBill());
        if(billById.getStatus() == 0) {
            this.getUpdateQuantityProduct(productDetail.getId(),Integer.parseInt(quantity));
        }

        System.out.println("da mua san pham !");
        if(billById.getStatus() == 0) {
            getDeleteVoucherByBill((Integer) session.getAttribute("IdBill"));
        }

        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/delete-voucher-bill")
    @ResponseBody
    public ResponseEntity<Map<String,String>> getDeleteVoucherBill(HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            thongBao.put("message", "Sai định dạng hóa đơn!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            thongBao.put("message","Hóa đơn không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(bill.getStatus() > 1) {
            thongBao.put("message","Không xóa được mã giảm giá!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(bill.getPaymentStatus() == 1) {
            thongBao.put("message","Không xóa được mã giảm giá!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        this.getSubtractVoucher(bill.getVoucher(),-1);
        thongBao.put("message","Xóa mã giảm giá thành công!");
        thongBao.put("check","1");
        bill.setVoucher(null);
        bill.setUpdateDate(new Date());
        bill.setStaff(staffLogin);
        Bill billSave = this.billService.save(bill);
        if(billSave.getStatus() == 1) {
            System.out.println("gia duoc giam been xoa " + this.billService.getDiscountBill(billSave.getId()));
            billSave.setPriceDiscount(new BigDecimal(this.billService.getDiscountBill(billSave.getId())));
            this.billService.save(billSave);
        }
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/click-voucher-bill/{idVoucher}")
    @ResponseBody
    public ResponseEntity<Map<String,String>> getClickVoucherBill(@PathVariable("idVoucher") String idVoucher, HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            thongBao.put("message", "Sai định dạng hóa đơn!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }

        String validateIdVoucher = validateInteger(idVoucher);
        if(!validateIdVoucher.trim().equals("")) {
            thongBao.put("message","Sai định dạng mã giảm giá!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            thongBao.put("message","Hóa đơn không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(bill.getStatus() > 1) {
            thongBao.put("message","Không thêm được mã giảm giá!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(bill.getPaymentStatus() == 1) {
            thongBao.put("message","Không thêm được mã giảm giá!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }


        Voucher voucher = this.voucherService.getOne(Integer.parseInt(idVoucher));
        if (voucher == null) {
            thongBao.put("message","mã giảm giá không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân vien chưa đag nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        LocalDate today = LocalDate.now();

        // Kiểm tra xem voucher có hiệu lực trong ngày hôm nay không
        if (today.isBefore(voucher.getStartDate()) || today.isAfter(voucher.getEndDate())) {
            thongBao.put("message","Phiếu giảm giá không khả dụng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        // Kiểm tra tổng giá trị hóa đơn có đủ điều kiện áp dụng voucher không
        if (bill.getTotalAmount().compareTo(voucher.getPricesApply()) < 0) {
            thongBao.put("message","Phiếu giảm giá không khả dụng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if (voucher.getStatus() != 1) {
            thongBao.put("message","Phiếu giảm giá không khả dụng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(bill.getVoucher() != null) {
            this.getSubtractVoucher(bill.getVoucher(),-1);
        }

        bill.setVoucher(voucher);
        bill.setUpdateDate(new Date());
        bill.setStaff(staffLogin);
        Bill billSave = this.billService.save(bill);
        if(billSave.getStatus() == 1) {
            System.out.println("gia duoc giam been them " + this.billService.getDiscountBill(billSave.getId()));
            billSave.setPriceDiscount(new BigDecimal(this.billService.getDiscountBill(billSave.getId())));
            this.billService.save(billSave);
        }

        thongBao.put("message","Thêm mã giảm giá thành công!");
        thongBao.put("check","1");
        this.getSubtractVoucher(voucher,1);
        return ResponseEntity.ok(thongBao);
    }

    //danh cho quan ly hoa don
    @GetMapping("/bill-status-index/{idBill}")
    public String getStatusBill(@PathVariable("idBill") String idBill,HttpSession session, ModelMap modelMap) {

        String validateIdBill = validateInteger(idBill);
        if(!validateIdBill.trim().equals("")) {
            return "redirect:"+validateIdBill;
        }

        Bill bill = this.billService.findById(Integer.parseInt(idBill)).orElse(null);
        if(bill == null) {
            return "redirect:/404";
        }

        if(bill.getStatus() == 0) {
            return "redirect:/404";
        }

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
        }

        ReturnBillExchangeBill returnBillExchangeBill = this.returnBillService.getReturnBillByIdBill(bill.getId());
        if(returnBillExchangeBill != null) {
            if(returnBillExchangeBill.getId() != null) {
                modelMap.addAttribute("returnBillExchangeBill",returnBillExchangeBill);
            }
        }

        session.setAttribute("IdBill",Integer.parseInt(idBill));
        modelMap.addAttribute("client", (Integer) session.getAttribute("IdClient"));
        modelMap.addAttribute("bill",(Integer) session.getAttribute("IdBill") == null ? null : this.billService.findById((Integer)session.getAttribute("IdBill")).orElse(null));
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        session.setAttribute("pageReturn",2);

        mess = "";
        colorMess = "";
        return "Bill/billInformationIndex";
    }

    @PostMapping("/add-quickly-customer")
    public String getAddQuicklyCustomer(@ModelAttribute(name = "customerShort")CustomerShortRequest customerShortRequest, HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        if(staff == null) {
            return "redirect:/login";
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            this.mess = "Sai định dạng hóa đơn!";
            this.colorMess = "3";
            return "redirect:/404";
        }

        String regexNameCustomer = "[\\p{L}\\p{Nd}\\s]+";

        if (customerShortRequest.getNameCustomer().matches(regexNameCustomer) == false) {
            return "redirect:/404";
        }

        String validateProvince = validateInteger(customerShortRequest.getProvince());
        if(!validateProvince.trim().equals("")) {
            return "redirect:"+validateProvince;
        }

        String validateDistrict = validateInteger(customerShortRequest.getDistrict());
        if(!validateDistrict.trim().equals("")) {
            return "redirect:"+validateDistrict;
        }

        String validateWard = validateInteger(customerShortRequest.getWard());
        if(!validateWard.trim().equals("")) {
            return "redirect:"+validateWard;
        }

        String regexNumberPhone = "^(0?)(3[2-9]|5[689]|7[06-9]|8[1-6]|9[0-46-9])[0-9]{7}$";
        if (!customerShortRequest.getNumberPhone().trim().matches(regexNumberPhone)) {
            return "redirect:/404";
        }

        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!customerShortRequest.getEmail().trim().matches(regexEmail)) {
            return "redirect:/404";
        }

        Bill bill = this.billService.findById(idBill).orElse(null);

        System.out.println("thong tin them nhanh la " + customerShortRequest.toString() + customerShortRequest.getStatus());
        if(bill.getStatus() > 1) {
            return "redirect:/404";

        }

        if (bill != null && bill.getStatus() == 0) {
            Customer customer = new Customer();
            customer.setFullName(customerShortRequest.getNameCustomer());
            customer.setNumberPhone(customerShortRequest.getNumberPhone());
            customer.setEmail(customerShortRequest.getEmail());
            customer.setAddRess(customerShortRequest.getWard().trim()+","+customerShortRequest.getDistrict().trim()+","+customerShortRequest.getProvince().trim()+","+customerShortRequest.getAddResDetail());
            customer.setCreateDate(new Date());
            customer.setUpdateDate(new Date());
            customer.setStatus(1);

            Customer customerSave = this.customerService.save(customer);

            bill.setUpdateDate(new Date());
            bill.setCustomer(customerSave);
            this.billService.save(bill);

            this.mess = "Đã thêm nhanh khách hàng!";
            this.colorMess = "1";
            return "redirect:/staff/bill/bill-detail/"+idBill;
        }else {
            return "redirect:/404";
        }
    }

    @GetMapping("/manage-bill")
    public String getIndexManageBill() {
        return "Bill/manageBillIndex";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        this.displayProductDetailsWithCurrentPrice();
        return staff;
    }

    @ModelAttribute("customerShort")
    public CustomerShortRequest customerShortRequest(){
        return new CustomerShortRequest();
    }

}
