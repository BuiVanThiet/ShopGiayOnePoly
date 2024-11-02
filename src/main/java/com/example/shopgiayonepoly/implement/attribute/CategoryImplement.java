package com.example.shopgiayonepoly.implement.attribute;

import com.example.shopgiayonepoly.entites.Category;
import com.example.shopgiayonepoly.repositores.attribute.CategoryRepository;
import com.example.shopgiayonepoly.service.attribute.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryImplement implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public <S extends Category> S save(S entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public Optional<Category> findById(Integer integer) {
        return categoryRepository.findById(integer);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public List<Category> findAll(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> getCategoryNotStatus0() {
        return this.categoryRepository.getCategoryNotStatus0();
    }

    @Override
    public List<Category> getCategoryDelete() {
        return this.categoryRepository.getCategoryDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setStatus(status);
            categoryRepository.save(category);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setStatus(0);
            categoryRepository.save(category);
        }
    }

    @Override
    public void updateCategory(int id, String codeCategory, String nameCategory) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCodeCategory(codeCategory);
            category.setNameCategory(nameCategory);
            categoryRepository.save(category);
        } else {
            throw new RuntimeException("Màu sắc có " + id + " Không tồn tại.");
        }
    }

    @Override
    public Set<Category> findCategoriesByIds(List<Integer> ids) {
        return new HashSet<>(categoryRepository.findAllById(ids));
    }

}
