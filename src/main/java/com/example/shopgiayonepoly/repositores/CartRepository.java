package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.client.CartItemResponse;
import com.example.shopgiayonepoly.dto.response.client.CartResponse;
import com.example.shopgiayonepoly.entites.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("""
                SELECT c
                FROM Cart c
                JOIN c.customer cus
                JOIN c.productDetail pd
                WHERE cus.id = :customerID
                AND pd.id = :productID
            """)
    Cart findByCustomerIDAndProductDetail(@Param("customerID") Integer customerID,
                                          @Param("productID") Integer productDetailID);


    @Query("""
                SELECT c
                FROM Cart c
                JOIN c.customer cus
                WHERE cus.id = :customerId
            """)
    List<Cart> getCartItemsForCustomer(@Param("customerId") Integer customerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.customer.id = :customerId")
    void deleteCartByCustomerID(@Param("customerId") Integer customerId);


}
