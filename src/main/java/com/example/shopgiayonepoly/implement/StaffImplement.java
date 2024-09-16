package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffImplement implements StaffService {

    @Override
    public List<Staff> findAll() {
        return null;
    }

    @Override
    public <S extends Staff> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Staff> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public long count() {
        return 0;
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
}
