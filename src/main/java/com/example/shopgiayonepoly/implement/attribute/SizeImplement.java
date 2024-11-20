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
    public List<Size> findAll(Sort sort) {
        return sizeRepository.findAll(sort);
    }

    @Override
    public Page<Size> findAll(Pageable pageable) {
        return sizeRepository.findAll(pageable);
    }

    @Override
    public List<Size> getSizeNotStatus0() {
        return this.sizeRepository.getSizeNotStatus0();
    }

    @Override
    public List<Size> getSizeDelete() {
        return this.sizeRepository.getSizeDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<Size> optionalSize = sizeRepository.findById(id);
        if (optionalSize.isPresent()) {
            Size size = optionalSize.get();
            size.setStatus(status);
            sizeRepository.save(size);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<Size> optionalSize = sizeRepository.findById(id);
        if (optionalSize.isPresent()) {
            Size size = optionalSize.get();
            size.setStatus(0);
            sizeRepository.save(size);
        }
    }

    @Override
    public void updateSize(int id, String codeSize, String nameSize) {
        Optional<Size> optionalSize = sizeRepository.findById(id);

        if (optionalSize.isPresent()) {
            Size size = optionalSize.get();
            size.setCodeSize(codeSize);
            size.setNameSize(nameSize);
            sizeRepository.save(size);
        } else {
            throw new RuntimeException("Kích cỡ có " + id + " Không tồn tại.");
        }
    }


}
