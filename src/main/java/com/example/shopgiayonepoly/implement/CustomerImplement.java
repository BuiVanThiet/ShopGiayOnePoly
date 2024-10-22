package com.example.shopgiayonepoly.implement;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.CustomerRepository;
import com.example.shopgiayonepoly.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerImplement implements CustomerService {
    private final Cloudinary cloudinary;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<CustomerResponse> getAllCustomer() {
        return customerRepository.getAllCustomer();
    }

    @Override
    public Page<Customer> getAllCustomerByPage(Pageable pageable) {
        return customerRepository.getAllCustomrByPage(pageable);
    }

    @Override
    public List<CustomerResponse> searchCustomerByKeyword(String key) {
        return customerRepository.searchCustomerByKeyword(key);
    }

    @Override
    public Page<Customer> searchCustomerByKeywordPage(String key, Pageable pageable) {
        return customerRepository.searchCustomerByKeywordPage(key, pageable);
    }

    @Override
    public <S extends Customer> S save(S entity) {
        return customerRepository.save(entity);
    }

    @Override
    public Optional<Customer> findById(Integer integer) {
        return customerRepository.findById(integer);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Customer getOne(Integer integer) {
        return customerRepository.findById(integer).get();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public List<Customer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.deleteBySetStatus(id);
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, Integer idCustomer) throws IOException {
        String nameImage = UUID.randomUUID().toString();
        Customer customer = this.customerRepository.findById(idCustomer).orElse(null);
        customer.setImage(nameImage);
        this.customerRepository.save(customer);
        // Đẩy file lên Cloudinary với tên file gốc
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", nameImage)) // Đặt tên file khi upload
                .get("url")
                .toString();
    }

    @Override
    public Customer getCustomerByID(Integer id) {
        return customerRepository.findById(id).orElse(new Customer());
    }
}
