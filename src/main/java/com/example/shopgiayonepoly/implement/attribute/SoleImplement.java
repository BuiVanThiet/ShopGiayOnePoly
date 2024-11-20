package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Sole;
import com.example.shopgiayonepoly.repositores.attribute.SoleRepository;
import com.example.shopgiayonepoly.service.attribute.SoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SoleImplement implements SoleService {
    @Autowired
    SoleRepository soleRepository;

    @Override
    public List<Sole> findAll() {
        return soleRepository.findAll();
    }

    @Override
    public <S extends Sole> S save(S entity) {
        return soleRepository.save(entity);
    }

    @Override
    public Optional<Sole> findById(Integer integer) {
        return soleRepository.findById(integer);
    }

    @Override
    public long count() {
        return soleRepository.count();
    }

    @Override
    public List<Sole> findAll(Sort sort) {
        return soleRepository.findAll(sort);
    }

    @Override
    public Page<Sole> findAll(Pageable pageable) {
        return soleRepository.findAll(pageable);
    }

    @Override
    public List<Sole> getSoleNotStatus0() {
        return this.soleRepository.getSoleNotStatus0();
    }

    @Override
    public List<Sole> getSoleDelete() {
        return this.soleRepository.getSoleDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<Sole> optionalSole = soleRepository.findById(id);
        if (optionalSole.isPresent()) {
            Sole sole = optionalSole.get();
            sole.setStatus(status);
            soleRepository.save(sole);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<Sole> optionalSole = soleRepository.findById(id);
        if (optionalSole.isPresent()) {
            Sole sole = optionalSole.get();
            sole.setStatus(0);
            soleRepository.save(sole);
        }
    }

    @Override
    public void updateSole(int id, String codeSole, String nameSole) {
        Optional<Sole> optionalSole = soleRepository.findById(id);

        if (optionalSole.isPresent()) {
            Sole sole = optionalSole.get();
            sole.setCodeSole(codeSole);
            sole.setNameSole(nameSole);
            soleRepository.save(sole);
        } else {
            throw new RuntimeException("Đế giày có " + id + " Không tồn tại.");
        }
    }


}
