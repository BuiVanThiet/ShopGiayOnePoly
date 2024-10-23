package com.example.shopgiayonepoly.controller.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/staff/return-bill")
public class ReturnBillController extends BaseBill {
    @GetMapping("/bill/{idBill}")
    public String getIndexReturnBill(@PathVariable("idBill") Integer idBill,HttpSession session) {
        this.productDetailCheckMark2Request = null;
        session.setAttribute("IdBill",idBill);
        return "Bill/returnBill";
    }
    @GetMapping("/create-return-bill")
    public String getCreateReturnBill(ModelMap modelMap) {
        modelMap.addAttribute("title","Tạo phiếu trả hàng thành công!");
        return "Bill/successBill";
    }
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
