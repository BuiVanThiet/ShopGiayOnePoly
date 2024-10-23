package com.example.shopgiayonepoly.restController.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.bill.BillDetailAjax;
import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckMark2Request;
import com.example.shopgiayonepoly.dto.response.bill.ExchangeBillDetailResponse;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.ProductDetail;
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
@RequestMapping("/exchange-bill-api")
public class ExchangeBillRestController extends BaseBill {
    private List<Object[]> productDetails;
    @GetMapping("/reset-return-bill-detail")
    public ResponseEntity<?> getResetReturnBill(HttpSession session) {
        session.setAttribute("exchangeBillDetailResponses", null); // Reset lại dữ liệu trong session mỗi lần tải trang
        session.setAttribute("totalMoneyExchange", 0); // Reset lại dữ liệu trong session mỗi lần tải trang
        productDetails = null;
        return ResponseEntity.ok("done");
    }

    @GetMapping("/bill-exchange-detail/{page}")
    public List<ExchangeBillDetailResponse> getListExchangeBillDetail(@PathVariable("page") Integer pageNumber, HttpSession session) {
        exchangeBillDetailResponses = (List<ExchangeBillDetailResponse>) session.getAttribute("exchangeBillDetailResponses");
        if(exchangeBillDetailResponses == null) {
            exchangeBillDetailResponses =  new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(pageNumber-1,2);
        return getConvertListToPageExchangeBill(exchangeBillDetailResponses,pageable).getContent();
    }

    @GetMapping("/max-page-exchange-bill")
    public Integer getMaxPageExchangeBill(HttpSession session) {
        exchangeBillDetailResponses = (List<ExchangeBillDetailResponse>) session.getAttribute("exchangeBillDetailResponses");
        if(exchangeBillDetailResponses == null) {
            exchangeBillDetailResponses = new ArrayList<>();
        }
        Integer page = (int) Math.ceil((double) this.exchangeBillDetailResponses.size() / 2);
        return page;
    }

    @GetMapping("/product-detaill-sell/{page}")
    public List<Object[]> getProductDetailSell(@PathVariable("page") Integer pageNumber, HttpSession session) {
        Pageable pageable = PageRequest.of(pageNumber-1,4);
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        if(this.productDetails == null) {
            this.productDetails = this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,(Integer) session.getAttribute("IdBill"));
        }
        System.out.println("Số lượng 1 trang la " + productDetails.size());
        return convertListToPage(productDetails,pageable).getContent();
    }

    @GetMapping("/page-max-product")
    public Integer getMaxPageProduct(HttpSession session) {
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        Integer maxPageProduct = (int) Math.ceil((double) this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,(Integer) session.getAttribute("IdBill")).size() / 4);
        System.out.println("so trang cua san pham " + maxPageProduct);
        return maxPageProduct;
    }
    @PostMapping("/filter-product-deatail")
    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckMark2Request productDetailCheckRequest2, HttpSession session) {
        this.productDetailCheckMark2Request = productDetailCheckRequest2;
        System.out.println("Thong tin loc " + productDetailCheckRequest2.toString());
        return ResponseEntity.ok("Done");
    }
    @PostMapping("/exchange")
    public ResponseEntity<Map<String,String>> getBuyProduct(
            @RequestParam(name = "quantityDetail") String quantity,
            @RequestParam(name = "idProductDetail") String idPDT,
            @RequestParam(name = "priceProductSale") String priceProductSale,
            @RequestParam(name = "priceProductRoot") String priceProductRoot,
            HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        thongBao.put("message","Thêm sản phẩm vào phiếu đơn đổi thành công!");
        thongBao.put("check","1");
        ExchangeBillDetailResponse exchangeBillDetailResponse;

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(idPDT));
        Integer indexExchange = getIndexExchangeBillDetail(Integer.parseInt(idPDT));
        if(indexExchange == -1) {
            exchangeBillDetailResponse = new ExchangeBillDetailResponse();
            exchangeBillDetailResponse.setProductDetail(productDetail);
            exchangeBillDetailResponse.setQuantityExchange(Integer.parseInt(quantity));
            exchangeBillDetailResponse.setPriceAtTheTimeOfExchange(BigDecimal.valueOf(Long.parseLong(priceProductSale)));
            exchangeBillDetailResponse.setTotalExchange(exchangeBillDetailResponse.getPriceAtTheTimeOfExchange().multiply(BigDecimal.valueOf(exchangeBillDetailResponse.getQuantityExchange())));
            exchangeBillDetailResponse.setPriceRootAtTime(BigDecimal.valueOf(Long.parseLong(priceProductRoot)));
            this.exchangeBillDetailResponses.add(exchangeBillDetailResponse);
        }else {
            exchangeBillDetailResponse = exchangeBillDetailResponses.get(indexExchange);
            exchangeBillDetailResponse.setQuantityExchange(exchangeBillDetailResponse.getQuantityExchange()+Integer.parseInt(quantity));
            exchangeBillDetailResponse.setTotalExchange(exchangeBillDetailResponse.getPriceAtTheTimeOfExchange().multiply(BigDecimal.valueOf(exchangeBillDetailResponse.getQuantityExchange())));
            this.exchangeBillDetailResponses.set(indexExchange,exchangeBillDetailResponse);
        }
        //cap nhat lai so luong san pham
        getReduceProductDetail(productDetail.getId(),Integer.parseInt(quantity));

        session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);
        System.out.println("Số lượng đổi: " + quantity + ", ID sản phẩm chi tiết: " + idPDT);

        session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);

        totalExchange = BigDecimal.valueOf(0);
        for (ExchangeBillDetailResponse response: exchangeBillDetailResponses) {
            totalExchange = totalExchange.add(response.getTotalExchange());
        }
        System.out.println("tien doi la " + totalExchange);

        return ResponseEntity.ok(thongBao);
    }

    //xoa di san pham doi
    @GetMapping("/remove-exchange-product/{id}/{quantity}")
    public ResponseEntity<Map<String,String>> getRemoveProductExchange(
            @PathVariable("id") String idProductDetail,
            @PathVariable("quantity") String quantityExchange,
            HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Integer indexExchange = getIndexExchangeBillDetail(Integer.parseInt(idProductDetail));
        if(indexExchange == -1) {
            thongBao.put("message","Không tìm thấy sản phẩm!");
            thongBao.put("check","3");
        }else {
            ExchangeBillDetailResponse exchangeBillDetailResponse = exchangeBillDetailResponses.get(indexExchange);
            this.exchangeBillDetailResponses.remove(exchangeBillDetailResponse);

            getReduceProductDetail(Integer.parseInt(idProductDetail),-(Integer.parseInt(quantityExchange)));
            thongBao.put("message","Xóa sản phẩm đổi thành công!");
            thongBao.put("check","1");

            session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);
        }

        totalExchange = BigDecimal.valueOf(0);
        for (ExchangeBillDetailResponse response: exchangeBillDetailResponses) {
            totalExchange = totalExchange.add(response.getTotalExchange());
        }

        return ResponseEntity.ok(thongBao);
    }

    //nut tang giam
    @PostMapping("/increase-or-decrease-product-exchange")
    public ResponseEntity<Map<String,String>> getIncreaseOrDecreaseProductExchange(@RequestBody BillDetailAjax billDetailAjax,HttpSession session) {

        Map<String,String> thongBao = new HashMap<>();

        Integer indexExchange = getIndexExchangeBillDetail(billDetailAjax.getId());

        if(indexExchange == -1) {
            thongBao.put("message","Sửa số lượng đổi không thành công!");
            thongBao.put("check","3");
        }else {
            Integer quantityUpdate = (billDetailAjax.getMethod().equals("cong") ? 1 : -1);
            ExchangeBillDetailResponse exchangeBillDetailResponse;
            exchangeBillDetailResponse = exchangeBillDetailResponses.get(indexExchange);
            exchangeBillDetailResponse.setQuantityExchange(exchangeBillDetailResponse.getQuantityExchange()+quantityUpdate);
            exchangeBillDetailResponse.setTotalExchange(exchangeBillDetailResponse.getPriceAtTheTimeOfExchange().multiply(BigDecimal.valueOf(exchangeBillDetailResponse.getQuantityExchange())));
            this.exchangeBillDetailResponses.set(indexExchange,exchangeBillDetailResponse);
            getReduceProductDetail(billDetailAjax.getId(),quantityUpdate);

            session.setAttribute("exchangeBillDetailResponses", exchangeBillDetailResponses);
            thongBao.put("message","Sửa sản phẩm vào phiếu đơn đổi thành công!");
            thongBao.put("check","1");
        }

        System.out.println(billDetailAjax.toString());

        totalExchange = BigDecimal.valueOf(0);
        for (ExchangeBillDetailResponse response: exchangeBillDetailResponses) {
            totalExchange = totalExchange.add(response.getTotalExchange());
        }

        return ResponseEntity.ok(thongBao);
    }

    private void getReduceProductDetail(Integer id, Integer quantityExchange) {
        int i = 0;
        for (Object[] objects : this.productDetails) {
            if (id.equals(objects[0])) {
                // Kiểm tra xem objects[11] có phải là Integer hay String
                if (objects[11] instanceof Integer) {
                    objects[11] = (Integer) objects[11] - quantityExchange;
                } else if (objects[11] instanceof String) {
                    objects[11] = Integer.parseInt((String) objects[11]) - quantityExchange;
                }
                this.productDetails.set(i, objects);
                // In ra giá trị các thuộc tính để kiểm tra
                Object[] objects1 = this.productDetails.get(i);
                System.out.println("Object sản phẩm là " + objects1[0] + " " + objects1[1] + " " + objects1[2] + " " + objects1[11]);
            }
            i++;
        }
    }

    private Integer getIndexExchangeBillDetail(Integer id) {
        for (int i = 0; i<this.exchangeBillDetailResponses.size();i++) {
            ExchangeBillDetailResponse objects = this.exchangeBillDetailResponses.get(i);
            if(id == objects.getProductDetail().getId()) {
                return i;
            }
        }
        return -1;
    }


    protected Page<ExchangeBillDetailResponse> getConvertListToPageExchangeBill(List<ExchangeBillDetailResponse> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<ExchangeBillDetailResponse> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}
