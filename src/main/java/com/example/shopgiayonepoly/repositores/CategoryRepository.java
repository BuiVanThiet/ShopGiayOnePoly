package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select category from Category category where category.status <> 0")
    List<Category> getCategoryNotStatus0();

    @Query("select category from Category category where category.status = 0")
    List<Category> getCategoryDelete();


}
