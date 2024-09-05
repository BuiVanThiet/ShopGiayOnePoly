package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.implement.BillDetailImplement;
import com.example.shopgiayonepoly.implement.BillImplement;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bill-api")
public class BillRestController {
    @Autowired
    BillImplement billImplement;
    @Autowired
    BillDetailImplement billDetailImplement;

    @GetMapping("/all")
    public List<Bill> getAll() {
        return billImplement.findAll();
    }

    @GetMapping("/all-new")
    public List<Bill> getAllNew() {
        Pageable pageable = PageRequest.of(0,5);
        return billImplement.getBillByStatusNew(pageable);
    }

    @GetMapping("/bill-detail-by-id-bill")
    public List<BillDetail> getBillDetail(HttpSession session) {
        System.out.println("sesion la" + session.getAttribute("IdBill"));
        Pageable pageable = PageRequest.of(0,5);
        return this.billDetailImplement.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"),pageable);
    }



}
