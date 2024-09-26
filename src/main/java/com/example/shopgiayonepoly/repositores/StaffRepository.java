package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Query("""
        select new com.example.shopgiayonepoly.dto.response.StaffResponse(
            s.id,
            s.createDate,
            s.updateDate,
            s.status,
            "image1",
            s.codeStaff,
            s.fullName,
            s.gender,
            s.birthDay,
            s.numberPhone,
            s.email,
            s.role.nameRole
        ) 
        from Staff s
    """)
    public List<StaffResponse> getAllStaff();


    @Query("""
            select new com.example.shopgiayonepoly.dto.response.StaffResponse(
            s.id,
            s.createDate,
            s.updateDate,
            s.status,
            "image1",
            s.codeStaff,
            s.fullName,
            s.gender,
            s.birthDay,
            s.numberPhone,
            s.email,
            s.role.nameRole
    )  
    from Staff s where concat(s.fullName, s.codeStaff, s.numberPhone) like %:key%
    """)
    public List<StaffResponse> searchStaffByKeyword(@Param("key") String key);
}
