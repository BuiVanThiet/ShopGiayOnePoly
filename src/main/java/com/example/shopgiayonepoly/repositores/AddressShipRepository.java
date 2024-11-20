package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.AddressShip;
import com.example.shopgiayonepoly.entites.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressShipRepository extends JpaRepository<AddressShip, Integer> {

}
