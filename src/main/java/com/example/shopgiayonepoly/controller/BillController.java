package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.service.BillDetailService;
import com.example.shopgiayonepoly.service.BillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    BillService billService;
    @Autowired
    BillDetailService billDetailService;
    String mess = "";
    String colorMess = "";
    //    Integer pageNumber = 0;
//    String nameSearch = "";
    @GetMapping("/home")
    public String getForm(ModelMap modelMap) {
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        this.mess = "";
        this.colorMess = "";
        modelMap.addAttribute("page","/Bill/index");
        return "Bill/index";
    }
    @GetMapping("/bill-detail/{idBill}")
    public String getBillDetail(@PathVariable("idBill") Integer idBill, ModelMap modelMap, HttpSession session) {
        if (idBill != null) {
            session.setAttribute("IdBill", idBill);
            System.out.println(session.getId());
        } else {
            System.out.println("idBill là null, không thể lưu vào session.");
        }
//        modelMap.addAttribute("listBillDetail",this.billDetailImplement.getBillDetailByIdBill(idBill));
        modelMap.addAttribute("page","/Bill/index");
        return "Bill/index";
    }
    @GetMapping("/create")
    public String getCreateBill(ModelMap modelMap) {
        Pageable pageable = PageRequest.of(0,5);
        List<Bill> listB = this.billService.getBillByStatusNew(pageable);
        System.out.println(listB.size());
        if(listB.size() >= 4) {
            this.mess = "Thêm bill thất bại, chỉ đợc tồn tại 5 bill mới!";
            this.colorMess = "3";
            return "redirect:/bill/home";
        }
        Bill billSave = new Bill();
        billSave.setPaymentStatus(0);
        billSave.setStatus(0);
        Bill bill = this.billService.save(billSave);
        System.out.printf(bill.toString());
        bill.setCodeBill("HD"+bill.getId().toString());
        bill.setUpdateDate(bill.getUpdateDate());
        bill.setCreateDate(bill.getCreateDate());
        this.billService.save(bill);
        this.mess = "Thêm bill thành công!";
        this.colorMess = "1";
        return "redirect:/bill/home";
    }

    //them san pham bang qr

}
