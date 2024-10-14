package com.example.shopgiayonepoly.controller.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.response.bill.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.entites.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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

        this.mess = "";
        this.colorMess = "";

        modelMap.addAttribute("page","/Bill/index");

        System.out.println("nhung san pham  ap dung dot giam gia");
        for (ProductDetail productDetail: saleProductService.getAllProductDetailWithDiscount()) {
            System.out.println("id" + productDetail.getId());
        }

        System.out.println("nhung san pham chua ap dung dot giam gia");
        for (ProductDetail productDetail: saleProductService.getAllProductDetailByPage()) {
            System.out.println("id" + productDetail.getId());
        }
//        displayProductDetailsWithCurrentPrice();
        return "Bill/index";

    }
    @GetMapping("/bill-detail/{idBill}")
    public String getBillDetail(@PathVariable("idBill") Integer idBill, ModelMap modelMap, HttpSession session) {
        session.setAttribute("IdBill", idBill);
        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill.getCustomer() != null){
            session.setAttribute("IdClient", bill.getCustomer().getId());
        }else {
            session.setAttribute("IdClient", null);
        }
        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(idBill).size() / 2);
        modelMap.addAttribute("bill",this.billService.findById((Integer)session.getAttribute("IdBill")).orElse(new Bill()));
        modelMap.addAttribute("client",(Integer)session.getAttribute("IdClient"));
        modelMap.addAttribute("clientInformation",bill.getCustomer());

        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        System.out.println("mes hien len la " + mess);
        this.mess = "";
        this.colorMess  = "";

        getDeleteVoucherByBill(idBill);

        return "Bill/index";
    }
    @GetMapping("/create")
    public String getCreateBill(ModelMap modelMap,HttpSession session) {
        Pageable pageable = PageRequest.of(0,10);
        List<Bill> listB = this.billService.getBillByStatusNew(pageable);
        System.out.println(listB.size());
        if(listB.size() >= 10) {
            this.mess = "Thêm hóa đơn thất bại, chỉ được tồn tại 10 hóa đơn mới!";
            this.colorMess = "3";
            return "redirect:/staff/bill/home";
        }
        Bill billSave = new Bill();
        Staff staff = (Staff) session.getAttribute("staffLogin");
        billSave.setStaff(staff);
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
    public ResponseEntity<Map<String,String>> getAddClientInBill(@PathVariable("idClient") Integer idClient, HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setUpdateDate(new Date());
        Customer customer = new Customer();
        Map<String,String> thongBao = new HashMap<>();
        thongBao.put("message","Thêm khách hàng thành công!");
        thongBao.put("check","1");
        customer.setId(idClient);
        bill.setCustomer(customer);
        this.billService.save(bill);
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/removeClientInBill/{idClient}")
    @ResponseBody
    public ResponseEntity<Map<String,String>>  getRemoveClientInBill(@PathVariable("idClient") Integer idClient, HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setUpdateDate(new Date());
        Map<String,String> thongBao = new HashMap<>();
        thongBao.put("message","Xóa khách hàng thành công!");
        thongBao.put("check","1");
//        Client client = new Client();
//        client.setId(idClient);
        bill.setCustomer(null);
        this.billService.save(bill);
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/pay-ment/{idBill}")
    public String getPayMent(
            @PathVariable("idBill") Integer id,
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

        BigDecimal cashNumber = new BigDecimal(cash.replaceAll(",", ""));
        BigDecimal surplusMoneyNumber = new BigDecimal(surplusMoney);
        BigDecimal shipMoneyNumber = new BigDecimal(shipMoney);

        Bill bill = this.billService.findById(id).orElse(null);

//        if(bill.getCustomer() != null) {
//            List<ClientBillInformationResponse> clientBillInformationResponses = this.billService.getClientBillInformationResponse(bill.getCustomer().getId());
//            ClientBillInformationResponse clientBillInformationResponse = clientBillInformationResponses.get(0);
//            bill.setAddRess(clientBillInformationResponse.getAddressDetail());
//        }
        bill.setAddRess(customerShip.trim());

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
                if(bill.getNote().length() < 0 || bill.getNote() == null || bill.getNote().trim().equals("")) {
                    bill.setNote("Thanh toán bằng tiền mặt!");
                }
            }
            System.out.println("Thong tin bill(TM)" + bill.toString());
            if(bill.getBillType() == 1) {
                this.setBillStatus(bill.getId(),101,session);
            }
            this.setBillStatus(bill.getId(),bill.getStatus(),session);

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
    public  ResponseEntity<Map<String,String>> getDeleteProductDetail(@PathVariable("id") Integer id, HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        thongBao.put("message","Xóa sản phẩm trong hóa đơn thành công!");
        thongBao.put("check","1");
        BillDetail billDetail = this.billDetailService.findById(id).orElse(new BillDetail());
        this.getUpdateQuantityProduct(billDetail.getProductDetail().getId(),-billDetail.getQuantity());
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
        bill.setUpdateDate(new Date());
        if(total != null) {
            bill.setTotalAmount(total);
        }else {
            bill.setTotalAmount(BigDecimal.valueOf(0));
        }
        this.billDetailService.delete(billDetail);
        this.billService.save(bill);

        getDeleteVoucherByBill(bill.getId());

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

        thongBao.put("message","Thêm sản phẩm vào hóa đơn thành công!");
        thongBao.put("check","1");

        System.out.println("Số lượng mua: " + quantity + ", ID sản phẩm chi tiết: " + idPDT);

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(idPDT));

        Bill billById = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());

        BillDetail billDetailSave = getBuyProduct(billById,productDetail,Integer.parseInt(quantity));

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
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        this.getSubtractVoucher(bill.getVoucher(),-1);
        Map<String,String> thongBao = new HashMap<>();
        thongBao.put("message","Xóa mã giảm giá thành công!");
        thongBao.put("check","1");
        bill.setVoucher(null);
        bill.setUpdateDate(new Date());
        this.billService.save(bill);
        return ResponseEntity.ok(thongBao);
    }


    @PostMapping("/click-voucher-bill/{idVoucher}")
    @ResponseBody
    public ResponseEntity<Map<String,String>> getClickVoucherBill(@PathVariable("idVoucher") String idVoucher, HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        if(bill.getVoucher() != null) {
            this.getSubtractVoucher(bill.getVoucher(),-1);
        }
        Map<String,String> thongBao = new HashMap<>();
        thongBao.put("message","Thêm mã giảm giá thành công!");
        thongBao.put("check","1");
        Voucher voucher = this.voucherService.getOne(Integer.parseInt(idVoucher));
        bill.setVoucher(voucher);
        bill.setUpdateDate(new Date());
        this.billService.save(bill);
        this.getSubtractVoucher(voucher,1);
        return ResponseEntity.ok(thongBao);
    }

    //danh cho quan ly hoa don
    @GetMapping("/bill-status-index/{idBill}")
    public String getStatusBill(@PathVariable("idBill") Integer idBill,HttpSession session, ModelMap modelMap) {
        session.setAttribute("IdBill",idBill);
        modelMap.addAttribute("client", (Integer) session.getAttribute("IdClient"));
        modelMap.addAttribute("bill",(Integer) session.getAttribute("IdBill") == null ? new Bill() : this.billService.findById((Integer)session.getAttribute("IdBill")).orElse(new Bill()));
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        session.setAttribute("pageReturn",2);
        mess = "";
        colorMess = "";
        return "Bill/billInformationIndex";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

}
