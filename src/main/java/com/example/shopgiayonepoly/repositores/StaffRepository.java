package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
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
            s.address,
            s.gender,   
            s.birthDay,
            s.numberPhone,
            s.email,
            s.role
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
            s.address,
            s.gender,
            s.birthDay,
            s.numberPhone,
            s.email,
            s.role
    )  
    from Staff s where concat(s.fullName, s.codeStaff, s.numberPhone) like %:key%
    """)
    public List<StaffResponse> searchStaffByKeyword(@Param("key") String key);

    @Query("select s from Staff s where (s.fullName like %:key% or s.codeStaff like %:key% or s.numberPhone like %:key%) and s.status = 1")
    public Page<Staff> searchStaffByKeywordPage(@Param("key") String key, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update Staff set status =0 where id=:id")
    public void deleteBySetStatus(@Param("id") Integer id);

    @Query("select v from Staff v where v.status =1 or v.status = 2")
    public Page<Staff> getAllStaffByPage(Pageable pageable);
}
