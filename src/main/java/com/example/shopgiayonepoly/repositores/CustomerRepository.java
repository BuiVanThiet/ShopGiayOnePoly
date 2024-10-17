package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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

    @Query("select c from Customer c where (c.fullName like %:key% or c.numberPhone like %:key%) and c.status = 1")
    public Page<Customer> searchCustomerByKeywordPage(@Param("key") String key, Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = "update Customer set status =0 where id=:id")
    public void deleteBySetStatus(@Param("id") Integer id);

    @Query("select v from Customer v where v.status =1")
    public Page<Customer> getAllCustomrByPage(Pageable pageable);
}
