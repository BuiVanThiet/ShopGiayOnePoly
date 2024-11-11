package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.response.ProductDetailResponse;
import com.example.shopgiayonepoly.dto.response.ProductResponse;
import com.example.shopgiayonepoly.entites.CategoryProduct;
import com.example.shopgiayonepoly.entites.Image;
import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    <S extends Product> S save(S entity);

    Optional<Product> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Product> findAll(Sort sort);

    Page<Product> findAll(Pageable pageable);

    List<Product> getProductNotStatus0();

    List<Product> getProductDelete();

    void updateStatus(int id, int status);

    void updateProduct(int id, String codeProduct, String nameProduct);

    Optional<Product> getOneProductByCodeProduct(String codeProduct);

    Optional<Product> getOneProductByID(Integer id);

    List<Image> findAllImagesByProductId(@Param("productId") Integer productId);

    List<CategoryProduct> findAllCategoryByProductId(@Param("productId") Integer productId);

    List<ProductResponse> findAllProductsWithOneImage();

    List<Product> findProducts(Integer idCategory, String searchTerm);
    List<String> findAllNameProduct();

    List<ProductDetail> findAllProductDetailByIDProduct(@Param("idProduct") Integer idProduct);

    List<ProductDetail> searchProductDetailsByKeyword(@Param("searchTerm") String searchTerm,@Param("idProduct") Integer idProduct);

    List<String> findAllCodeProduct();
}
