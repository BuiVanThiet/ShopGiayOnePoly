package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.ProductWithDiscountResponse;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Integer> {
    @Query("select s from SaleProduct s where s.status =1")
    public Page<SaleProduct> getAllSaleProductByPage(Pageable pageable);

    @Query("select s from SaleProduct s where s.status =1")
    public List<SaleProduct> getAllSaleProduct();

    @Query("select s from SaleProduct s where s.status =0")
    public Page<SaleProduct> getSaleProductDeleteByPage(Pageable pageable);

    @Query("select s from SaleProduct s where s.status =0")
    public List<SaleProduct> getAllSaleProductDelete();

    @Modifying
    @Transactional
    @Query(value = "update SaleProduct set status =0, updateDate = CURRENT_TIMESTAMP  where id=:id")
    public void deleteBySetStatus(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update SaleProduct set status=1, updateDate = CURRENT_TIMESTAMP  where  id =:id")
    public void restoreStatusSaleProduct(@Param("id") Integer id);

    @Query("select s from SaleProduct s where (s.nameSale like %:key% or s.codeSale like %:key%) and s.status = 1")
    public Page<SaleProduct> searchSaleProductByKeyword(@Param("key") String key,
                                                        Pageable pageable);

    @Query("select s from SaleProduct s where s.discountType =:types and s.status = 1")
    Page<SaleProduct> searchSaleProductByTypeSaleProduct(@Param("types") int type,
                                                         Pageable pageable);

    @Query("select p from ProductDetail p where p.status = 1 and p.saleProduct is null")
    public List<ProductDetail> getAllProductDetailByPage();

    @Query("select p FROM ProductDetail p WHERE p.status = 1 AND p.saleProduct IS NOT NULL")
    public List<ProductDetail> getAllProductDetailWithDiscount();

    @Modifying
    @Transactional
    @Query("UPDATE ProductDetail p " +
           "SET p.price = CASE " +
           "WHEN :discountType = 1 THEN p.price - (p.price * :discountValue / 100) " +
           "WHEN :discountType = 2 THEN p.price - :discountValue " +
           "ELSE p.price END " +
           "WHERE p.id IN :productIds")
    void applyDiscountToMultipleProducts(@Param("productIds") List<Integer> productIds,
                                         @Param("discountValue") BigDecimal discountValue,
                                         @Param("discountType") Integer discountType);

    @Modifying
    @Query("UPDATE ProductDetail p SET p.price = ?2 WHERE p.id = ?1")
    void updatePriceById(Integer productId, BigDecimal price);

    @Query("select pd from ProductDetail pd" +
           " join pd.saleProduct sp" +
           " where sp.id = :idSaleProduct")
    public List<ProductDetail> findProducDetailByIDDiscout(@Param("idSaleProduct") Integer id);


    @Modifying
    @Transactional
    @Query("update SaleProduct v set v.status = 2 where v.endDate < CURRENT_TIMESTAMP and v.status <> 2")
    public void updateSaleProductStatusForExpired();
    @Query("select p from ProductDetail p")
    public List<ProductDetail> getAllProductDetail();

    @Query(value = """
    select
        id,
        code_sale,
        name_sale,
        discount_value,
        discount_type,
        start_date,
        end_date,
        CASE
            WHEN status = 1 and CONVERT(DATE, GETDATE()) > CONVERT(DATE, end_date) THEN 3
            ELSE status
        END AS status_desc
    from sale_product 
    where 
     -- 1. Lọc theo discountType, nếu null thì hiện tất cả
        (:typeCheck IS NULL OR discount_type = :typeCheck) 
        -- 2. Lọc gần đúng khi nhập ký tự của trường codeVoucher và nameVoucher
        AND CONCAT(name_sale, code_sale) LIKE CONCAT('%', :searchTerm, '%')
        -- 3. Lọc theo 3 trạng thái: hoạt động, ngừng hoạt động, hết hạn
        AND (
            (:status = 1 AND status = 1 and (CONVERT(DATE, GETDATE()) BETWEEN CONVERT(DATE, start_date) AND CONVERT(DATE, end_date))) -- hoạt động
            OR (:status = 2 AND (status = 0 OR status = 2)) -- ngừng hoạt động
            OR (:status = 3 AND CONVERT(DATE, GETDATE()) > CONVERT(DATE, end_date) and status = 1) -- hết hạn
        ) 
        ORDER BY update_date DESC;
""",nativeQuery = true)
    public List<Object[]> getAllSaleProductByFilter(@Param("typeCheck") Integer typeCheck,@Param("searchTerm")String searchTerm,@Param("status")Integer status);
    @Query(value = """
    WITH CategoryCTE AS (
        SELECT DISTINCT
            cp.id_product,
            cat.name_category
        FROM category_product cp
        JOIN category cat ON cp.id_category = cat.id
    ),
    ImageCTE AS (
        SELECT DISTINCT
            im.id_product,
            im.name_image
        FROM image im
    )
    SELECT 
        pd.id AS product_detail_id, --0
        p.id AS product_id, -- 1 Thêm product_id vào SELECT để sử dụng trong GROUP BY
        p.name_product, --2
        c.name_color, --3
        s.name_size, --4
        m.name_manufacturer, --5
        mat.name_material, --6
        o.name_origin, --7
        so.name_sole, --8
        pd.price, --9
        pd.quantity, --10
        pd.quantity AS updated_quantity,  -- 11 Trừ số lượng ảo
       CASE
          WHEN sp.start_date <= CAST(GETDATE() AS DATE) AND sp.end_date >= CAST(GETDATE() AS DATE) THEN
              CASE
                  WHEN sp.discount_type = 1 THEN
                      CASE
                          WHEN pd.price * (1 - sp.discount_value / 100) < 0 THEN 0
                          ELSE pd.price * (1 - sp.discount_value / 100)
                      END
                  WHEN sp.discount_type = 2 THEN
                      CASE
                          WHEN pd.price - sp.discount_value < 0 THEN 0
                          ELSE pd.price - sp.discount_value
                      END
                  ELSE pd.price
              END
          ELSE pd.price
      END AS final_price, --12
        p.status AS product_status, --13
        pd.status AS product_detail_status, --14
        CASE
            WHEN sp.start_date <= CAST(GETDATE() AS DATE) AND sp.end_date >= CAST(GETDATE() AS DATE) THEN
                CASE
                    WHEN sp.discount_type = 1 THEN N'Giảm ' + CAST(CAST(sp.discount_value AS INT) AS NVARCHAR) + N' %'
                    WHEN sp.discount_type = 2 THEN N'Giảm ' + FORMAT(CAST(sp.discount_value AS INT), '#,###') + N' VNĐ'
                    ELSE N'Không giảm'
                END
            ELSE N'Không giảm'
        END AS title_sale, --15
        (SELECT STRING_AGG(name_category, ', ') FROM CategoryCTE WHERE CategoryCTE.id_product = p.id) AS categories,  --16 Không dùng DISTINCT ở đây
        (SELECT STRING_AGG(name_image, ', ') FROM ImageCTE WHERE ImageCTE.id_product = p.id) AS images --17
        ,p.code_product -- 18
        ,CASE
           WHEN sp_virtual.discount_type = 1 THEN
               CASE
                   WHEN pd.price * (1 - sp_virtual.discount_value / 100) < 0 THEN 0
                   ELSE pd.price * (1 - sp_virtual.discount_value / 100)
               END
           WHEN sp_virtual.discount_type = 2 THEN
               CASE
                   WHEN pd.price - sp_virtual.discount_value < 0 THEN 0
                   ELSE pd.price - sp_virtual.discount_value
               END
           ELSE pd.price
       END AS price_Discount_virtual--19
    FROM product_detail pd
    CROSS JOIN
        (SELECT discount_type, discount_value FROM sale_product WHERE id = :idSaleProductCheck) sp_virtual
    LEFT JOIN product p ON pd.id_product = p.id
    LEFT JOIN color c ON pd.id_color = c.id
    LEFT JOIN size s ON pd.id_size = s.id
    LEFT JOIN sale_product sp ON pd.id_sale_product = sp.id
    LEFT JOIN manufacturer m ON p.id_manufacturer = m.id
    LEFT JOIN material mat ON p.id_material = mat.id
    LEFT JOIN origin o ON p.id_origin = o.id
    LEFT JOIN sole so ON p.id_sole = so.id
    LEFT JOIN category_product cp ON p.id = cp.id_product
    LEFT JOIN category cat ON cp.id_category = cat.id
    LEFT JOIN image im on im.id_product = p.id
    WHERE pd.status = 1
      AND p.status = 1
      -- Tìm kiếm gần đúng theo tên sản phẩm
      AND (:nameProduct IS NULL OR p.name_product LIKE CONCAT('%', :nameProduct, '%'))
      -- Lọc theo danh mục
      AND (:categoryList IS NULL OR cat.id IN (:categoryList))
      -- Lọc theo màu sắc
      AND (:colorList IS NULL OR c.id IN (:colorList))
      -- Lọc theo kích thước
      AND (:sizeList IS NULL OR s.id IN (:sizeList))
      -- Lọc theo nhà sản xuất
      AND (:manufacturerList IS NULL OR m.id IN (:manufacturerList))
      -- Lọc theo chất liệu
      AND (:materialList IS NULL OR mat.id IN (:materialList))
      -- Lọc theo nơi sản xuất
      AND (:originList IS NULL OR o.id IN (:originList))
      -- Lọc theo đế giày
      AND (:soleList IS NULL OR so.id IN (:soleList))
      AND  (
            (:statusCheckIdSale = 1 AND pd.id_sale_product IS NULL ) 
            OR (:statusCheckIdSale = 2 AND pd.id_sale_product IS NOT NULL) -- ngừng hoạt động
        ) 
      GROUP BY
             pd.id,
             p.id,                -- Thêm cột p.id vào GROUP BY
             p.name_product,
             c.name_color,
             s.name_size,
             m.name_manufacturer,
             mat.name_material,
             o.name_origin,
             so.name_sole,
             pd.price,
             pd.quantity,
             p.status,
             pd.status,
             sp.start_date,
             sp.end_date,
             sp.discount_type,
             sp.discount_value
             ,p.code_product
            ,sp_virtual.discount_type  -- Thêm vào GROUP BY
             ,sp_virtual.discount_value  -- Thêm vào GROUP BY
    """,nativeQuery = true)
    public List<Object[]> getAllProduct(
            @Param("nameProduct") String nameProduct,
            @Param("categoryList") Integer[]  categoryList,
            @Param("colorList") Integer[]  colorList,
            @Param("sizeList") Integer[]  sizeList,
            @Param("manufacturerList") Integer[]  manufacturerList,
            @Param("materialList") Integer[]  materialList,
            @Param("originList") Integer[]  originList,
            @Param("soleList") Integer[]  soleList,
            @Param("statusCheckIdSale") Integer statusCheckIdSale,
            @Param("idSaleProductCheck") Integer idSaleProductCheck
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE sale_product SET end_date = DATEADD(day, 1, CONVERT(date, GETDATE())), status = 1, update_date = GETDATE() WHERE id = :id", nativeQuery = true)
    public void updateSaleProductExpired(@Param("id") Integer id);
}
