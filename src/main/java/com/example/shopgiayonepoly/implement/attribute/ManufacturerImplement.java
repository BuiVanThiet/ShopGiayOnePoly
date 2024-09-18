package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import com.example.shopgiayonepoly.repositores.attribute.ManufacturerRepository;
import com.example.shopgiayonepoly.service.attribute.ManufacturerSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerImplement implements ManufacturerSevice {
    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Override
    public List<Manufacturer> findAll() {
        return manufacturerRepository.findAll();
    }

    @Override
    public <S extends Manufacturer> S save(S entity) {
        return manufacturerRepository.save(entity);
    }

    @Override
    public Optional<Manufacturer> findById(Integer integer) {
        return manufacturerRepository.findById(integer);
    }

    @Override
    public long count() {
        return manufacturerRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        manufacturerRepository.deleteById(integer);
    }

    @Override
    public List<Manufacturer> findAll(Sort sort) {
        return manufacturerRepository.findAll(sort);
    }

    @Override
    public Page<Manufacturer> findAll(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    @Override
    public List<Manufacturer> getClientNotStatus0() {
        return this.manufacturerRepository.getClientNotStatus0();
    }


}
