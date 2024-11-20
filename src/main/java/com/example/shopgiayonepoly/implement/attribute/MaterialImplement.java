package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.repositores.attribute.MaterialRepository;
import com.example.shopgiayonepoly.service.attribute.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialImplement implements MaterialService {
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
    public List<Material> findAll(Sort sort) {
        return materialRepository.findAll(sort);
    }

    @Override
    public Page<Material> findAll(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    @Override
    public List<Material> getMaterialNotStatus0() {
        return this.materialRepository.getMaterialNotStatus0();
    }

    @Override
    public List<Material> getMaterialDelete() {
        return this.materialRepository.getMaterialDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<Material> optionalMaterial = materialRepository.findById(id);
        if (optionalMaterial.isPresent()) {
            Material material = optionalMaterial.get();
            material.setStatus(status);
            materialRepository.save(material);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<Material> optionalMaterial = materialRepository.findById(id);
        if (optionalMaterial.isPresent()) {
            Material material = optionalMaterial.get();
            material.setStatus(0);
            materialRepository.save(material);
        }
    }

    @Override
    public void updateMaterial(int id, String codeMaterial, String nameMaterial) {
        Optional<Material> optionalMaterial = materialRepository.findById(id);

        if (optionalMaterial.isPresent()) {
            Material material = optionalMaterial.get();
            material.setCodeMaterial(codeMaterial);
            material.setNameMaterial(nameMaterial);
            materialRepository.save(material);
        } else {
            throw new RuntimeException("Chất liệu có " + id + " Không tồn tại.");
        }
    }


}
