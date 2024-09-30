package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("""
        select new com.example.shopgiayonepoly.dto.response.CustomerResponse(
            c.id,
            c.createDate,
            c.updateDate,
            c.status,
            "image1",
            c.fullName,
            c.gender,
            c.birthDay,
            c.numberPhone,
            c.email,
            c.addRess
        ) 
        from Customer c 
        left join AddressShip addRess on c.id = addRess.customer.id
    """)
    public List<CustomerResponse> getAllCustomer();

    @Query("""
        select new com.example.shopgiayonepoly.dto.response.CustomerResponse(
            c.id,
            c.createDate,
            c.updateDate,
            c.status,
            "image1",
            c.fullName,
            c.gender,
            c.birthDay,
            c.numberPhone,
            c.email,
            c.addRess
        ) 
        from Customer c 
        where concat(c.fullName, c.numberPhone) like %:key%
    """)
    public List<CustomerResponse> searchCustomerByKeyword(@Param("key") String key);
}
