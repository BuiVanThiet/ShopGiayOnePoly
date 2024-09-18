package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query("select client from Size client where client.status <> 0")
    List<Size> getClientNotStatus0();
}
