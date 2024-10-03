package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientSecurityResponsetory extends JpaRepository<Customer,Integer> {
    @Query("""
        select new com.example.shopgiayonepoly.dto.response.ClientLoginResponse(cs.id,
        cs.createDate,
        cs.updateDate,
        cs.status,
        cs.fullName,
        cs.numberPhone,
        cs.birthDay,
        cs.image,
        cs.email,
        cs.acount,
        cs.password,
        cs.gender,
        cs.addRess,
        'KhachHang'
        ) from Customer cs where cs.email = :email or cs.acount = :account and cs.status = 1
""")
    ClientLoginResponse getCustomerByEmailAndAcount(@Param("email") String email, @Param("account") String account);
}
