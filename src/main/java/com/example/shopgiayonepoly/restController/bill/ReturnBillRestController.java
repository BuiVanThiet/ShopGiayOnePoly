package com.example.shopgiayonepoly.restController.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.bill.ReturnBillDetailRequest;
import com.example.shopgiayonepoly.dto.response.bill.InfomationReturnBillResponse;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/return-bill-api")
public class ReturnBillRestController extends BaseBill {
    Integer quantity = 0;
    Integer idProductDetail = 0;
    BigDecimal totalReturn = BigDecimal.valueOf(0);
    private List<BillDetail> billDetailList;

    @GetMapping("/bill-detail/{page}")
    public List<BillDetail> getListBillDetailByIdBill(@PathVariable("page") Integer page,HttpSession session) {
        Pageable pageable = PageRequest.of(page-1,2);
        System.out.println("session la " + session.getAttribute("IdBill"));
        if(billDetailList == null) {
            System.out.println("neu list null vao day");
            billDetailList = this.billDetailService.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"));
        }
        return getConvertListToPageBillDetail(billDetailList,pageable).getContent();
    }
    @GetMapping("/max-page-bill-detail")
    public Integer getMaxPageBillDetail(HttpSession session) {
        System.out.println("session la " + session.getAttribute("IdBill"));
        if(billDetailList == null) {
            System.out.println("neu list null vao day");
            billDetailList = this.billDetailService.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"));
        }
        Integer page = (int) Math.ceil((double) this.billDetailList.size() / 2);
        return page;
    }

    @GetMapping("/bill-return-detail/{page}")
    public List<ReturnBillDetailResponse> getListReturnBillDetail(@PathVariable("page") Integer pageNumber,HttpSession session) {
        returnBillDetailResponses = (List<ReturnBillDetailResponse>) session.getAttribute("returnBillDetailResponses");
        if(returnBillDetailResponses == null) {
            returnBillDetailResponses =  new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(pageNumber-1,2);
//        System.out.println(returnBillDetailResponse.getProductDetail().toString());
//        System.out.println(returnBillDetailResponse.getProductDetail().getProduct().toString());
        return getConvertListToPageReturnBill(returnBillDetailResponses,pageable).getContent();
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
//                BigDecimal discountRatio = (BigDecimal) session.getAttribute("discountRatioPercentage");
//                BigDecimal priReturn = (BigDecimal)  request.getPriceBuy().multiply((BigDecimal.valueOf(1).subtract(discountRatio)));
//                newReturnBillDetailResponse.setPriceBuy(priReturn.setScale(2, RoundingMode.CEILING));
                BigDecimal discountRatio = (BigDecimal) session.getAttribute("discountRatioPercentage");
                // Tính giá sau khi giảm giá
                BigDecimal priReturn = request.getPriceBuy().multiply(BigDecimal.valueOf(1).subtract(discountRatio));
                // Làm tròn về bội số của 500
                BigDecimal roundedPrice = priReturn.divide(BigDecimal.valueOf(500), 0, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(500));
                // Set giá trị đã làm tròn vào đối tượng response
                newReturnBillDetailResponse.setPriceBuy(roundedPrice);
//                newReturnBillDetailResponse.setPriceBuy(request.getPriceBuy());
//                newReturnBillDetailResponse.setTotalReturn(request.getPriceBuy().multiply(BigDecimal.valueOf(request.getQuantityReturn())));
                newReturnBillDetailResponse.setTotalReturn(roundedPrice.multiply(BigDecimal.valueOf(request.getQuantityReturn())));

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
        int indexUpdateBill = getBillDetailResponseIndex(idProductDetail);
        for (BillDetail billDetail: billDetailList) {
            if(billDetail.getProductDetail().getId() == idProductDetail) {
                if(indexUpdateBill != -1) {
                    BillDetail detail = billDetailList.get(indexUpdateBill);
                    detail.setQuantity(detail.getQuantity()-quantity);
                    detail.setTotalAmount(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
                    System.out.println("da vao day de giam so luong");
                    billDetailList.set(indexUpdateBill,detail);
                }
            }
        }
        totalReturn = BigDecimal.valueOf(0);
        for (ReturnBillDetailResponse returnBillDetailResponse : returnBillDetailResponses) {
                totalReturn = totalReturn.add(returnBillDetailResponse.getTotalReturn());
        }
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/remove-product-in-return-bill/{idProduct}/{quantity}")
    public ResponseEntity<Map<String, String>> getRemoveProductInReturnBill(@PathVariable("idProduct") Integer idProductDetailRequest,@PathVariable("quantity") Integer quantity, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        int index = getReturnBillDetailResponseIndex(idProductDetailRequest);

        if(index != -1) {
            this.returnBillDetailResponses.remove(index);
            List<BillDetail> billDetails = this.billDetailService.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"));
            for (BillDetail detail: billDetails) {
                if(detail.getProductDetail().getId() == idProductDetailRequest) {
                    int indexUpdateBill = getBillDetailResponseIndex(idProductDetailRequest);
                    for (BillDetail billDetail: billDetailList) {
                        if(billDetail.getProductDetail().getId() == idProductDetail) {
                            if(indexUpdateBill != -1) {
                                BillDetail detailUpdate = billDetailList.get(indexUpdateBill);
                                detailUpdate.setQuantity(detail.getQuantity());
                                detailUpdate.setTotalAmount(detail.getTotalAmount());
                                System.out.println("da khoi phuc so luong");
                                thongBao.put("message", "Xóa sản phẩm trả thành công!");
                                thongBao.put("check", "1");
                                billDetailList.set(indexUpdateBill,detailUpdate);
                            }
                        }
                    }
                }
            }

            totalReturn = BigDecimal.valueOf(0);
            for (ReturnBillDetailResponse returnBillDetailResponse : returnBillDetailResponses) {
                totalReturn = totalReturn.add(returnBillDetailResponse.getTotalReturn());
            }
            return ResponseEntity.ok(thongBao);
        }else {
            thongBao.put("message", "Sai định dạng, xóa thất bại!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
    }

    @GetMapping("/infomation-return-bill")
    public InfomationReturnBillResponse getInfomationReturnBill(HttpSession session) {
        List<Object[]> objects = this.billService.getInfomationBillReturn((Integer) session.getAttribute("IdBill"));
        InfomationReturnBillResponse response = new InfomationReturnBillResponse();
        for (int i = 0; i<1;i++) {
            Object[] objectSave = objects.get(i);
            response.setCodeBill((String) objectSave[1]);
            response.setNameCustomer((String) objectSave[2]);
            response.setDiscount((BigDecimal) objectSave[3]);
            response.setDiscountRatioPercentage((BigDecimal) objectSave[4]);
            response.setQuantityBuy((Integer) objectSave[5]);
        }
        response.setTotalReturn(totalReturn);
        session.setAttribute("discountRatioPercentage", response.getDiscountRatioPercentage().divide(BigDecimal.valueOf(100))); // Reset lại dữ liệu trong session mỗi lần tải trang
        System.out.println(response.toString());
        return response;
    }

    @GetMapping("/reset-return-bill-detail")
    public ResponseEntity<?> getResetReturnBill(HttpSession session) {
        session.setAttribute("returnBillDetailResponses", null); // Reset lại dữ liệu trong session mỗi lần tải trang
        session.setAttribute("totalMoneyReturn", 0); // Reset lại dữ liệu trong session mỗi lần tải trang
        session.setAttribute("discountRatioPercentage", 0); // Reset lại dữ liệu trong session mỗi lần tải trang
        billDetailList = null;
        quantity = 0;
        idProductDetail = 0;
        totalReturn = BigDecimal.valueOf(0);
        return ResponseEntity.ok("done");
    }

    //cong tru so luong tra
    @GetMapping("/increase-or-decrease-product-return/{idProductReturn}/{quantity}/{method}")
    public ResponseEntity<Map<String, String>> getIncreaseOrDecreaseProductReturn(
            @PathVariable("idProductReturn") Integer idProductReturn,
            @PathVariable("quantity") Integer quantityReturn,
            @PathVariable("method") String method
    ) {
        Map<String, String> thongBao = new HashMap<>();
        int index = getReturnBillDetailResponseIndex(idProductReturn);
        if(index != -1) {

        }else {
            thongBao.put("message", "Khong ton tai!");
            thongBao.put("check", "3");
        }
        return ResponseEntity.ok(thongBao);
    }

    public int getReturnBillDetailResponseIndex(Integer idProduct) {
        for (int i = 0; i < returnBillDetailResponses.size(); i++) {
            if (returnBillDetailResponses.get(i).getProductDetail().getId() == idProduct) {
                System.out.println(i);
                return i; // Trả về chỉ số của sản phẩm nếu tìm thấy
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    public int getBillDetailResponseIndex(Integer idProduct) {
        for (int i = 0; i < billDetailList.size(); i++) {
            if (billDetailList.get(i).getProductDetail().getId() == idProduct) {
                System.out.println(i);
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
    protected Page<BillDetail> getConvertListToPageBillDetail(List<BillDetail> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<BillDetail> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

    protected Page<ReturnBillDetailResponse> getConvertListToPageReturnBill(List<ReturnBillDetailResponse> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<ReturnBillDetailResponse> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}
