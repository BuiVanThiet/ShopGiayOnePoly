package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.SaleProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sale-product")
public class SaleProductController {
    @Autowired
    private SaleProductService saleProductService;

    private static final int pageSize = 5;

    @GetMapping("/list")
    public String getFormListSaleProduct(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                         @RequestParam(name = "pageNumberDelete", defaultValue = "0") int pageNumberDelete,
                                         Model model) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<SaleProduct> pageSale=saleProductService.getAllSaleProductByPage(pageable);
        Pageable pageableDelete  = PageRequest.of(pageNumberDelete,pageSize);
        Page<SaleProduct> pageSaleDelete =saleProductService.getDeletedSaleProductsByPage(pageableDelete);
        SaleProduct saleProduct = new SaleProduct();
        model.addAttribute("saleProduct", saleProduct);
        model.addAttribute("pageSale",pageSale);
        model.addAttribute("pageSaleDelete",pageSaleDelete);
        return "sale_product/index";
    }
    @GetMapping("/delete/{id}")
    public String deleteSaleProduct(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes){
        saleProductService.deleteSaleProductBySetStatus(id);
        redirectAttributes.addFlashAttribute("mes","Xóa thành công đợt giảm giá với ID: "+id);
        return "redirect:/sale-product/list";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
