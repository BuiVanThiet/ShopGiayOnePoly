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
//        displayProductDetailsWithCurrentPrice();
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

        getDeleteVoucherByBill(Integer.parseInt(idBill));

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

//        this.getAddHistory("bill",bill.getId(),"Mới tạo","Mới tao","Mới tạo",staff,"Mới tạo");
        this.productDetailCheckMark2Request = null;
        this.mess = "Thêm hóa đơn mới thành công!";
        this.colorMess = "1";
        return "redirect:/staff/bill/home";
    }

    //phan trang
//    @GetMapping("/page-bill-detail/{number}")
//    public String getPageBillDetail(@PathVariable("number") Integer number, HttpSession session,ModelMap modelMap) {
//        System.out.println("da con trang " + session.getAttribute("numberPage"));
//        return "redirect:/bill/bill-detail/"+(Integer) session.getAttribute("IdBill");
//    }
//    @ModelAttribute("pageNumber")
//    public Integer pageNumber(HttpSession session){
//        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(this.idBillUpdate).size() / 2);
//        System.out.println("so trang la " + pageNumber);
//        return pageNumber;
//    }

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
        this.billService.save(bill);
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
        this.billService.save(bill);
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

//        if(bill.getCustomer() != null) {
//            List<ClientBillInformationResponse> clientBillInformationResponses = this.billService.getClientBillInformationResponse(bill.getCustomer().getId());
//            ClientBillInformationResponse clientBillInformationResponse = clientBillInformationResponses.get(0);
//            bill.setAddRess(clientBillInformationResponse.getAddressDetail());
//        }
        if(customerShip.trim().equals("Không có")) {
            bill.setAddRess(customerShip.trim());
        }else {
            String getAddRessDetail = customerShip.trim();
            String[] part = getAddRessDetail.split(",\\s*");
            String name = part[0];
            String numberPhone = part[1];
            String province = part[2];
            String district = part[3];
            String ward = part[4];
            String addResDetail = String.join(", ", java.util.Arrays.copyOfRange(part, 5, part.length));
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
//        if(cashAll.equals(cashEquals)) {
//            bill.setPaymentStatus(1);
//        }else {
//            bill.setPaymentStatus(0);
//        }

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
                bill.setPaymentStatus(0);
                bill.setStatus(1);
                bill.setPaymentMethod(1);
            }else {
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

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        System.out.println("tien tai khoan " + totalPrice);
        BigDecimal accountMoney = new BigDecimal(totalPrice);

        Integer returnFrom = (Integer) session.getAttribute("pageReturn");

        if(returnFrom == 1) {
            if(paymentStatus == 1) {
                this.billPay.setAcountMoney(accountMoney.divide(BigDecimal.valueOf(100)));
                this.billPay.setPaymentStatus(1);
                this.billPay.setSurplusMoney(BigDecimal.valueOf(0.00));
                this.billPay.setUpdateDate(new Date());
                this.billPay.setStatus(5);
                this.billService.save(this.billPay);
                this.setBillStatus(this.billPay.getId(),101,session);
                this.setBillStatus(this.billPay.getId(),this.billPay.getStatus(),session);

//                this.getUpdateQuantityProduct(session);
                modelMap.addAttribute("title","Tạo hóa đơn thành công!");
                return "Bill/successBill" ;
            }else {
                modelMap.addAttribute("title","Tạo hóa đơn thất bại!");
                System.out.println("Bil bo thanh toan " + this.billPay.toString());
                return "Bill/errorBill";
            }
        }else {
            if(paymentStatus == 1) {
                this.billPay = (Bill) session.getAttribute("billPaymentRest");
                this.billPay.setAcountMoney(accountMoney.divide(BigDecimal.valueOf(100)));
                this.billPay.setPaymentStatus(1);
                this.billPay.setSurplusMoney(BigDecimal.valueOf(0.00));
                this.billPay.setUpdateDate(new Date());
                this.billPay.setPaymentMethod(2);
                this.billService.save(this.billPay);

                this.setBillStatus(this.billPay.getId(),101,session);
                session.removeAttribute("billPaymentRest");
                session.removeAttribute("pageReturn");

//                this.getUpdateQuantityProduct(session);

                mess = "Thanh toán thành công!";
                colorMess = "1";
                return "redirect:/staff/bill/bill-status-index/"+session.getAttribute("IdBill");
            }else {
                return "redirect:/staff/bill/bill-status-index/"+session.getAttribute("IdBill");
            }
        }
    }

//    @GetMapping("/deleteBillDetail/{id}")
//        public String getDeleteProductDetail(@PathVariable("id") Integer id, HttpSession session) {
//        BillDetail billDetail = this.billDetailService.findById(id).orElse(new BillDetail());
//        this.billDetailService.delete(billDetail);
//        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
//        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
//        bill.setUpdateDate(new Date());
//        if(total != null) {
//            bill.setTotalAmount(total);
//        }else {
//            bill.setTotalAmount(BigDecimal.valueOf(0));
//        }
//        this.mess="Xóa sản phẩm trong hóa đơn thành công!";
//        this.colorMess="1";
//
//        getUpdateQuantityProduct(billDetail.getProductDetail().getId(),billDetail.getQuantity(),2);
//
//        this.billService.save(bill);
//        return "redirect:/bill/bill-detail/"+session.getAttribute("IdBill");
//    }

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

        thongBao.put("message","Xóa sản phẩm trong hóa đơn thành công!");
        thongBao.put("check","1");
        BillDetail billDetail = this.billDetailService.findById(Integer.parseInt(id)).orElse(null);
        this.getUpdateQuantityProduct(billDetail.getProductDetail().getId(),-billDetail.getQuantity());

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
        bill.setStaff(staffLogin);
        this.billDetailService.delete(billDetail);
        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
        bill.setUpdateDate(new Date());
        System.out.println("tong tien hoa don khi xoas " + total);
        bill.setTotalAmount(total);

        this.billService.save(bill);

        getDeleteVoucherByBill((Integer) session.getAttribute("IdBill"));

        return ResponseEntity.ok(thongBao);
    }

//    @PostMapping("/buy-product-detail")
//    public String getBuyProduct(
//            @RequestParam(name = "quantityDetail") String quantity,
//            @RequestParam(name = "idProductDetail") String idPDT,
//            HttpSession session) {
//        System.out.println("Số lượng mua: " + quantity + ", ID sản phẩm chi tiết: " + idPDT);
//
//        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(idPDT));
//
//        Bill billById = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
//
//        BillDetail billDetailSave = getBuyProduct(billById,productDetail,Integer.parseInt(quantity));
//
////        Integer idBillDetail = this.billDetailService.getBillDetailExist(billById.getId(),productDetail.getId());
////        if(idBillDetail != null) {
////            billDetailSave = this.billDetailService.findById(idBillDetail).orElse(new BillDetail());
////            billDetailSave.setQuantity(billDetailSave.getQuantity()+1);
////            billDetailSave.setTotalAmount(billDetailSave.getPrice().multiply(BigDecimal.valueOf(billDetailSave.getQuantity())));
////        }else {
////            billDetailSave = new BillDetail();
////            billDetailSave.setBill(billById);
////            billDetailSave.setProductDetail(productDetail);
////            billDetailSave.setQuantity(Integer.parseInt(quantity));
////            billDetailSave.setPrice(productDetail.getPrice());
////            billDetailSave.setTotalAmount(billDetailSave.getPrice().multiply(BigDecimal.valueOf(billDetailSave.getQuantity())));
////            billDetailSave.setStatus(1);
////        }
//        this.mess = "Thêm sản phẩm thành công!";
//        this.colorMess = "1";
//
//        getUpdateQuantityProduct(billDetailSave.getProductDetail().getId(),Integer.parseInt(quantity),1);
//
//        this.billDetailService.save(billDetailSave);
//        this.setTotalAmount(billDetailSave.getBill());
//        return "redirect:/bill/bill-detail/" + (Integer) session.getAttribute("IdBill");
//    }

    //    @GetMapping("/delete-voucher-bill")
//
//    public String getDeleteVoucherBill(HttpSession session) {
//        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
//
//        bill.setVoucher(null);
//        bill.setUpdateDate(new Date());
//        this.billService.save(bill);
//        this.mess = "Xóa voucher thành công!";
//        this.colorMess = "1";
//
//        return "redirect:/bill/bill-detail/" + (Integer) session.getAttribute("IdBill");
//    }
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
        this.getUpdateQuantityProduct(productDetail.getId(),Integer.parseInt(quantity));
        System.out.println("da mua san pham !");
        getDeleteVoucherByBill(billById.getId());

        return ResponseEntity.ok(thongBao);
    }
    //    @GetMapping("/click-voucher-bill/{idVoucher}")
//    public String getClickVoucherBill(@PathVariable("idVoucher") String idVoucher,HttpSession session) {
//        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
//
//        Voucher voucher = new Voucher();
//        voucher.setId(Integer.parseInt(idVoucher));
//        bill.setVoucher(voucher);
//        bill.setUpdateDate(new Date());
//        this.billService.save(bill);
//        this.mess = "Thêm voucher thành công!";
//        this.colorMess = "1";
//
//        return "redirect:/bill/bill-detail/" + (Integer) session.getAttribute("IdBill");
//    }
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
        this.billService.save(bill);
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

        if(bill.getVoucher() != null) {
            this.getSubtractVoucher(bill.getVoucher(),-1);
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

        bill.setVoucher(voucher);
        bill.setUpdateDate(new Date());
        bill.setStaff(staffLogin);
        this.billService.save(bill);
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

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
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
        return staff;
    }

    @ModelAttribute("customerShort")
    public CustomerShortRequest customerShortRequest(){
        return new CustomerShortRequest();
    }

}
