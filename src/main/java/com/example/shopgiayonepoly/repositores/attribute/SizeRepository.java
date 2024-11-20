package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query("select kichCo from Size kichCo where kichCo.status <> 0 order by kichCo.id desc ")
    List<Size> getSizeNotStatus0();

    @Query("select kichCo from Size kichCo where kichCo.status = 0")
    List<Size> getSizeDelete();
}
