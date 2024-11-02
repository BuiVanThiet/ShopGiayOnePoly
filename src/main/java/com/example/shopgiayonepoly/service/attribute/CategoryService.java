package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryService {
    List<Category> findAll();

    <S extends Category> S save(S entity);

    Optional<Category> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Category> findAll(Sort sort);

    Page<Category> findAll(Pageable pageable);

    List<Category> getCategoryNotStatus0();

    List<Category> getCategoryDelete();

    void updateStatus(int id, int status);

    void updateCategory(int id, String codeCategory, String nameCategory);

    Set<Category> findCategoriesByIds(List<Integer> ids);

}
