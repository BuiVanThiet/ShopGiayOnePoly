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

    List<ProductIClientResponse> filterProducts(
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("manufacturerIds") List<Integer> manufacturerIds,
            @Param("materialIds") List<Integer> materialIds,
            @Param("originIds") List<Integer> originIds,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("priceSort") String priceSort);

    List<ProductIClientResponse> searchProducts(@Param("keyword") String keyword);

    public List<ProductDetailClientRespone> findProductDetailByProductId(@Param("productId") Integer productId);

    public List<ProductIClientResponse> GetTop12ProductWithPriceHighest();

    public List<ProductIClientResponse> GetTop12ProductWithPriceLowest();

    public List<ColorClientResponse> findDistinctColorsByProductDetailId(@Param("productId") Integer productDetailId);

    public List<SizeClientResponse> findDistinctSizesByProductDetailId(@Param("productId") Integer productDetailId);

    List<ProductDetailClientRespone> findByProductDetailColorAndSizeAndProductId(
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
