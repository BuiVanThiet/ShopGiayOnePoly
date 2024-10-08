package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffRepository;
import com.example.shopgiayonepoly.service.StaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffImplement implements StaffService {
    @Autowired
    StaffRepository staffRepository;

    @Override
    public List<StaffResponse> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    @Override
    public Page<Staff> getAllStaffByPage(Pageable pageable) {
        return staffRepository.getAllStaffByPage(pageable);
    }

    @Override
    public List<StaffResponse> searchStaffByKeyword(String key) {
        return staffRepository.searchStaffByKeyword(key);
    }

    @Override
    public <S extends Staff> S save(S entity) {
        return staffRepository.save(entity);
    }

    @Override
    public Optional<Staff> findById(Integer integer) {
        return staffRepository.findById(integer);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void updateStaff(StaffRequest staffRequest) {
        Staff staff = new Staff();
        BeanUtils.copyProperties(staffRequest, staff);
        staffRepository.save(staff);
    }

    @Override
    public Staff getOne(Integer integer) {
        return staffRepository.findById(integer).get();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public List<Staff> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Staff> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteStaff(Integer id) {
        staffRepository.deleteBySetStatus(id);
    }
}
