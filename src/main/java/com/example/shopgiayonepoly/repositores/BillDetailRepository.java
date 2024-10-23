package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.bill.CategoryProductResponse;
import com.example.shopgiayonepoly.dto.response.bill.ImageProductResponse;
import com.example.shopgiayonepoly.dto.response.bill.ProductDetailSellResponse;
import com.example.shopgiayonepoly.entites.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail,Integer> {
    @Query("select bdt from BillDetail bdt where bdt.bill.id = :idBill order by bdt.createDate desc ")
    Page<BillDetail> getBillDetailByIdBill(@Param("idBill") Integer idBills, Pageable pageable);
    @Query("select pdt from ProductDetail pdt")
    List<ProductDetail> getAllProductDetail();
    @Query("select pdt from ProductDetail pdt where pdt.id = :idCheck")
    ProductDetail getProductDetailById(@Param("idCheck") Integer idCheck);

    @Query("select bdt.id from BillDetail bdt where bdt.bill.id = :idBillCheck and bdt.productDetail.id = :idPDTCheck")
    Integer getBillDetailExist(@Param("idBillCheck") Integer idBillCheck, @Param("idPDTCheck") Integer idPDTCheck);
    @Query("select bdt from BillDetail bdt where bdt.bill.id = :idBill")
    List<BillDetail> getBillDetailByIdBill(@Param("idBill") Integer idBills);

    @Query("select bdt.id from BillDetail bdt where bdt.bill.id = :idBill order by bdt.createDate desc")
    Integer getFirstBillDetailIdByIdBill(@Param("idBill") Integer idBill);

    //    @Query("select pdt from ProductDetail pdt " +
////            "join pdt.product p " +
//            "left join pdt.product.categories cate " +
//            "where (:product is null or pdt.product.nameProduct like %:product%)" +
//            "and (:color is null or pdt.color.id = :color) " +
//            "and (:size is null or pdt.size.id = :size) " +
//            "and (:material is null or pdt.product.material.id = :material) " +
//            "and (:manufacturer is null or pdt.product.manufacturer.id = :manufacturer) " +
//            "and (:origin is null or pdt.product.origin.id = :origin) " +
//            "and ((:categories is null or :categories = '') or cate.id in (:categories)) " +  // Điều kiện với categories
//            "and pdt.status <> 0 and pdt.product.status <> 0")
//    List<ProductDetail> findProductDetailSale(@Param("product") String nameProduct,
//                                              @Param("color") Integer idColor,
//                                              @Param("size") Integer idSize,
//                                              @Param("material") Integer idMaterial,
//                                              @Param("manufacturer") Integer idManufacturer,
//                                              @Param("origin") Integer idOrigin
//                                                ,@Param("categories") List<Integer> idCategory
//                                              );
//@Query("""
//    SELECT
//        new com.example.shopgiayonepoly.dto.response.bill.ProductDetailSaleResponse(
//            pd.id,
//            pd.createDate,
//            pd.updateDate,
//            pd.status,
//            pd.product,
//            pd.color,
//            pd.size,
//            CASE
//                WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 1
//                    THEN pd.price - (pd.price * pd.saleProduct.discountValue / 100)
//                WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 2
//                    THEN pd.price - pd.saleProduct.discountValue
//                ELSE pd.price
//            END,
//            pd.price,
//            pd.quantity,
//            pd.describe
//
//        )
//    FROM ProductDetail pd
//    left JOIN pd.product p
//    LEFT JOIN p.categories c
//    WHERE p.nameProduct LIKE %:nameProduct%
//    AND (:size IS NULL OR pd.size.id = :size)
//    AND (:color IS NULL OR pd.color.id = :color)
//    AND (:material IS NULL OR p.material.id = :material)
//    AND (:manufacturer IS NULL OR p.manufacturer.id = :manufacturer)
//    AND (:origin IS NULL OR p.origin.id = :origin)
//    AND (:categories IS NULL OR c.id IN :categories)
//    AND pd.status <> 0\s
//    AND p.status <> 0
//
//""")
    @Query("SELECT pd FROM ProductDetail pd " +
            "JOIN pd.product p " +
            "LEFT JOIN p.categories c " +
            "WHERE p.nameProduct LIKE %:nameProduct% "+
            "AND (:size IS NULL OR pd.size.id = :size) " +
            "AND (:color IS NULL OR pd.color.id = :color) " +
            "AND (:material IS NULL OR p.material.id = :material) " +
            "AND (:manufacturer IS NULL OR p.manufacturer.id = :manufacturer) " +
            "AND (:origin IS NULL OR p.origin.id = :origin) " +
            "AND (:categories IS NULL OR  c.id IN :categories) " +
            "AND pd.status <> 0 AND p.status <> 0 ")
    List<ProductDetail> findProductDetailSale(
            @Param("nameProduct") String nameProduct,
            @Param("size") Integer size,
            @Param("color") Integer color,
            @Param("material") Integer material,
            @Param("manufacturer") Integer manufacturer,
            @Param("origin") Integer origin,
            @Param("categories") List<Integer> categories
    );

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
                    WHEN sp.discount_type = 1 THEN pd.price * (1 - sp.discount_value / 100)
                    WHEN sp.discount_type = 2 THEN pd.price - sp.discount_value
                    ELSE pd.price
                END
            ELSE pd.price
        END AS final_price, --12
        p.status AS product_status, --13
        pd.status AS product_detail_status, --14
        CASE
            WHEN sp.start_date <= CAST(GETDATE() AS DATE) AND sp.end_date >= CAST(GETDATE() AS DATE) THEN
                CASE
                    WHEN sp.discount_type = 1 THEN N'Giảm ' + CAST(sp.discount_value AS NVARCHAR) + N' %'
                    WHEN sp.discount_type = 2 THEN N'Giảm ' + CAST(sp.discount_value AS NVARCHAR) + N' VNĐ'
                    ELSE N'Không giảm'
                END
            ELSE N'Không giảm'
        END AS title_sale, --15
        (SELECT STRING_AGG(name_category, ', ') FROM CategoryCTE WHERE CategoryCTE.id_product = p.id) AS categories,  --16 Không dùng DISTINCT ở đây
        (SELECT STRING_AGG(name_image, ', ') FROM ImageCTE WHERE ImageCTE.id_product = p.id) AS images --17
    FROM product_detail pd
    LEFT JOIN bill_detail bd ON bd.id_product_detail = pd.id AND bd.id_bill = :idBill  -- Hóa đơn 122
    JOIN product p ON pd.id_product = p.id
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
             COALESCE(bd.quantity, 0),  -- Thêm bill_detail.quantity vào GROUP BY
             p.status,
             pd.status,
             sp.start_date,
             sp.end_date,
             sp.discount_type,
             sp.discount_value;
      ;
""", nativeQuery = true)
    List<Object[]> findProductDetailSaleTest(
            @Param("nameProduct") String nameProduct,
            @Param("categoryList") Integer[]  categoryList,
            @Param("colorList") Integer[]  colorList,
            @Param("sizeList") Integer[]  sizeList,
            @Param("manufacturerList") Integer[]  manufacturerList,
            @Param("materialList") Integer[]  materialList,
            @Param("originList") Integer[]  originList,
            @Param("soleList") Integer[]  soleList,
            @Param("idBill") Integer idBill
    );

    @Query("select new com.example.shopgiayonepoly.dto.response.bill.ImageProductResponse(image.nameImage) from Image image where image.product.id = :idCheck")
    List<ImageProductResponse> getImageByBill(@Param("idCheck") Integer id);
    @Query("select new com.example.shopgiayonepoly.dto.response.bill.CategoryProductResponse(cate.nameCategory) from Category cate right join cate.products product where product.id = :idCheck")
    List<CategoryProductResponse> getCategoryByBill(@Param("idCheck") Integer id);

    @Query("select COALESCE(sum(bdt.totalAmount), 0.00) from BillDetail bdt where bdt.bill.id = :idCheck")
    BigDecimal getTotalAmountByIdBill(@Param("idCheck") Integer id);

    @Query("select cate from Category cate where cate.status <> 0")
    List<Category> getAllCategores();
}
