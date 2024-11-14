package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.TransactionCheckRequest;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.TransactionVNPayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transactionVNPay-api")
public class TransactionVNPayRestController {
    @Autowired
    TransactionVNPayService transactionVNPayService;
    TransactionCheckRequest transactionCheckRequest = null;
    @GetMapping("/list")
    public List<Object[]> getListTransaction(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        // Nếu transactionCheckRequest là null, khởi tạo mặc định
        if(transactionCheckRequest == null) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // Lấy ngày hiện tại dưới dạng chuỗi
            String[] bankCodeList = null;
            transactionCheckRequest = new TransactionCheckRequest(
                    null, "", currentDate, currentDate, "", null, "");
        }

        // Gọi phương thức dịch vụ và truyền các tham số đúng
        List<Object[]> transactions = transactionVNPayService.getAllTransactionVNPay(transactionCheckRequest);

        // In ra kết quả (nếu cần)
        for (Object[] objects : transactions) {
            System.out.println("------");
            System.out.println(objects[0]);
        }

        // Phân trang
        Pageable pageable = PageRequest.of(0, 5);
        return convertListToPage(transactions, pageable).getContent();
    }
    @PostMapping("/search-transaction")
    public String getSearchTransaction(@RequestBody TransactionCheckRequest transactionCheckRequest2, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(transactionCheckRequest2.getTransactionStatus().equals("--Tất cả--")) {
            transactionCheckRequest2.setTransactionStatus(null);
        }
        System.out.println(transactionCheckRequest2.toString());
        transactionCheckRequest = transactionCheckRequest2;
        return "done";
    }

    @PostMapping("/search-transaction-test")
    public List<Object[]> getSearchTransactionTest(@RequestBody TransactionCheckRequest transactionCheckRequest2, HttpSession session) {
        System.out.println(transactionCheckRequest2.toString());
        transactionCheckRequest = transactionCheckRequest2;
        return transactionVNPayService.getAllTransactionVNPay(transactionCheckRequest);
    }

    @GetMapping("/reset-search-transaction")
    public String getResetSearchTransaction(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        transactionCheckRequest = null;
        return "done";

    }

    @GetMapping("/max-page-transaction")
    public Integer getMaxPageTransaction(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(transactionCheckRequest == null) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // Lấy ngày hiện tại dưới dạng chuỗi
            String[] bankCodeList = null;
            transactionCheckRequest = new TransactionCheckRequest(
                    null, "", currentDate, currentDate, "", null, "");
        }
        List<Object[]> transactions = transactionVNPayService.getAllTransactionVNPay(transactionCheckRequest);
        Integer pageNumber = (int) Math.ceil((double) transactions.size() / 5);
        return pageNumber;
    }


    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}
