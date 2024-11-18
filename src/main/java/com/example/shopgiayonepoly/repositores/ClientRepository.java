package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.AddressShip;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
                   GROUP BY p.id, p.nameProduct
            """)
    public List<ProductIClientResponse> getAllProduct();

    @Query("""
                select new com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse(
                p.id,
                p.nameProduct,
                MIN(i.nameImage),
                MIN(pd.price))
                FROM Product p
                left join p.images i
                left join ProductDetail pd on p.id = pd.product.id
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
            """)
    public List<ProductDetailClientRespone> findProductDetailByProductId(@Param("productId") Integer productId);


    @Query(value = """
            select DISTINCT new com.example.shopgiayonepoly.dto.response.client.ColorClientResponse(
            c.id,
            c.nameColor
            )
            FROM ProductDetail pd
            JOIN pd.color c
            WHERE pd.product.id = :productId
            AND c.nameColor IS NOT NULL
            """)
    public List<ColorClientResponse> findDistinctColorsByProductId(@Param("productId") Integer productId);

    @Query(value = """
            select DISTINCT new com.example.shopgiayonepoly.dto.response.client.SizeClientResponse(
            s.id,
            s.nameSize
            )
            FROM ProductDetail pd
            JOIN pd.size s
            WHERE pd.product.id = :productId
            AND s.nameSize IS NOT NULL
            """)
    public List<SizeClientResponse> findDistinctSizesByProductId(@Param("productId") Integer productId);

    @Query("""
                SELECT new com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone(
                    pd.id,
                    pd.product.id,
                    pd.product.nameProduct,
                    pd.price,
                    CASE
                        WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 1
                            THEN pd.price - (pd.price * (CAST(pd.saleProduct.discountValue AS double) / 100))
                        WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 2
                            THEN pd.price - CAST(pd.saleProduct.discountValue AS double)
                        ELSE pd.price
                    END as priceDiscount, 
                    pd.quantity,
                    pd.describe,
                    c.nameColor,
                    s.nameSize,
                    MIN(i.nameImage)
                ) 
                FROM ProductDetail pd
                LEFT JOIN pd.size s
                LEFT JOIN pd.color c 
                LEFT JOIN pd.saleProduct sp
                LEFT JOIN pd.product.images i 
                WHERE pd.color.id = :colorId AND pd.size.id = :sizeId
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
                         sp.discountValue
                         
            """)
    ProductDetailClientRespone findByProductDetailColorAndSizeAndProductId(
            @Param("colorId") Integer colorId,
            @Param("sizeId") Integer sizeId,
            @Param("productId") Integer productId);

    @Query("SELECT CASE " +
           "WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 1 " +
           "THEN pd.price - (pd.price * (pd.saleProduct.discountValue / 100)) " +
           "WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 2 " +
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

    @Query("select addressShip from AddressShip addressShip where addressShip.customer.id=:idBill order by addressShip.createDate desc ")
    List<AddressShip> getListAddressShipByIDCustomer(@Param("idBill") Integer idBill);


}
