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
    @GetMapping("/list")
    public String getListVoucherByPage(Model model, @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Voucher> pageVoucher = voucherService.getAllVoucherByPage(pageable);
        model.addAttribute("pageVoucher",pageVoucher);
        model.addAttribute("voucher", new VoucherRequest());
        return "voucher/index";
    }


    @PostMapping("/create")
    public String createNewVoucher(@Valid @ModelAttribute("voucher") VoucherRequest voucherRequest, BindingResult result, Model model, @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber) {
        if (result.hasErrors()) {
            int pageSize = 5;
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
            Page<Voucher> pageVoucher = voucherService.getAllVoucherByPage(pageable);
            model.addAttribute("pageVoucher", pageVoucher);
            return "voucher/index";
        }
        voucherService.createNewVoucher(voucherRequest);
        model.addAttribute("mes", "Add new voucher successfully with ID: " + voucherRequest.getId());
        return "redirect:/voucher/list";
    }

    @GetMapping("/detail/{id}")
    public String getFormUpdate(Model model,@PathVariable("id")Integer id){
        Voucher voucher= voucherService.getOne(id);
        model.addAttribute("voucher",voucher);
        model.addAttribute("title","UPDATE VOUCHER WITH ID: "+id);
        return "voucher/update";
    }

    @PostMapping("/update")
    public String updateVoucher(Model model,@Valid VoucherRequest voucherRequest, BindingResult result){
        if(result.hasErrors()){
            return "voucher/update";
        }
        voucherService.updateVoucher(voucherRequest);
        model.addAttribute("mes","Update voucher successfully with ID: "+voucherRequest.getId());
        return "redirect:/voucher/list";
    }
    @GetMapping("/delete/{id}")
    public String deleteVoucher(Model model, @PathVariable("id")Integer id){
        voucherService.deleteVoucher(id);
        model.addAttribute("mes","Xóa thành công phiếu giảm giá với ID là: "+id);
        return "redirect:/voucher/list";
    }
}
