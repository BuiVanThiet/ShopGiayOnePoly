package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Size;
import com.example.shopgiayonepoly.repositores.attribute.SizeRepository;
import com.example.shopgiayonepoly.service.attribute.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeImplement implements SizeService {
    @Autowired
    SizeRepository sizeRepository;

    @Override
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public <S extends Size> S save(S entity) {
        return sizeRepository.save(entity);
    }

    @Override
    public Optional<Size> findById(Integer integer) {
        return sizeRepository.findById(integer);
    }

    @Override
    public long count() {
        return sizeRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        sizeRepository.deleteById(integer);
    }

    @Override
    public List<Size> findAll(Sort sort) {
        return sizeRepository.findAll(sort);
    }

    @Override
    public Page<Size> findAll(Pageable pageable) {
        return sizeRepository.findAll(pageable);
    }

    @Override
    public List<Size> getClientNotStatus0() {
        return this.sizeRepository.getClientNotStatus0();
    }
}
