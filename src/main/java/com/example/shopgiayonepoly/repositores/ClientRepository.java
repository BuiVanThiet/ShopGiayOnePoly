package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone;
import com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse;
import com.example.shopgiayonepoly.entites.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT new com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse(" +
           "p.id, p.nameProduct," +
           " MIN(i.nameImage)," +
           " MIN(pd.price)) " +
           "FROM Product p " +
           "LEFT JOIN p.images i " +
           "LEFT JOIN ProductDetail pd ON p.id = pd.product.id " +
           "GROUP BY p.id, p.nameProduct")
    public List<ProductIClientResponse> getAllProduct();

    @Query(value = """
    SELECT 
        pd.id AS productDetailId,
        p.id AS productId,
        p.name_product AS productName,
        p.code_product AS productCode,
        p.describe AS productDescription,
        pd.price AS price,
        pd.import_price AS importPrice,
        pd.quantity AS quantity,
        pd.describe AS productDetailDescription,
        pd.high AS height,
        pd.width AS width,
        pd.wight AS weight,  -- Đã sửa từ 'wight' thành 'weight'
        pd.lenght_product AS length,  -- Đã sửa từ 'lenght_product' thành 'length'
        c.name_color AS colorName,  
        s.name_size AS sizeName,
        i.name_image AS productImage
    FROM 
        product_detail pd
        JOIN product p ON pd.id_product = p.id
        LEFT JOIN color c ON pd.id_color = c.id
        LEFT JOIN size s ON pd.id_size = s.id
        LEFT JOIN sale_product sp ON pd.id_sale_product = sp.id
        LEFT JOIN category_product cp ON p.id = cp.id_product
        LEFT JOIN image i ON cp.id_category = i.id
    WHERE 
        p.id = :productId
    """, nativeQuery = true)
    public List<ProductDetailClientRespone> findProductDetailByProductId(@Param("productId") Integer productId);


}
