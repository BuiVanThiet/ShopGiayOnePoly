package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.Client;
import com.example.shopgiayonepoly.service.BillDetailService;
import com.example.shopgiayonepoly.service.BillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    Integer currentPage = 1;
    @GetMapping("/home")
    public String getForm(ModelMap modelMap, HttpSession session) {
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        Pageable pageable = PageRequest.of(0,5);
        List<Bill> billList =  billService.getBillByStatusNew(pageable);
        if(billList.size() > 0) {
            Bill bill = billList.stream().reduce(billList.get(0),(max,id)->{
                if(id.getId() > max.getId()) {
                    return id;
                }
                return max;
            });
            session.setAttribute("IdBill", bill.getId());
            if(bill.getClient() != null){
                session.setAttribute("IdClient", bill.getClient().getId());
            }else {
                session.setAttribute("IdClient", null);
            }
            Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(bill.getId()).size() / 2);
            modelMap.addAttribute("pageNumber", pageNumber);
        }else {
            session.setAttribute("IdBill", null);
            modelMap.addAttribute("IdClient",null);


        }
        session.setAttribute("numberPage",0);
        modelMap.addAttribute("bill",(Integer)session.getAttribute("IdBill"));
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
        if(bill.getClient() != null){
            session.setAttribute("IdClient", bill.getClient().getId());
        }else {
            session.setAttribute("IdClient", null);
        }
        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(idBill).size() / 2);
        modelMap.addAttribute("pageNumber", pageNumber);
        modelMap.addAttribute("currentPage",currentPage);
        currentPage = 1;
        modelMap.addAttribute("bill",(Integer)session.getAttribute("IdBill"));
        modelMap.addAttribute("client",(Integer)session.getAttribute("IdClient"));

        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        System.out.println("mes hien len la " + mess);
//        this.mess = "";
//        this.colorMess  = "";
        return "Bill/index";
    }
    @GetMapping("/create")
    public String getCreateBill(ModelMap modelMap,HttpSession session) {
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
        session.setAttribute("IdBill",bill.getId());
        session.setAttribute("IdClient",null);
        this.mess = "Thêm bill thành công!";
        this.colorMess = "1";
        return "redirect:/bill/home";
    }

    //phan trang
    @GetMapping("/page-bill-detail/{number}")
    public String getPageBillDetail(@PathVariable("number") Integer number, HttpSession session,ModelMap modelMap) {
        session.setAttribute("numberPage",number);
        System.out.println("da con trang " + session.getAttribute("numberPage"));
        this.currentPage = number;
        return "redirect:/bill/bill-detail/"+(Integer) session.getAttribute("IdBill");
    }
//    @ModelAttribute("pageNumber")
//    public Integer pageNumber(HttpSession session){
//        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(this.idBillUpdate).size() / 2);
//        System.out.println("so trang la " + pageNumber);
//        return pageNumber;
//    }

    @GetMapping("/addClientInBill/{idClient}")
    public String getAddClientInBill(@PathVariable("idClient") Integer idClient, HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setUpdateDate(new Date());
        Client client = new Client();
        client.setId(idClient);
        bill.setClient(client);
        this.billService.save(bill);
        this.mess = "Thêm khách hàng thành công!";
        this.colorMess = "1";
        System.out.println("Them thanh cong khach hang co id la "+idClient+" tai hoa don " + session.getAttribute("IdBill"));
        return "redirect:/bill/bill-detail/"+(Integer) session.getAttribute("IdBill");
    }

    @GetMapping("/removeClientInBill/{idClient}")
    public String getRemoveClientInBill(@PathVariable("idClient") Integer idClient, HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setUpdateDate(new Date());
//        Client client = new Client();
//        client.setId(idClient);
        bill.setClient(null);
        this.billService.save(bill);
        this.mess = "Xóa khách hàng thành công!";
        this.colorMess = "1";
        System.out.println("Xoa thanh cong khach hang co id la "+idClient+" tai hoa don " + session.getAttribute("IdBill"));
        return "redirect:/bill/bill-detail/"+(Integer) session.getAttribute("IdBill");
    }

}
