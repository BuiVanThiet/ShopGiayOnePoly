package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.SaleProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<SaleProduct> pageSale = saleProductService.getAllSaleProductByPage(pageable);
        Pageable pageableDelete = PageRequest.of(pageNumberDelete, pageSize);
        Page<SaleProduct> pageSaleDelete = saleProductService.getDeletedSaleProductsByPage(pageableDelete);
        List<ProductDetail> listProductDetail = saleProductService.getAllProductDetailByPage();
        model.addAttribute("pageSale", pageSale);
        model.addAttribute("pageSaleDelete", pageSaleDelete);
        model.addAttribute("listProductDetail", listProductDetail);
        return "sale_product/index";
    }

    @GetMapping("/create")
    public String getFormCreateSale(Model model) {
        SaleProductRequest saleProduct = new SaleProductRequest();
        model.addAttribute("saleProduct", saleProduct);
        return "sale_product/create";
    }

    @PostMapping("/create")
    public String createNewSale(RedirectAttributes redirectAttributes, Model model,
                                @Valid @ModelAttribute("saleProduct") SaleProductRequest saleProductRequest, BindingResult result) {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal niceTeen = new BigDecimal("91");
        BigDecimal tenHundred = new BigDecimal("10000");
        BigDecimal oneMillion = new BigDecimal("1000000");
        LocalDate dateNow = LocalDate.now();
        if (saleProductRequest.getDiscountValue() == null) {
            result.rejectValue("discountValue", "error.saleProduct", "Giá trị giảm không được để trống!");
        } else {
            if (saleProductRequest.getDiscountType() == 1) {
                if (saleProductRequest.getDiscountValue().compareTo(zero) <= 0 || saleProductRequest.getDiscountValue().compareTo(niceTeen) >= 0) {
                    result.rejectValue("discountValue", "error.saleProduct", "Giá trị giảm phải lớn hơn 0% và nhỏ hơn 90%!");
                }
            } else {
                if (saleProductRequest.getDiscountValue().compareTo(tenHundred) < 0 || saleProductRequest.getDiscountValue().compareTo(oneMillion) > 0) {
                    result.rejectValue("discountValue", "error.saleProduct", "Giá trị giảm phải lớn hơn 10.000₫ và nhỏ hơn 1.000.000₫!");
                }
            }
        }
        // Kiểm tra startDate
        if (saleProductRequest.getStartDate() == null) {
            result.rejectValue("startDate", "error.saleProduct", "Ngày bắt đầu không được để trống!");
        } else if (saleProductRequest.getStartDate().isBefore(dateNow)) {
            result.rejectValue("startDate", "error.saleProduct", "Ngày bắt đầu phải là ngày hiện tại hoặc sau ngày hiện tại: " + dateNow);
        }

        // Kiểm tra endDate
        if (saleProductRequest.getEndDate() == null) {
            result.rejectValue("endDate", "error.saleProduct", "Ngày kết thúc không được để trống!");
        } else {
            if (saleProductRequest.getEndDate().isBefore(dateNow)) {
                result.rejectValue("endDate", "error.voucher", "Ngày kết thúc phải lớn hơn ngày hiện tại: " + dateNow);
            } else if (saleProductRequest.getEndDate().isBefore(saleProductRequest.getStartDate())) {
                result.rejectValue("endDate", "error.saleProduct", "Ngày kết thúc phải lớn hơn ngày bắt đầu");
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("mes", "Thêm thất bại");
            return "sale_product/create";
        }
        saleProductService.createNewSale(saleProductRequest);
        redirectAttributes.addFlashAttribute("mes", " Thêm mới đợt giảm giá thành công");
        return "redirect:/sale-product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSaleProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        saleProductService.deleteSaleProductBySetStatus(id);
        redirectAttributes.addFlashAttribute("mes", "Xóa thành công đợt giảm giá với ID: " + id);
        return "redirect:/sale-product/list";
    }

    @GetMapping("/restore/{id}")
    public String RestoreSaleProduct(RedirectAttributes redirectAttributes,@PathVariable("id")Integer id){
        saleProductService.restoreSaleProductStatus(id);
        redirectAttributes.addFlashAttribute("mes","Khôi phục đợt giảm giá thành công");
        return "redirect:/sale-product/list";
    }
    @GetMapping("/edit/{id}")
    public String getFormUpdateSale(Model model,@PathVariable("id")Integer id){
        SaleProduct saleProduct = saleProductService.getSaleProductByID(id);
        SaleProductRequest saleProductRequest = new SaleProductRequest();
        BeanUtils.copyProperties(saleProduct,saleProductRequest);
        model.addAttribute("saleProductRequest",saleProductRequest);
        model.addAttribute("title", "CẬP NHẬT ĐỢT GIẢM GIÁ VỚI ID: " + id);
        return "sale_product/update";
    }

    @PostMapping("/update")
    public String UpdateSale(RedirectAttributes redirectAttributes, Model model,
                                @Valid @ModelAttribute("saleProductRequest") SaleProductRequest saleProductRequest, BindingResult result) {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal niceTeen = new BigDecimal("91");
        BigDecimal tenHundred = new BigDecimal("10000");
        BigDecimal oneMillion = new BigDecimal("1000000");
        LocalDate dateNow = LocalDate.now();
        if (saleProductRequest.getDiscountValue() == null) {
            result.rejectValue("discountValue", "error.saleProduct", "Giá trị giảm không được để trống!");
        } else {
            if (saleProductRequest.getDiscountType() == 1) {
                if (saleProductRequest.getDiscountValue().compareTo(zero) <= 0 || saleProductRequest.getDiscountValue().compareTo(niceTeen) >= 0) {
                    result.rejectValue("discountValue", "error.saleProduct", "Giá trị giảm phải lớn hơn 0% và nhỏ hơn 90%!");
                }
            } else {
                if (saleProductRequest.getDiscountValue().compareTo(tenHundred) < 0 || saleProductRequest.getDiscountValue().compareTo(oneMillion) > 0) {
                    result.rejectValue("discountValue", "error.saleProduct", "Giá trị giảm phải lớn hơn 10.000₫ và nhỏ hơn 1.000.000₫!");
                }
            }
        }
        // Kiểm tra startDate
        if (saleProductRequest.getStartDate() == null) {
            result.rejectValue("startDate", "error.saleProduct", "Ngày bắt đầu không được để trống!");
        } else if (saleProductRequest.getStartDate().isBefore(dateNow)) {
            result.rejectValue("startDate", "error.saleProduct", "Ngày bắt đầu phải là ngày hiện tại hoặc sau ngày hiện tại: " + dateNow);
        }

        // Kiểm tra endDate
        if (saleProductRequest.getEndDate() == null) {
            result.rejectValue("endDate", "error.saleProduct", "Ngày kết thúc không được để trống!");
        } else {
            if (saleProductRequest.getEndDate().isBefore(dateNow)) {
                result.rejectValue("endDate", "error.voucher", "Ngày kết thúc phải lớn hơn ngày hiện tại: " + dateNow);
            } else if (saleProductRequest.getEndDate().isBefore(saleProductRequest.getStartDate())) {
                result.rejectValue("endDate", "error.saleProduct", "Ngày kết thúc phải lớn hơn ngày bắt đầu");
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("mes", "Cập nhật thất bại");
            return "sale_product/update";
        }
        SaleProduct saleProduct = saleProductService.getSaleProductByID(saleProductRequest.getId());
        saleProductRequest.setCreateDate(saleProduct.getCreateDate());
        saleProductService.createNewSale(saleProductRequest);
        redirectAttributes.addFlashAttribute("mes", "Cập nhật đợt giảm giá với ID "+saleProductRequest.getId()+" thành công");
        return "redirect:/sale-product/list";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
