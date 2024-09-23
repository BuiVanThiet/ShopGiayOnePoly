package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.response.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.dto.response.ClientBillInformationResponse;
import com.example.shopgiayonepoly.dto.response.VoucherResponse;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.BillDetailService;
import com.example.shopgiayonepoly.service.BillService;
import com.example.shopgiayonepoly.service.VNPayService;
import com.example.shopgiayonepoly.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    BillService billService;
    @Autowired
    BillDetailService billDetailService;
    @Autowired
    VNPayService vnPayService;
    @Autowired
    VoucherService voucherService;

    Bill billPay;
    String mess = "";
    String colorMess = "";
    @GetMapping("/home")
    public String getForm(ModelMap modelMap, HttpSession session) {
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        Pageable pageable = PageRequest.of(0,10);
        List<Bill> billList =  billService.getBillByStatusNew(pageable);
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
            return "redirect:/bill/home";
        }
        Bill billSave = new Bill();
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
        this.billService.save(bill);
        session.setAttribute("IdBill",bill.getId());
        session.setAttribute("IdClient",null);
        this.mess = "Thêm hóa đơn mới thành công!";
        this.colorMess = "1";
        return "redirect:/bill/home";
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
    public ResponseEntity<String> getAddClientInBill(@PathVariable("idClient") Integer idClient, HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setUpdateDate(new Date());
        Customer customer = new Customer();
        customer.setId(idClient);
        bill.setCustomer(customer);
        this.billService.save(bill);
        this.mess = "Thêm khách hàng thành công!";
        this.colorMess = "1";
        System.out.println("Them thanh cong khach hang co id la "+idClient+" tai hoa don " + session.getAttribute("IdBill"));
        return ResponseEntity.ok("Thêm khách hàng thành công!");
    }

    @GetMapping("/removeClientInBill/{idClient}")
    @ResponseBody
    public ResponseEntity<String>  getRemoveClientInBill(@PathVariable("idClient") Integer idClient, HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setUpdateDate(new Date());
//        Client client = new Client();
//        client.setId(idClient);
        bill.setCustomer(null);
        this.billService.save(bill);
        this.mess = "Xóa khách hàng thành công!";
        this.colorMess = "1";
        System.out.println("Xoa thanh cong khach hang co id la "+idClient+" tai hoa don " + session.getAttribute("IdBill"));
        return ResponseEntity.ok("Xóa khách hàng thành công!");
    }

    @PostMapping("/pay-ment/{idBill}")
    public String getPayMent(
            @PathVariable("idBill") Integer id,
            @RequestParam("cash") String cash,
            @RequestParam("note") String note,
            @RequestParam("shipMoney") String shipMoney,
            @RequestParam("surplusMoney") String surplusMoney,
            @RequestParam("cashAccount") String cashAccount,
            HttpSession session,
            HttpServletRequest request) {

        BigDecimal cashNumber = new BigDecimal(cash.replaceAll(",", ""));
        BigDecimal surplusMoneyNumber = new BigDecimal(surplusMoney);
        BigDecimal shipMoneyNumber = new BigDecimal(shipMoney);

        Bill bill = this.billService.findById(id).orElse(null);

        if(bill.getCustomer() != null) {
            List<ClientBillInformationResponse> clientBillInformationResponses = this.billService.getClientBillInformationResponse(bill.getCustomer().getId());
            ClientBillInformationResponse clientBillInformationResponse = clientBillInformationResponses.get(0);
            bill.setAddRess(clientBillInformationResponse.getAddressDetail());
        }

        BillTotalInfornationResponse billTotalInfornationResponse = this.billService.findBillVoucherById(bill.getId());
        BigDecimal cashAll = bill.getCash().add(bill.getAcountMoney().add(bill.getShippingPrice()));
        BigDecimal cashEquals = billTotalInfornationResponse.getFinalAmount().setScale(2, RoundingMode.HALF_UP);
        if(cashAll.equals(cashEquals)) {
            bill.setPaymentStatus(1);
        }else {
            bill.setPaymentStatus(0);
        }

        bill.setUpdateDate(new Date());

        bill.setNote(note);

        bill.setCash(cashNumber.setScale(2,RoundingMode.FLOOR));
        bill.setSurplusMoney(surplusMoneyNumber.setScale(2,RoundingMode.FLOOR));
        bill.setShippingPrice(shipMoneyNumber);

        if(bill.getPaymentMethod() == 1 || bill.getBillType() == 2) {
            bill.setPaymentStatus(1);
            bill.setUpdateDate(new Date());
            bill.setStatus(4);
            System.out.println("Thong tin bill(TM)" + bill.toString());
//            this.billService.save(bill);
            if(bill.getVoucher() != null) {
                this.getSubtractVoucher(bill.getVoucher());
            }
            return "Bill/successBill";
        }else if (bill.getPaymentMethod() == 2) {
            if(bill.getNote().length() < 0 || bill.getNote() == null || bill.getNote().trim().equals("")) {
                bill.setNote("chuyen khoan");
            }
            bill.setCash(BigDecimal.valueOf(0));
            bill.setSurplusMoney(BigDecimal.valueOf(0));
            this.billPay = bill;
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder((Integer.parseInt(cashAccount)+Integer.parseInt(shipMoney)), bill.getNote(), baseUrl);
            return "redirect:" + vnpayUrl;
        }else {
            this.billPay = bill;
            boolean isGreaterThanZero = bill.getSurplusMoney().compareTo(BigDecimal.ZERO) > 0;

            if(isGreaterThanZero == true) {
                bill.setPaymentStatus(1);
                bill.setUpdateDate(new Date());
                System.out.println("Do nhap qua so tien mat nen khong the tao thanh toan online");
                System.out.println("Thong tin Bill thanh toan bang tien va tk(1)" + this.billPay.toString());
//                this.billService.save(bill);
                return "Bill/successBill";
            }else {
                if(bill.getNote().length() < 0 || bill.getNote() == null || bill.getNote().trim().equals("")) {
                    bill.setNote("chuyen khoan va tien mat");
                }
                bill.setSurplusMoney(BigDecimal.valueOf(0));
                this.billPay = bill;
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnPayService.createOrder((Integer.parseInt(cashAccount)+Integer.parseInt(shipMoney)), bill.getNote(), baseUrl);
                System.out.println("Thong tin Bill thanh toan bang tien va tk(2)" + this.billPay.toString());
                return "redirect:" + vnpayUrl;
            }
        }
    }


    //thanh toan bang vnpay
    @GetMapping("/vnpay-payment")
    public String getVNpay(HttpServletRequest request, Model model, HttpSession session){
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
        this.billPay.setAcountMoney(accountMoney.divide(BigDecimal.valueOf(100)));
        this.billPay.setPaymentStatus(1);
        this.billPay.setSurplusMoney(BigDecimal.valueOf(0.00));
        this.billPay.setUpdateDate(new Date());
        System.out.println("Thong tin thanh toan bang the " + this.billPay.toString());
        if(paymentStatus == 1) {
            this.billPay.setStatus(4);
//            this.billService.save(this.billPay);
            if(this.billPay.getVoucher() != null) {
                this.getSubtractVoucher(this.billPay.getVoucher());
            }

            return "Bill/successBill" ;
        }else {
            Bill bill = this.billPay;
            bill.setShippingPrice(BigDecimal.valueOf(0));
            bill.setCash(BigDecimal.valueOf(0));
            bill.setAcountMoney(BigDecimal.valueOf(0));
            bill.setBillType(1);
            bill.setPaymentStatus(0);
            bill.setSurplusMoney(BigDecimal.valueOf(0));
//            bill.setStatus(0);
            bill.setUpdateDate(new Date());
//            this.billService.save(bill);
            return "Bill/errorBill";
        }
    }

    @GetMapping("/deleteBillDetail/{id}")
        public String getDeleteProductDetail(@PathVariable("id") Integer id, HttpSession session) {
        BillDetail billDetail = this.billDetailService.findById(id).orElse(new BillDetail());
        this.billDetailService.delete(billDetail);
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
        bill.setUpdateDate(new Date());
        if(total != null) {
            bill.setTotalAmount(total);
        }else {
            bill.setTotalAmount(BigDecimal.valueOf(0));
        }
        this.mess="Xóa sản phẩm trong hóa đơn thành công!";
        this.colorMess="1";
        this.billService.save(bill);
        return "redirect:/bill/bill-detail/"+session.getAttribute("IdBill");
    }

    @PostMapping("/buy-product-detail")
    public String getBuyProduct(
            @RequestParam(name = "quantityDetail") String quantity,
            @RequestParam(name = "idProductDetail") String idPDT,
            HttpSession session) {
        System.out.println("Số lượng mua: " + quantity + ", ID sản phẩm chi tiết: " + idPDT);

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(idPDT));

        Bill billById = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());

        BillDetail billDetailSave;

        Integer idBillDetail = this.billDetailService.getBillDetailExist(billById.getId(),productDetail.getId());
        if(idBillDetail != null) {
            billDetailSave = this.billDetailService.findById(idBillDetail).orElse(new BillDetail());
            billDetailSave.setQuantity(billDetailSave.getQuantity()+1);
            billDetailSave.setTotalAmount(billDetailSave.getPrice().multiply(BigDecimal.valueOf(billDetailSave.getQuantity())));
        }else {
            billDetailSave = new BillDetail();
            billDetailSave.setBill(billById);
            billDetailSave.setProductDetail(productDetail);
            billDetailSave.setQuantity(Integer.parseInt(quantity));
            billDetailSave.setPrice(productDetail.getPrice());
            billDetailSave.setTotalAmount(billDetailSave.getPrice().multiply(BigDecimal.valueOf(billDetailSave.getQuantity())));
            billDetailSave.setStatus(1);
        }
        this.mess = "Thêm sản phẩm thành công!";
        this.colorMess = "1";
        this.billDetailService.save(billDetailSave);
        this.setTotalAmount(billDetailSave.getBill());
        return "redirect:/bill/bill-detail/" + (Integer) session.getAttribute("IdBill");
    }

    @GetMapping("/delete-voucher-bill")
    public String getDeleteVoucherBill(HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);

        bill.setVoucher(null);
        bill.setUpdateDate(new Date());
        this.billService.save(bill);
        this.mess = "Xóa voucher thành công!";
        this.colorMess = "1";

        return "redirect:/bill/bill-detail/" + (Integer) session.getAttribute("IdBill");
    }

    @GetMapping("/click-voucher-bill/{idVoucher}")
    public String getClickVoucherBill(@PathVariable("idVoucher") String idVoucher,HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);

        Voucher voucher = new Voucher();
        voucher.setId(Integer.parseInt(idVoucher));
        bill.setVoucher(voucher);
        bill.setUpdateDate(new Date());
        this.billService.save(bill);
        this.mess = "Thêm voucher thành công!";
        this.colorMess = "1";

        return "redirect:/bill/bill-detail/" + (Integer) session.getAttribute("IdBill");
    }


    //tính tổng tiền cho hóa đơn
    public void setTotalAmount(Bill bill) {
        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
        if(total != null) {
            bill.setUpdateDate(new Date());
            bill.setTotalAmount(total);
        }else {
            bill.setUpdateDate(new Date());
            bill.setTotalAmount(BigDecimal.valueOf(0));
        }
        System.out.println("Thong tin bill: " + bill.toString());

        this.billService.save(bill);
    }

    //trừ đi voucher cua hóa đơn
    public void getSubtractVoucher(Voucher voucher) {
//            voucher.setQuantity(voucher.getQuantity() - 1);
            voucher.setUpdateDate(new Date());
            VoucherRequest voucherRequest = new VoucherRequest();
            BeanUtils.copyProperties(voucher,voucherRequest);
            System.out.println(voucherRequest.toString());
            System.out.println(voucherRequest.getId() + "" + voucherRequest.getCreateDate() + "" + voucherRequest.getUpdateDate());
            System.out.println(voucher.getId() + "" + voucher.getCreateDate() + "" + voucher.getUpdateDate());
//            this.voucherService.updateVoucher(voucherRequest);
    }




}
