package com.example.shopgiayonepoly.restController.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.bill.ReturnBillDetailRequest;
import com.example.shopgiayonepoly.dto.response.bill.ReturnBillDetailResponse;
import com.example.shopgiayonepoly.dto.response.bill.ReturnBillResponse;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.ReturnBill;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/return-bill")
public class ReturnBillRestController extends BaseBill {
    Integer quantity = 0;
    Integer idProductDetail = 0;
    private List<BillDetail> billDetailList;

    @GetMapping("/bill-detail/{page}")
    public List<BillDetail> getListBillDetailByIdBill(@PathVariable("page") Integer page,HttpSession session) {
        Pageable pageable = PageRequest.of(page-1,2);
        System.out.println("session la " + session.getAttribute("IdBill"));
        billDetailList = this.billDetailService.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"));
        int index = getReturnBillDetailResponseIndex(idProductDetail);
        for (BillDetail billDetail: billDetailList) {
            if(billDetail.getProductDetail().getId() == idProductDetail) {
                if(index != -1) {
                    BillDetail detail = billDetailList.get(index);
                    detail.setQuantity(detail.getQuantity()-quantity);
                    detail.setTotalAmount(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
                    System.out.println("da vao day de giam so luong");
                    billDetailList.set(index,detail);
                }
            }
        }
        return getConvertListToPage(billDetailList,pageable).getContent();
    }
    @GetMapping("/max-page-bill-detail")
    public Integer getMaxPageBillDetail(HttpSession session) {
        System.out.println("session la " + session.getAttribute("IdBill"));
        billDetailList = this.billDetailService.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"));
        Integer page = (int) Math.ceil((double) this.billDetailList.size() / 2);
        return page;
    }

    @GetMapping("/bill-return-detail")
    public List<ReturnBillDetailResponse> getListReturnBillDetail(HttpSession session) {
        returnBillDetailResponses = (List<ReturnBillDetailResponse>) session.getAttribute("returnBillDetailResponses");
        if(returnBillDetailResponses == null) {
            returnBillDetailResponses =  new ArrayList<>();
        }
//        System.out.println(returnBillDetailResponse.getProductDetail().toString());
//        System.out.println(returnBillDetailResponse.getProductDetail().getProduct().toString());
        return this.returnBillDetailResponses;
    }

    @GetMapping("/max-page-return-bill")
    public Integer getMaxPageReturnBill(HttpSession session) {
        returnBillDetailResponses = (List<ReturnBillDetailResponse>) session.getAttribute("returnBillDetailResponses");
        if(returnBillDetailResponses == null) {
            returnBillDetailResponses = new ArrayList<>();
        }
        Integer page = (int) Math.ceil((double) this.returnBillDetailResponses.size() / 2);
        return page;
    }

    @PostMapping("/add-product-in-return-bill")
    public ResponseEntity<Map<String, String>> getAddProductInReturnBill(@RequestBody ReturnBillDetailRequest request, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        ProductDetail productDetail = this.billDetailService.getProductDetailById(request.getIdProductDetail());

        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách trả lại hay chưa
        int index = getReturnBillDetailResponseIndex(productDetail.getId());

        if (index == -1) {
            // Nếu sản phẩm chưa tồn tại, kiểm tra số lượng trước khi thêm sản phẩm mới
            Boolean checkQuantity = returnBillDetailResponse(request.getIdProductDetail(), request.getQuantityReturn());
            if (checkQuantity == true) {
                thongBao.put("message", "Vượt quá số lượng trả!");
                thongBao.put("check", "3");
            } else {
                // Thêm sản phẩm mới vào danh sách trả lại
                ReturnBillDetailResponse newReturnBillDetailResponse = new ReturnBillDetailResponse();
                newReturnBillDetailResponse.setProductDetail(productDetail);
                newReturnBillDetailResponse.setQuantityReturn(request.getQuantityReturn());
                newReturnBillDetailResponse.setPriceBuy(request.getPriceBuy());
                newReturnBillDetailResponse.setTotalReturn(request.getPriceBuy().multiply(BigDecimal.valueOf(request.getQuantityReturn())));

                this.returnBillDetailResponses.add(newReturnBillDetailResponse);
                session.setAttribute("returnBillDetailResponses", returnBillDetailResponses); // Cập nhật session

                thongBao.put("message", "Chọn sản phẩm thành công!");
                thongBao.put("check", "1");
                idProductDetail = productDetail.getId();
                quantity = newReturnBillDetailResponse.getQuantityReturn();
            }
        } else {
            // Nếu sản phẩm đã tồn tại, tính tổng số lượng mới trước khi cập nhật
            ReturnBillDetailResponse existingReturnBillDetailResponse = returnBillDetailResponses.get(index);
            int newQuantity = existingReturnBillDetailResponse.getQuantityReturn() + request.getQuantityReturn();
            System.out.println(newQuantity);
            // Kiểm tra số lượng mới trước khi cộng dồn
            Boolean checkQuantity = returnBillDetailResponse(existingReturnBillDetailResponse.getProductDetail().getId(), quantity);
            if (checkQuantity == true) {
                thongBao.put("message", "Vượt quá số lượng trả!");
                thongBao.put("check", "3");
            } else {
                // Cập nhật số lượng và tổng tiền của sản phẩm
                existingReturnBillDetailResponse.setQuantityReturn(newQuantity);
                existingReturnBillDetailResponse.setTotalReturn(existingReturnBillDetailResponse.getPriceBuy().multiply(BigDecimal.valueOf(newQuantity)));

                // Cập nhật lại phần tử trong danh sách
                returnBillDetailResponses.set(index, existingReturnBillDetailResponse);
                thongBao.put("message", "Cập nhật số lượng sản phẩm thành công!");
                thongBao.put("check", "1");
                idProductDetail = productDetail.getId();
                quantity = newQuantity;
            }
        }

        return ResponseEntity.ok(thongBao);
    }


    @GetMapping("/reset-return-bill-detail")
    public ResponseEntity<?> getResetReturnBill(HttpSession session) {
        session.setAttribute("returnBillDetailResponses", null); // Reset lại dữ liệu trong session mỗi lần tải trang
        return ResponseEntity.ok("done");
    }

    public int getReturnBillDetailResponseIndex(Integer idProduct) {
        for (int i = 0; i < returnBillDetailResponses.size(); i++) {
            if (returnBillDetailResponses.get(i).getProductDetail().getId().equals(idProduct)) {
                return i; // Trả về chỉ số của sản phẩm nếu tìm thấy
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    public boolean returnBillDetailResponse(Integer idProduct, Integer quantity) {
        for (BillDetail billDetail : billDetailList) {
            // So sánh ID của sản phẩm
            if (billDetail.getProductDetail().getId().equals(idProduct)) {
                // Kiểm tra nếu số lượng trong kho ít hơn số lượng yêu cầu
                if (billDetail.getQuantity() < quantity) {
                    System.out.println("so luong trong bill " + billDetail.getQuantity());
                    System.out.println("so luong nhap vao " + quantity);
                    System.out.println("sai yeu cau!");
                    return true; // Vượt quá số lượng
                } else {
                    System.out.println("so luong trong bill " + billDetail.getQuantity());
                    System.out.println("so luong nhap vao " + quantity);
                    System.out.println("dung yeu cau tra!");
                    return false; // Số lượng hợp lệ (không vượt quá)
                }
            }
        }
        System.out.println("dung yeu cau tra!");
        // Trường hợp không tìm thấy sản phẩm trong danh sách
        return false;
    }

    @GetMapping("/infomation-return-bill")
    public ReturnBillResponse getInfomationReturnBill(HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        ReturnBillResponse returnBillResponse = new ReturnBillResponse();
        returnBillResponse.setBill(bill);
        BigDecimal total = BigDecimal.ZERO;  // Khởi tạo tổng bằng 0
        // Duyệt qua tất cả các phần tử trong danh sách
        for (ReturnBillDetailResponse response : returnBillDetailResponses) {
            if (response != null) { // Kiểm tra tránh lỗi NullPointerException
                total = total.add(response.getTotalReturn());  // Cộng dồn totalReturn
            }
        }
        returnBillResponse.setTotalReturn(total);
        return returnBillResponse;
    }

    protected Page<BillDetail> getConvertListToPage(List<BillDetail> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<BillDetail> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }



}
