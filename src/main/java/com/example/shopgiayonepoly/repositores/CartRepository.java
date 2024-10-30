package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.client.CartItemResponse;
import com.example.shopgiayonepoly.dto.response.client.CartResponse;
import com.example.shopgiayonepoly.entites.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("""
            select new com.example.shopgiayonepoly.dto.response.client.CartResponse(
            c.id,
            cus.id,
            pd.id,
            c.quantity
            )
            from Cart c
            join c.customer cus
            join  c.productDetail pd
            where cus.id=:customerID
            and pd.id=:productID
            """)
    CartResponse findByCustomerIDAndProductDetail(@Param("customerID")Integer customerID,
                                                  @Param("productID")Integer productDetailID);

    @Query("""
    select new com.example.shopgiayonepoly.dto.response.client.CartItemResponse(
        pd.id,
        pd.product.nameProduct,
        pd.color.nameColor,
        pd.size.codeSize,
        (SELECT i.nameImage FROM pd.product.images i WHERE i.id = (SELECT MIN(i2.id) FROM pd.product.images i2)),
        pd.price,
        c.quantity, 
        pd.price * c.quantity
    )
    from Cart c
    join c.customer cus
    join c.productDetail pd
    where cus.id = :customerId
""")
    List<CartItemResponse> getCartItemsForCustomer(@Param("customerId") Integer customerId);



}
