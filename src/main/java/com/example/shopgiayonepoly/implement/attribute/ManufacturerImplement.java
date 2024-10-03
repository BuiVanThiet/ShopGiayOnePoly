package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import com.example.shopgiayonepoly.repositores.attribute.ManufacturerRepository;
import com.example.shopgiayonepoly.service.attribute.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerImplement implements ManufacturerService {
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
    public List<Manufacturer> findAll(Sort sort) {
        return manufacturerRepository.findAll(sort);
    }

    @Override
    public Page<Manufacturer> findAll(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    @Override
    public List<Manufacturer> getManufacturerNotStatus0() {
        return this.manufacturerRepository.getManufacturerNotStatus0();
    }

    @Override
    public List<Manufacturer> getManufacturerDelete() {
        return this.manufacturerRepository.getManufacturerDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(id);
        if (optionalManufacturer.isPresent()) {
            Manufacturer manufacturer = optionalManufacturer.get();
            manufacturer.setStatus(status);
            manufacturerRepository.save(manufacturer);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(id);
        if (optionalManufacturer.isPresent()) {
            Manufacturer manufacturer = optionalManufacturer.get();
            manufacturer.setStatus(0);
            manufacturerRepository.save(manufacturer);
        }
    }

    @Override
    public void updateManufacturer(int id, String codeManufacturer, String nameManufacturer) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(id);

        if (optionalManufacturer.isPresent()) {
            Manufacturer manufacturer = optionalManufacturer.get();
            manufacturer.setCodeManufacturer(codeManufacturer);
            manufacturer.setNameManufacturer(nameManufacturer);
            manufacturerRepository.save(manufacturer);
        } else {
            throw new RuntimeException("Màu sắc có " + id + " Không tồn tại.");
        }
    }


}
