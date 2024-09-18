package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.repositores.attribute.MaterialRepository;
import com.example.shopgiayonepoly.service.attribute.MaterialSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialImplement implements MaterialSevice {
    @Autowired
    MaterialRepository materialRepository;

    @Override
    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    @Override
    public <S extends Material> S save(S entity) {
        return materialRepository.save(entity);
    }

    @Override
    public Optional<Material> findById(Integer integer) {
        return materialRepository.findById(integer);
    }

    @Override
    public long count() {
        return materialRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        materialRepository.deleteById(integer);
    }

    @Override
    public List<Material> findAll(Sort sort) {
        return materialRepository.findAll(sort);
    }

    @Override
    public Page<Material> findAll(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    @Override
    public List<Material> getClientNotStatus0() {
        return this.materialRepository.getClientNotStatus0();
    }
}
