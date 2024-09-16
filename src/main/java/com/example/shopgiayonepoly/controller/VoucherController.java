package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @GetMapping("/page")
    public String getListVoucherByPage(Model model, @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Voucher> pageVoucher = voucherService.getAllVoucherByPage(pageable);
        model.addAttribute("pageVoucher", pageVoucher);
        return "voucher/index";
    }

    @PostMapping("/create")
    public String createNewVoucher(Model model,@Valid VoucherRequest voucherRequest, BindingResult result){
        if(result.hasErrors()){
            return "";
        }
        voucherService.createNewVoucher(voucherRequest);
        model.addAttribute("mes","Add new voucher successfully with ID: "+voucherRequest.getId());
        return "redirect:/voucher/page";
    }
    @PostMapping("/update")
    public String updateVoucher(Model model,@Valid VoucherRequest voucherRequest, BindingResult result){
        if(result.hasErrors()){
            return "";
        }
        voucherService.updateVoucher(voucherRequest);
        model.addAttribute("mes","Update voucher successfully with ID: "+voucherRequest.getId());
        return "redirect:/voucher/page";
    }
    @GetMapping("/delete/{id}")
    public String deleteVoucher(Model model, @PathVariable("id")Integer id){
        voucherService.deleteVoucher(id);
        model.addAttribute("mes","Delete voucher successfully with ID: "+id);
        return "redirect:/voucher/page";
    }
}
