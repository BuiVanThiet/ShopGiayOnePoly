package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.response.VoucherResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    private final int pageSize = 5;

    @GetMapping("/list")
    public String getListVoucherByPage(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                       @RequestParam(name = "pageNumberDelete", defaultValue = "0") Integer pageNumberDelete,
                                       @RequestParam(name = "pageNumberExpired", defaultValue = "0") Integer pageNumberExpired,
                                       Model model) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Voucher> pageVoucher = voucherService.getAllVoucherByPage(pageable);

        Pageable pageableDelete = PageRequest.of(pageNumberDelete, pageSize);
        Page<Voucher> pageVoucherDelete = voucherService.getAllVoucherDeleteByPage(pageableDelete);

        Pageable pageableExpired = PageRequest.of(pageNumberExpired, pageSize);
        Page<Voucher> pageVoucherExpired = voucherService.getVoucherExpiredByPage(pageableExpired);

        model.addAttribute("pageVoucher", pageVoucher);
        model.addAttribute("pageVoucherDelete", pageVoucherDelete);
        model.addAttribute("pageVoucherExpired", pageVoucherExpired);
        model.addAttribute("voucher", new VoucherRequest());
        return "voucher/index";
    }


    @PostMapping("/create")
    public String createNewVoucher(@Valid @ModelAttribute("voucher") VoucherRequest voucherRequest, BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                   @RequestParam(name = "pageNumberDelete", defaultValue = "0") Integer pageNumberDelete,
                                   @RequestParam(name = "pageNumberExpired", defaultValue = "0") Integer pageNumberExpired) {

        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal niceTeen = new BigDecimal("91");
        BigDecimal tenHundred = new BigDecimal("10000");
        BigDecimal oneMillion = new BigDecimal("1000000");
        LocalDate dateNow = LocalDate.now();

// Kiểm tra priceReduced
        if (voucherRequest.getPriceReduced() == null) {
            result.rejectValue("priceReduced", "error.voucher", "Giá trị giảm không được để trống!");
        } else {
            if (voucherRequest.getDiscountType() == 1) { // Loại giảm giá là phần trăm
                if (voucherRequest.getPriceReduced().compareTo(zero) <= 0 || voucherRequest.getPriceReduced().compareTo(niceTeen) >= 0) {
                    result.rejectValue("priceReduced", "error.voucher", "Giá trị giảm phải lớn hơn 0% và nhỏ hơn 90%!");
                }
            } else { // Loại giảm giá là tiền mặt
                if (voucherRequest.getPriceReduced().compareTo(tenHundred) < 0 || voucherRequest.getPriceReduced().compareTo(oneMillion) > 0) {
                    result.rejectValue("priceReduced", "error.voucher", "Giá trị giảm phải lớn hơn 10.000₫ và nhỏ hơn 1.000.000₫!");
                }
            }
        }

// Kiểm tra startDate
        if (voucherRequest.getStartDate() == null) {
            result.rejectValue("startDate", "error.voucher", "Ngày bắt đầu không được để trống!");
        } else if (voucherRequest.getStartDate().isBefore(dateNow)) {
            result.rejectValue("startDate", "error.voucher", "Ngày bắt đầu phải là ngày hiện tại hoặc sau ngày hiện tại: " + dateNow);
        }

// Kiểm tra endDate
        if (voucherRequest.getEndDate() == null) {
            result.rejectValue("endDate", "error.voucher", "Ngày kết thúc không được để trống!");
        } else {
            if (voucherRequest.getEndDate().isBefore(dateNow)) {
                result.rejectValue("endDate", "error.voucher", "Ngày kết thúc phải lớn hơn ngày hiện tại: " + dateNow);
            } else if (voucherRequest.getEndDate().isBefore(voucherRequest.getStartDate())) {
                result.rejectValue("endDate", "error.voucher", "Ngày kết thúc phải lớn hơn ngày bắt đầu");
            }
        }

// Kiểm tra pricesApply và pricesMax
        if (voucherRequest.getPricesApply() != null && voucherRequest.getPricesMax() != null && voucherRequest.getPricesApply().compareTo(voucherRequest.getPricesMax()) < 0) {
            result.rejectValue("pricesApply", "error.voucher", "Giá trị áp dụng phải lớn hơn giá trị giảm tối đa!");
        }
        if (result.hasErrors()) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Voucher> pageVoucher = voucherService.getAllVoucherByPage(pageable);
            model.addAttribute("pageVoucher", pageVoucher);

            Pageable pageableDelete = PageRequest.of(pageNumberDelete, pageSize);
            Page<Voucher> pageVoucherDelete = voucherService.getAllVoucherByPage(pageableDelete);
            model.addAttribute("pageVoucherDelete", pageVoucherDelete);

            Pageable pageableExpired = PageRequest.of(pageNumberExpired, pageSize);
            Page<Voucher> pageVoucherExpired = voucherService.getVoucherExpiredByPage(pageableExpired);
            model.addAttribute("pageVoucherExpired", pageVoucherExpired);
            return "voucher/index";
        }
        voucherService.createNewVoucher(voucherRequest);
        redirectAttributes.addFlashAttribute("mes", " Thêm phiếu giảm giá thành công với mã là: " + voucherRequest.getCodeVoucher());
        return "redirect:/voucher/list";
    }


    @GetMapping("/delete/{id}")
    public String deleteVoucher(RedirectAttributes ra, @PathVariable("id") Integer id) {
        voucherService.deleteVoucher(id);
        ra.addFlashAttribute("mes", "Xóa thành công phiếu giảm giá với ID là: " + id);
        return "redirect:/voucher/list";
    }

    @GetMapping("/restore/{id}")
    public String restoreVoucher(RedirectAttributes ra, @PathVariable("id") Integer id) {
        voucherService.restoreStatusVoucher(id);
        ra.addFlashAttribute("mes", "Khôi phục thành công phiếu giảm giá với ID là: " + id);
        return "redirect:/voucher/list";
    }

    @GetMapping("/edit/{id}")
    public String getFormUpdate(Model model, @PathVariable("id") Integer id) {
        Voucher voucher = voucherService.getOne(id);
        VoucherRequest voucherRequest = new VoucherRequest();
        BeanUtils.copyProperties(voucher, voucherRequest);
        model.addAttribute("voucher", voucherRequest);
        model.addAttribute("title", "UPDATE VOUCHER WITH ID: " + id);
        return "voucher/update";
    }

    @PostMapping("/update")
    public String updateVoucher(RedirectAttributes redirectAttributes, @Valid @ModelAttribute("voucher") VoucherRequest voucherRequest,
                                BindingResult result, Model model) {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal oneHundred = new BigDecimal("90");
        BigDecimal tenHundred = new BigDecimal("10000");
        BigDecimal oneMillion = new BigDecimal("1000000");
        LocalDate dateNow = LocalDate.now();

// Kiểm tra priceReduced
        if (voucherRequest.getPriceReduced() == null) {
            result.rejectValue("priceReduced", "error.voucher", "Giá trị giảm không được để trống!");
        } else {
            if (voucherRequest.getDiscountType() == 1) { // Loại giảm giá là phần trăm
                if (voucherRequest.getPriceReduced().compareTo(zero) <= 0 || voucherRequest.getPriceReduced().compareTo(oneHundred) >= 0) {
                    result.rejectValue("priceReduced", "error.voucher", "Giá trị giảm phải lớn hơn 0% và nhỏ hơn 100%!");
                }
            } else { // Loại giảm giá là tiền mặt
                if (voucherRequest.getPriceReduced().compareTo(tenHundred) < 0 || voucherRequest.getPriceReduced().compareTo(oneMillion) > 0) {
                    result.rejectValue("priceReduced", "error.voucher", "Giá trị giảm phải lớn hơn 10.000₫ và nhỏ hơn 1.000.000₫!");
                }
            }
        }

// Kiểm tra startDate
        if (voucherRequest.getStartDate() == null) {
            result.rejectValue("startDate", "error.voucher", "Ngày bắt đầu không được để trống!");
        } else if (voucherRequest.getStartDate().isBefore(dateNow)) {
            result.rejectValue("startDate", "error.voucher", "Ngày bắt đầu phải là ngày hiện tại hoặc sau ngày hiện tại: " + dateNow);
        }

// Kiểm tra endDate
        if (voucherRequest.getEndDate() == null) {
            result.rejectValue("endDate", "error.voucher", "Ngày kết thúc không được để trống!");
        } else {
            if (voucherRequest.getEndDate().isBefore(dateNow)) {
                result.rejectValue("endDate", "error.voucher", "Ngày kết thúc phải lớn hơn ngày hiện tại: " + dateNow);
            } else if (voucherRequest.getEndDate().isBefore(voucherRequest.getStartDate())) {
                result.rejectValue("endDate", "error.voucher", "Ngày kết thúc phải lớn hơn ngày bắt đầu");
            }
        }

// Kiểm tra pricesApply và pricesMax
        if (voucherRequest.getPricesApply() != null && voucherRequest.getPricesMax() != null && voucherRequest.getPricesApply().compareTo(voucherRequest.getPricesMax()) < 0) {
            result.rejectValue("pricesApply", "error.voucher", "Giá trị áp dụng phải lớn hơn giá trị giảm tối đa!");
        }
        if (result.hasErrors()) {
            model.addAttribute("title", "UPDATE VOUCHER WITH ID: " + voucherRequest.getId());
            return "voucher/update";
        }

        Voucher existingVoucher = voucherService.getOne(voucherRequest.getId());
        voucherRequest.setCreateDate(existingVoucher.getCreateDate());

        voucherService.updateVoucher(voucherRequest);
        redirectAttributes.addFlashAttribute("mes", "Update voucher successfully with ID: " + voucherRequest.getId());
        return "redirect:/voucher/list";
    }

    @GetMapping("/search-key")
    public String searchVoucherByKey(@RequestParam("key") String key, Model model,
                                     @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                     @RequestParam(name = "pageNumberDelete", defaultValue = "0") Integer pageNumberDelete,
                                     @RequestParam(name = "pageNumberExpired", defaultValue = "0") Integer pageNumberExpired) {

        Pageable pageableSearch = PageRequest.of(pageNumber, pageSize);
        Page<Voucher> pageVoucher = voucherService.searchVoucherByKeyword(key, pageableSearch);

        Pageable pageableDelete = PageRequest.of(pageNumberDelete, pageSize);
        Page<Voucher> pageVoucherDelete = voucherService.getAllVoucherDeleteByPage(pageableDelete);

        Pageable pageableExpired = PageRequest.of(pageNumberExpired, pageSize);
        Page<Voucher> pageVoucherExpired = voucherService.getVoucherExpiredByPage(pageableExpired);

        model.addAttribute("pageVoucher", pageVoucher);
        model.addAttribute("pageVoucherDelete", pageVoucherDelete);
        model.addAttribute("voucherExpired", pageableExpired);
        model.addAttribute("voucher", new VoucherRequest());
        return "voucher/index";
    }

    @GetMapping("/search-type")
    public String searchVouchersByType(
            @RequestParam(name = "type", required = false) String type,
            Model model,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageNumberDelete", defaultValue = "0") Integer pageNumberDelete,
            @RequestParam(name = "pageNumberExpired", defaultValue = "0") Integer pageNumberExpired) {

        Pageable pageableSearch = PageRequest.of(pageNumber, pageSize);
        Page<Voucher> pageVoucher;

        if (type == null || type.isEmpty()) {
            pageVoucher = voucherService.getAllVoucherByPage(pageableSearch);
        } else {
            int typeInt = Integer.parseInt(type);
            pageVoucher = voucherService.searchVoucherByTypeVoucher(typeInt, pageableSearch);
        }

        Pageable pageableDelete = PageRequest.of(pageNumberDelete, pageSize);
        Page<Voucher> pageVoucherDelete = voucherService.getAllVoucherDeleteByPage(pageableDelete);

        Pageable pageableExpired = PageRequest.of(pageNumberExpired, pageSize);
        Page<Voucher> pageVoucherExpired = voucherService.getVoucherExpiredByPage(pageableExpired);

        model.addAttribute("pageVoucher", pageVoucher);
        model.addAttribute("pageVoucherDelete", pageVoucherDelete);
        model.addAttribute("pageVoucherExpired", pageVoucherExpired);
        model.addAttribute("voucher", new VoucherRequest());
        model.addAttribute("type", type);

        return "voucher/index";
    }

    @GetMapping("/extend/{id}")
    public String extendVoucherExpired(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        voucherService.updateVoucherExpired(id);
        redirectAttributes.addFlashAttribute("mes", "Gia hạn phiếu giảm giá thành công");
        return "redirect:/voucher/list";
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void updateExpiredVouchersStatus() {
        System.out.println("Running scheduled task to update voucher status");
        voucherService.updateVoucherStatusForExpired();
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

    @GetMapping("/detail/{id}")
    public String getDetailVoucherByID(@PathVariable("id") Integer id, Model model) {
        Voucher voucher = voucherService.getOne(id);
        model.addAttribute("voucher", voucher);
        model.addAttribute("title", "Voucher Detail ID " + id);
        return "voucher/detail";
    }


}
