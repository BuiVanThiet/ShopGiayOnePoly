package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.TransactionCheckRequest;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.ExcelExportService;
import com.example.shopgiayonepoly.service.TransactionVNPayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/transactionVNPay-api")
public class TransactionVNPayRestController {
    @Autowired
    TransactionVNPayService transactionVNPayService;
    TransactionCheckRequest transactionCheckRequest = null;
    @GetMapping("/list/{page}")
    public List<Object[]> getListTransaction(@PathVariable("page") Integer page,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
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

        // Phân trang
        Pageable pageable = PageRequest.of(page-1, 5);
        return convertListToPage(transactions, pageable).getContent();
    }

    @GetMapping("/sum-total")
    public Map<String,BigDecimal> getSumTransaction(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        Map<String,BigDecimal> data = new HashMap<>();
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
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
        // Tính tổng giá trị từ cột thứ 8 (giả sử là số dạng BigDecimal hoặc Double)
        BigDecimal sum = BigDecimal.ZERO;
        Integer sl = 0;
        for (Object[] objects : transactions) {
            Integer total = Integer.parseInt((String) objects[8]);
            sum = sum.add(BigDecimal.valueOf(total));
            sl++;
        }
        data.put("total",sum);
        data.put("quantity",new BigDecimal(sl));
        // Trả về tổng dưới dạng chuỗi
        return data;
    }

    @PostMapping("/search-transaction")
    public String getSearchTransaction(@RequestBody TransactionCheckRequest transactionCheckRequest2, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        if(transactionCheckRequest2.getTransactionStatus().equals("--Tất cả--")) {
            transactionCheckRequest2.setTransactionStatus(null);
        }
        transactionCheckRequest = transactionCheckRequest2;
        return "done";
    }

    @PostMapping("/search-transaction-test")
    public List<Object[]> getSearchTransactionTest(@RequestBody TransactionCheckRequest transactionCheckRequest2, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        transactionCheckRequest = transactionCheckRequest2;
        return transactionVNPayService.getAllTransactionVNPay(transactionCheckRequest);
    }

    @GetMapping("/reset-search-transaction")
    public String getResetSearchTransaction(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
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
        if(staffLogin.getStatus() != 1) {
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
    @GetMapping("/excel-export-transaction")
    public ResponseEntity<Map<String,String>> getExportTransaction(HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        List<Object[]> transactions = transactionVNPayService.getAllTransactionVNPay(transactionCheckRequest);
        List<Object[]> tranSactionExportList = new ArrayList<>();
        Object[] objectTransactionAdd;
        for (Object[] objects: transactions) {
            objectTransactionAdd = new Object[9];
            objectTransactionAdd[0] = objects[0]; //Mã GD
            objectTransactionAdd[1] = objects[1]; //Số hóa đơn
            objectTransactionAdd[2] = objects[10]; //Mã GD Bank
            objectTransactionAdd[3] = objects[4]; //Nội dung
            objectTransactionAdd[4] = objects[3]; //Ngân hàng
            objectTransactionAdd[5] = objects[2]; //Số tiền(không phải để cộng)
            objectTransactionAdd[6] = objects[5]; // Trạng thái
            objectTransactionAdd[7] = objects[6]; // "Ngày tạo
            tranSactionExportList.add(objectTransactionAdd);
        }
        ExcelExportService excelExportService = new ExcelExportService();

        ExcelExportService service = new ExcelExportService();
        try {
            service.exportListToExcel(tranSactionExportList);
            thongBao.put("message","Xuất file pdf thành công!");
            thongBao.put("check","1");
        } catch (IOException e) {
            e.printStackTrace();
            thongBao.put("message","Xuất file pdf không thành công!");
            thongBao.put("check","3");
        }
        return ResponseEntity.ok(thongBao);
    }

    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}
