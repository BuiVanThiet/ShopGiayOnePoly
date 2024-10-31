package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.Token;
import com.example.shopgiayonepoly.repositores.TokenRepository;
import com.example.shopgiayonepoly.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenImplement implements TokenService {
    @Autowired
    TokenRepository tokenRepository;

    @Override
    public List<Token> findAll() {
        return tokenRepository.findAll();
    }

    @Override
    public void save(Token entity) {
        tokenRepository.insertToken(entity.getIdAccount(),entity.getNameTable(),entity.getEmailSend(),entity.getCreateDate(),entity.getUpdateDate());
    }

    @Override
    public Optional<Token> findById(Integer integer) {
        return tokenRepository.findById(integer);
    }

    @Override
    public long count() {
        return tokenRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        tokenRepository.deleteById(integer);
    }

    @Override
    public void delete(Token entity) {
        tokenRepository.delete(entity);
    }

    @Override
    public Page<Token> findAll(Pageable pageable) {
        return tokenRepository.findAll(pageable);
    }

    @Override
    public Token saveGetId(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public <S extends Token> S saveOrUpdate(S entity) {
        return tokenRepository.save(entity);
    }
}
