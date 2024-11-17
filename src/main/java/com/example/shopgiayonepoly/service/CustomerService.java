package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerResponse> getAllCustomer();

    public Page<CustomerResponse> getAllCustomerByPage(Pageable pageable);

    public List<CustomerResponse> searchCustomerByKeyword(String key);

    public Page<CustomerResponse> searchCustomerByKeywordPage(String key, Pageable pageable);

    <S extends Customer> S save(S entity);

    Optional<Customer> findById(Integer integer);

    long count();

    public Customer getOne(Integer integer);

    void deleteById(Integer integer);

    List<Customer> findAll(Sort sort);

    Page<Customer> findAll(Pageable pageable);

    public void deleteCustomer(Integer id);

    String uploadFile(MultipartFile multipartFile, Integer idCustomer) throws IOException;

    public Customer getCustomerByID(Integer id);

    public Customer existsByEmail(String email);

}
