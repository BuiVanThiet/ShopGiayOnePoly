package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.ProductDetailResponse;
import com.example.shopgiayonepoly.dto.response.ProductResponse;
import com.example.shopgiayonepoly.entites.CategoryProduct;
import com.example.shopgiayonepoly.entites.Image;
import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select product from Product product where product.status <> 0 order by product.id desc ")
    List<Product> getProductNotStatus0();

    @Query("select product from Product product where product.status = 0")
    List<Product> getProductDelete();

    @Query("select product from Product product where product.codeProduct = :codeProduct")
    Optional<Product> getOneProductByCodeProduct(@Param("codeProduct") String codeProduct);

    @Query("select product from Product product where product.id = :id")
    Optional<Product> getOneProductByID(@Param("id") Integer id);

    @Query("SELECT image FROM Image image WHERE image.product.id = :productId")
    List<Image> findAllImagesByProductId(@Param("productId") Integer productId);

    @Query("SELECT categoryProduct FROM CategoryProduct categoryProduct WHERE categoryProduct.product.id = :productId")
    List<CategoryProduct> findAllCategoryByProductId(@Param("productId") Integer productId);

    @Query("SELECT new com.example.shopgiayonepoly.dto.response.ProductResponse(p.id,p.codeProduct, p.nameProduct,p.material," +
            "p.manufacturer,p.origin,p.sole, p.describe, p.createDate,p.updateDate,p.status, MIN(i.nameImage)) " +
            "FROM Product p " +
            "LEFT JOIN p.images i LEFT JOIN p.material LEFT JOIN p.origin LEFT JOIN p.manufacturer LEFT JOIN p.sole  WHERE p.status <> 0" +
            "GROUP BY p.id,p.codeProduct, p.nameProduct,p.material,"+
                     "p.manufacturer,p.origin,p.sole, p.describe, p.createDate,p.updateDate,p.status")
    List<ProductResponse> findAllProductsWithOneImage();

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN CategoryProduct cp ON p.id = cp.idProduct " +
            "WHERE ( :idCategory = 0 AND :searchTerm IS NOT NULL AND p.status <> 0 AND ( " +
            "LOWER(p.codeProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.nameProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.material.nameMaterial) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.manufacturer.nameManufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.origin.nameOrigin) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.sole.nameSole) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.describe) LIKE LOWER(CONCAT('%', :searchTerm, '%')))) OR " +
            "( :idCategory = 0 AND :searchTerm IS NULL AND p.status <> 0) OR " +
            "( :idCategory <> 0 AND :searchTerm IS NULL AND cp.idCategory = :idCategory AND p.status <> 0) OR " +
            "( :idCategory <> 0 AND :searchTerm IS NOT NULL AND cp.idCategory = :idCategory AND p.status <> 0 AND ( " +
            "LOWER(p.codeProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.nameProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.material.nameMaterial) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.manufacturer.nameManufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.origin.nameOrigin) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.sole.nameSole) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.describe) LIKE LOWER(CONCAT('%', :searchTerm, '%')))) " +
            "order by p.id desc ")
    List<Product> findProducts(@Param("idCategory") Integer idCategory, @Param("searchTerm") String searchTerm);



    @Query("SELECT p FROM Product p " +
            "LEFT JOIN CategoryProduct cp ON p.id = cp.idProduct " +
            "WHERE ( :idCategory = 0 AND :searchTerm IS NOT NULL AND p.status = 0 AND ( " +
            "LOWER(p.codeProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.nameProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.material.nameMaterial) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.manufacturer.nameManufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.origin.nameOrigin) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.sole.nameSole) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.describe) LIKE LOWER(CONCAT('%', :searchTerm, '%')))) OR " +
            "( :idCategory = 0 AND :searchTerm IS NULL AND p.status = 0) OR " +
            "( :idCategory <> 0 AND :searchTerm IS NULL AND cp.idCategory = :idCategory AND p.status = 0) OR " +
            "( :idCategory <> 0 AND :searchTerm IS NOT NULL AND cp.idCategory = :idCategory AND p.status = 0 AND ( " +
            "LOWER(p.codeProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.nameProduct) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.material.nameMaterial) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.manufacturer.nameManufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.origin.nameOrigin) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.sole.nameSole) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.describe) LIKE LOWER(CONCAT('%', :searchTerm, '%')))) " +
            "order by p.id desc ")
    List<Product> findProductDelete(@Param("idCategory") Integer idCategory, @Param("searchTerm") String searchTerm);


    @Query("SELECT DISTINCT p.nameProduct FROM Product p")
    List<String> findAllNameProduct();

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :idProduct " +
            "GROUP BY pd.id, pd.product.id, pd.size.id, pd.color.id, pd.status, pd.describe, pd.createDate, pd.updateDate, " +
            "pd.price, pd.import_price, pd.weight, pd.saleProduct.id, pd.quantity")
    List<ProductDetail> findAllProductDetailByIDProduct(@Param("idProduct") Integer idProduct);

//    @Query("SELECT new com.example.shopgiayonepoly.dto.response.ProductDetailResponse( pd.product, pd.color.nameColor, pd.size.nameSize, pd.status, pd.describe, " +
//            "pd.price, pd.import_price, pd.weight, pd.saleProduct.id, SUM(pd.quantity) ) " +
//            "FROM ProductDetail pd " +
//            "WHERE pd.product.id = :idProduct " +
//            "GROUP BY pd.product, pd.color.nameColor, pd.size.nameSize, pd.status, pd.describe, " +
//            "pd.price, pd.import_price, pd.weight, pd.saleProduct.id")
//    List<ProductDetailResponse> findAllProductDetailByIDProduct(@Param("idProduct") Integer idProduct);

    @Query("SELECT pd FROM ProductDetail pd " +
            "WHERE (LOWER(pd.color.nameColor) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(pd.size.nameSize) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR CAST(pd.price AS string) LIKE CONCAT('%', :searchTerm, '%') " +
            "OR CAST(pd.import_price AS string) LIKE CONCAT('%', :searchTerm, '%') " +
            "OR CAST(pd.quantity AS string) LIKE CONCAT('%', :searchTerm, '%') " +
            "OR LOWER(pd.describe) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR CAST(pd.weight AS string) LIKE CONCAT('%', :searchTerm, '%')) " +
            "AND pd.product.id = :idProduct")
    List<ProductDetail> searchProductDetailsByKeyword(@Param("searchTerm") String searchTerm,@Param("idProduct") Integer idProduct);


    @Query("SELECT p.codeProduct FROM Product p")
    List<String> findAllCodeProduct();

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.id = :id")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Query("SELECT SUM(pd.quantity) FROM ProductDetail pd WHERE pd.product.id = :id")
    Integer findQuantityByIDProduct(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.product.id = :idProduct")
    void deleteImageByIdProduct(@Param("idProduct") Integer idProduct);


}
