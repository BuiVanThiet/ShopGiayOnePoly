package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.bill.BillResponseManage;
import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.AddressShip;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Bill, Integer> {
    @Query("""
                   SELECT new com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse(
                       p.id, p.nameProduct,
                       MIN(i.nameImage),
                       MIN(pd.price))
                   FROM Product p
                   LEFT JOIN p.images i 
                   LEFT JOIN ProductDetail pd ON p.id = pd.product.id 
                   WHERE p.status = 1 AND pd.status = 1
                   GROUP BY p.id, p.nameProduct
            """)
    public List<ProductIClientResponse> getAllProduct();

    @Query("SELECT new com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse(" +
            "p.id, p.nameProduct, MIN(i.nameImage), MIN(pd.price)) " +
            "FROM Product p " +
            "LEFT JOIN CategoryProduct cp ON p.id = cp.idProduct " +
            "LEFT JOIN p.images i " +
            "LEFT JOIN ProductDetail pd ON p.id = pd.product.id " +
            "WHERE (:categoryIds IS NULL OR cp.idCategory IN :categoryIds) " +
            "AND (:manufacturerIds IS NULL OR p.manufacturer.id IN :manufacturerIds) " +
            "AND (:materialIds IS NULL OR p.material.id IN :materialIds) " +
            "AND (:originIds IS NULL OR p.origin.id IN :originIds) " +
            "AND (:minPrice IS NULL OR pd.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR pd.price <= :maxPrice) " +
            "and p.status <> 0 " +
            "GROUP BY p.id, p.nameProduct " +
            "ORDER BY CASE WHEN :priceSort = 'asc' THEN MIN(pd.price) END ASC, " +
            "         CASE WHEN :priceSort = 'desc' THEN MIN(pd.price) END DESC")
    List<ProductIClientResponse> filterProducts(
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("manufacturerIds") List<Integer> manufacturerIds,
            @Param("materialIds") List<Integer> materialIds,
            @Param("originIds") List<Integer> originIds,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("priceSort") String priceSort);

    @Query("SELECT new com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse(" +
            "p.id, p.nameProduct, MIN(i.nameImage), MIN(pd.price)) " +
            "FROM Product p " +
            "LEFT JOIN p.images i " +
            "LEFT JOIN ProductDetail pd ON p.id = pd.product.id " +
            "WHERE (:keyword IS NULL OR LOWER(p.nameProduct) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "and  p.status <> 0 " +
            "GROUP BY p.id, p.nameProduct " +
            "ORDER BY p.nameProduct")
    List<ProductIClientResponse> searchProducts(@Param("keyword") String keyword);

    @Query("""
                select new com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse(
                p.id,
                p.nameProduct,
                MIN(i.nameImage),
                MIN(pd.price))
                FROM Product p
                left join p.images i
                left join ProductDetail pd on p.id = pd.product.id
                WHERE p.status = 1 AND pd.status = 1
                group by p.id, p.nameProduct
                order by MIN(pd.price) desc
                      
            """)
    public List<ProductIClientResponse> GetTop12ProductWithPriceHighest();

    @Query("""
                select new com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse(
                p.id,
                p.nameProduct,
                MIN(i.nameImage),
                MIN(pd.price))
                FROM Product p
                left join p.images i
                left join ProductDetail pd on p.id = pd.product.id
                WHERE p.status = 1 AND pd.status = 1
                group by p.id, p.nameProduct
                order by MIN(pd.price) asc
            """)
    public List<ProductIClientResponse> GetTop12ProductWithPriceLowest();

    @Query(value = """
    SELECT new com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone(
        pd.id,
        p.id,
        p.nameProduct,
        pd.price,
        CASE
            WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 1
                THEN pd.price - (pd.price * (CAST(pd.saleProduct.discountValue AS double) / 100))
            WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 2
                THEN pd.price - CAST(pd.saleProduct.discountValue AS double)
            ELSE pd.price
        END,
        pd.quantity,
        p.describe,
        c.nameColor,
        s.nameSize,
        sp.id
    )
    FROM ProductDetail pd
    JOIN pd.product p
    LEFT JOIN pd.color c
    LEFT JOIN pd.size s
    LEFT JOIN pd.saleProduct sp
    WHERE p.id = :productId 
      AND pd.status = 1 
      AND pd.id IS NOT NULL
""")
    public List<ProductDetailClientRespone> findProductDetailByProductId(@Param("productId") Integer productId);



    @Query(value = """
    SELECT DISTINCT new com.example.shopgiayonepoly.dto.response.client.ColorClientResponse(
        c.id,
        c.nameColor
    )
    FROM ProductDetail pd
    JOIN pd.color c
    WHERE pd.id = :productDetailId
    AND c.nameColor IS NOT NULL
""")
    List<ColorClientResponse> findDistinctColorsByProductDetailId(@Param("productDetailId") Integer productDetailId);


    @Query(value = """
        select DISTINCT new com.example.shopgiayonepoly.dto.response.client.SizeClientResponse(
        s.id,
        s.nameSize
        )
        FROM ProductDetail pd
        JOIN pd.size s
        WHERE pd.id = :productDetailId
        AND s.nameSize IS NOT NULL
        """)
    public List<SizeClientResponse> findDistinctSizesByProductDetailId(@Param("productDetailId") Integer productDetailId);

    @Query("""
    SELECT DISTINCT new com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone(
        pd.id,
        pd.product.id,
        pd.product.nameProduct,
        pd.price,
        CASE
            WHEN pd.saleProduct IS NOT NULL AND (CURRENT_DATE BETWEEN pd.saleProduct.startDate AND pd.saleProduct.endDate)
                 AND pd.saleProduct.discountType = 1
                THEN pd.price - (pd.price * (CAST(pd.saleProduct.discountValue AS double) / 100))
            WHEN pd.saleProduct IS NOT NULL AND (CURRENT_DATE BETWEEN pd.saleProduct.startDate AND pd.saleProduct.endDate)
                 AND pd.saleProduct.discountType = 2
                THEN pd.price - CAST(pd.saleProduct.discountValue AS double)
            ELSE pd.price
        END as priceDiscount,
        pd.quantity,
        pd.describe,
        c.nameColor,
        s.nameSize,
        MIN(i.nameImage),
        pd.product.material.nameMaterial,
        pd.product.manufacturer.nameManufacturer,
        pd.product.origin.nameOrigin
    ) 
    FROM ProductDetail pd
    LEFT JOIN pd.size s
    LEFT JOIN pd.color c 
    LEFT JOIN pd.saleProduct sp
    LEFT JOIN pd.product.images i 
    WHERE pd.color.id = :colorId 
      AND pd.size.id = :sizeId
      AND pd.product.id = :productId
    GROUP BY pd.id,
             pd.product.id,
             pd.product.nameProduct,
             pd.price,
             pd.quantity,
             pd.describe,
             c.nameColor,
             s.nameSize,
             pd.saleProduct,
             sp.discountType,
             sp.discountValue,
             pd.saleProduct.startDate,
             pd.saleProduct.endDate,
             pd.product.material.nameMaterial,
             pd.product.manufacturer.nameManufacturer,
             pd.product.origin.nameOrigin
""")
    List<ProductDetailClientRespone> findByProductDetailColorAndSizeAndProductId(
            @Param("colorId") Integer colorId,
            @Param("sizeId") Integer sizeId,
            @Param("productId") Integer productId);

    @Query("SELECT CASE " +
            "WHEN pd.saleProduct IS NOT NULL AND " +
            "(CURRENT_DATE BETWEEN pd.saleProduct.startDate AND pd.saleProduct.endDate) " +
            "AND pd.saleProduct.discountType = 1 " +
            "THEN pd.price - (pd.price * (pd.saleProduct.discountValue / 100)) " +
            "WHEN pd.saleProduct IS NOT NULL AND " +
            "(CURRENT_DATE BETWEEN pd.saleProduct.startDate AND pd.saleProduct.endDate) " +
            "AND pd.saleProduct.discountType = 2 " +
            "THEN pd.price - pd.saleProduct.discountValue " +
            "ELSE pd.price END " +
            "FROM ProductDetail pd " +
            "WHERE pd.id = :productDetailId")
    BigDecimal findDiscountedPriceByProductDetailId(@Param("productDetailId") Integer productDetailId);


    @Query("""
                select new com.example.shopgiayonepoly.dto.response.client.VoucherClientResponse(
                v.id,
                v.nameVoucher,
                v.codeVoucher,
                v.discountType,
                v.priceReduced)
                from Voucher v
                where v.id=:id
            """)
    VoucherClientResponse findVoucherApplyByID(@Param("id") Integer id);

    @Query("select c from Cart c where c.customer.id =:idCustomer")
    List<Cart> findListCartByIdCustomer(@Param("idCustomer") Integer idCustomer);

    @Transactional
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.customer.id = :customerId AND c.productDetail.id = :productDetailId")
    void deleteCartByCustomerIdAndProductDetailId(@Param("customerId") Integer customerId,
                                                  @Param("productDetailId") Integer productDetailId);

    @Query("select bd from BillDetail bd where bd.bill.id=:idBill")
    List<BillDetail> getListBillDetailByID(@Param("idBill") Integer idBill);

    @Query("select addressShip from AddressShip addressShip where addressShip.customer.id=:idBill and addressShip.status=1 order by addressShip.createDate asc")
    List<AddressShip> getListAddressShipByIDCustomer(@Param("idBill") Integer idBill);

    @Query("select addressShip from AddressShip addressShip where addressShip.status=1 order by addressShip.createDate asc")
    List<AddressShip> getListAddressShipByIDCustomer();


    @Query("select productDetail.quantity from ProductDetail productDetail where productDetail.id =: idProductDetail")
    Integer getQuantityProductDetailByID(@Param("idProductDetail")Integer idProductDetail);

    @Query("""
        select 
        new com.example.shopgiayonepoly.dto.response.bill.BillResponseManage(
            bill.id, 
            bill.codeBill, 
            bill.customer, 
            bill.staff, 
            bill.addRess, 
            bill.voucher, 
            bill.shippingPrice, 
            bill.cash, 
            bill.acountMoney, 
            bill.note, 
            bill.totalAmount - bill.priceDiscount, 
            bill.paymentMethod, 
            bill.billType, 
            bill.paymentStatus, 
            bill.surplusMoney, 
            bill.createDate, 
            bill.updateDate,
            bill.status) 
        from Bill bill 
        left join ReturnBillExchangeBill rb on rb.bill.id = bill.id
        LEFT JOIN bill.customer customer
        LEFT JOIN bill.staff staff
        LEFT JOIN bill.voucher voucher
        where
            bill.status <> 0 and
            (concat(COALESCE(bill.codeBill, ''), COALESCE(bill.customer.fullName, ''), COALESCE(bill.customer.numberPhone, '')) like %:nameCheck%)
            AND (:statusCheck IS NULL OR bill.status IN (:statusCheck))
            AND (bill.updateDate between :startDate and :endDate)
            and bill.customer.id = :idCustomer
            order by bill.updateDate desc
    """)
    Page<BillResponseManage> getAllBillByStatusDiss0(
            @Param("idCustomer") Integer idCustomer,
            @Param("nameCheck") String nameCheck,
            @Param("statusCheck") List<Integer> statusCheck,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable);

    @Query("""
            select 
            new com.example.shopgiayonepoly.dto.response.bill.BillResponseManage(
                bill.id, bill.codeBill, bill.customer, bill.staff, bill.addRess, bill.voucher, 
                bill.shippingPrice, bill.cash, bill.acountMoney, bill.note, bill.totalAmount, 
                bill.paymentMethod, bill.billType, bill.paymentStatus, bill.surplusMoney, 
                bill.createDate, bill.updateDate, bill.status) 
            from Bill bill 
            LEFT JOIN bill.customer customer
            LEFT JOIN bill.staff staff
            LEFT JOIN bill.voucher voucher
            where
                bill.status <> 0 and
                (concat(COALESCE(bill.codeBill, ''), COALESCE(bill.customer.fullName, ''), COALESCE(bill.customer.numberPhone, '')) like %:nameCheck%)
                AND (:statusCheck IS NULL OR bill.status IN (:statusCheck))
                AND (bill.updateDate between :startDate and :endDate)
                and bill.customer.id = :idCustomer
                order by bill.updateDate desc
        """)
    List<BillResponseManage> getAllBillByStatusDiss0(
            @Param("idCustomer") Integer idCustomer,
            @Param("nameCheck") String nameCheck,
            @Param("statusCheck") List<Integer> statusCheck,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


}
