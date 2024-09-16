package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.repositores.attribute.MaterialRepository;
import com.example.shopgiayonepoly.repositores.attribute.OriginRepository;
import com.example.shopgiayonepoly.service.attribute.MaterialSevice;
import com.example.shopgiayonepoly.service.attribute.OriginSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OriginImplement implements OriginSevice {
    @Autowired
    OriginRepository originRepository;

    @Override
    public List<Origin> findAll() {
        return originRepository.findAll();
    }

    @Override
    public <S extends Origin> S save(S entity) {
        return originRepository.save(entity);
    }

    @Override
    public Optional<Origin> findById(Integer integer) {
        return originRepository.findById(integer);
    }

    @Override
    public long count() {
        return originRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        originRepository.deleteById(integer);
    }

    @Override
    public List<Origin> findAll(Sort sort) {
        return originRepository.findAll(sort);
    }

    @Override
    public Page<Origin> findAll(Pageable pageable) {
        return originRepository.findAll(pageable);
    }


}
