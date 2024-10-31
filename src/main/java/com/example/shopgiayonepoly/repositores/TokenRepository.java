package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO token (id_account, name_table, email_send, create_date, update_date) VALUES (:idAccount, :nameTable, :emailSend, :createDate, :updateDate)", nativeQuery = true)
    void insertToken(Integer idAccount, String nameTable, String emailSend, Date createDate, Date updateDate);
}
