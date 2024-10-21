package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone;
import com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse;
import com.example.shopgiayonepoly.repositores.ClientRepository;
import com.example.shopgiayonepoly.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<ProductIClientResponse> getAllProduct() {
       return clientRepository.getAllProduct();
    }

    @Override
    public List<ProductDetailClientRespone> findProductDetailByProductId(Integer productId) {
        return  clientRepository.findProductDetailByProductId(productId);
    }

}
