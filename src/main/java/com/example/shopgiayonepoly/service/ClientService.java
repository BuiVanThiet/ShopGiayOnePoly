package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.bill.SearchBillByStatusRequest;
import com.example.shopgiayonepoly.dto.response.bill.BillResponseManage;
import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.AddressShip;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ClientService {
    public List<ProductIClientResponse> getAllProduct();

    public List<ProductDetailClientRespone> findProductDetailByProductId(@Param("productId") Integer productId);

    public List<ProductIClientResponse> GetTop12ProductWithPriceHighest();

    public List<ProductIClientResponse> GetTop12ProductWithPriceLowest();

    public List<ColorClientResponse> findDistinctColorsByProductId(@Param("productId") Integer productId);

    public List<SizeClientResponse> findDistinctSizesByProductId(@Param("productId") Integer productId);

    ProductDetailClientRespone findByProductDetailColorAndSizeAndProductId(
            @Param("colorId") Integer colorId,
            @Param("sizeId") Integer sizeId,
            @Param("productId") Integer productId);

    BigDecimal findDiscountedPriceByProductDetailId(@Param("productDetailId") Integer productDetailId);

    VoucherClientResponse findVoucherApplyByID(Integer id);

    List<Cart> findListCartByIdCustomer(@Param("idCustomer") Integer idCustomer);

    void deleteCartByCustomerIdAndProductDetailId(Integer customerId, Integer productDetailId);

    List<BillDetail> getListBillDetailByID(@Param("idBill") Integer idBill);

    List<AddressShip> getListAddressShipByIDCustomer(@Param("idBill") Integer idBill);

    List<AddressShip> getListAddressShipByIDCustomer();

    Integer getQuantityProductDetailByID(@Param("idProductDetail") Integer idProductDetail);

    Page<BillResponseManage> getAllBillByStatusDiss0(Integer idCustomer, String nameCheck, SearchBillByStatusRequest searchBillByStatusRequest, Date start, Date end, Pageable pageable);

    List<BillResponseManage> getAllBillByStatusDiss0(Integer idCustomer, String nameCheck, SearchBillByStatusRequest searchBillByStatusRequest, Date start, Date end);
}
