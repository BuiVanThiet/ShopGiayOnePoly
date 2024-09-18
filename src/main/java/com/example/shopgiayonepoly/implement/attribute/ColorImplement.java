package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.repositores.attribute.ColorRepository;
import com.example.shopgiayonepoly.service.attribute.ColorSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorImplement implements ColorSevice {
    @Autowired
    ColorRepository colorRepository;

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public <S extends Color> S save(S entity) {
        return colorRepository.save(entity);
    }

    @Override
    public Optional<Color> findById(Integer integer) {
        return colorRepository.findById(integer);
    }

    @Override
    public long count() {
        return colorRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        colorRepository.deleteById(integer);
    }

    @Override
    public List<Color> findAll(Sort sort) {
        return colorRepository.findAll(sort);
    }

    @Override
    public Page<Color> findAll(Pageable pageable) {
        return colorRepository.findAll(pageable);
    }

    @Override
    public List<Color> getClientNotStatus0() {
        return this.colorRepository.getClientNotStatus0();
    }


}
