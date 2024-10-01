package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.repositores.attribute.OriginRepository;
import com.example.shopgiayonepoly.service.attribute.OriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OriginImplement implements OriginService {
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
    public List<Origin> findAll(Sort sort) {
        return originRepository.findAll(sort);
    }

    @Override
    public Page<Origin> findAll(Pageable pageable) {
        return originRepository.findAll(pageable);
    }

    @Override
    public List<Origin> getClientNotStatus0() {
        return this.originRepository.getOriginNotStatus0();
    }

    @Override
    public List<Origin> getOriginDelete() {
        return this.originRepository.getOriginDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<Origin> optionalOrigin = originRepository.findById(id);
        if (optionalOrigin.isPresent()) {
            Origin origin = optionalOrigin.get();
            origin.setStatus(status);
            originRepository.save(origin);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<Origin> optionalOrigin = originRepository.findById(id);
        if (optionalOrigin.isPresent()) {
            Origin origin = optionalOrigin.get();
            origin.setStatus(0);
            originRepository.save(origin);
        }
    }

    @Override
    public void updateOrigin(int id, String codeOrigin, String nameOrigin) {
        Optional<Origin> optionalOrigin = originRepository.findById(id);

        if (optionalOrigin.isPresent()) {
            Origin origin = optionalOrigin.get();
            origin.setCodeOrigin(codeOrigin);
            origin.setNameOrigin(nameOrigin);
            originRepository.save(origin);
        } else {
            throw new RuntimeException("Màu sắc có " + id + " Không tồn tại.");
        }
    }


}
