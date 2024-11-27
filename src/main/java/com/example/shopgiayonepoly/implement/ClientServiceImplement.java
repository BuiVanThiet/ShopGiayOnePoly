package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.AddressShip;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.Cart;
import com.example.shopgiayonepoly.repositores.ClientRepository;
import com.example.shopgiayonepoly.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
        return clientRepository.findProductDetailByProductId(productId);
    }

    @Override
    public List<ProductIClientResponse> GetTop12ProductWithPriceHighest() {
        List<ProductIClientResponse> allProducts = clientRepository.GetTop12ProductWithPriceHighest();
        return allProducts.stream().limit(12).collect(Collectors.toList());
    }

    @Override
    public List<ProductIClientResponse> GetTop12ProductWithPriceLowest() {
        List<ProductIClientResponse> allProducts = clientRepository.GetTop12ProductWithPriceLowest();
        return allProducts.stream().limit(12).collect(Collectors.toList());
    }

    @Override
    public List<ColorClientResponse> findDistinctColorsByProductId(Integer productId) {
        return clientRepository.findDistinctColorsByProductId(productId);
    }

    @Override
    public List<SizeClientResponse> findDistinctSizesByProductId(Integer productId) {
        return clientRepository.findDistinctSizesByProductId(productId);
    }

    @Override
    public ProductDetailClientRespone findByProductDetailColorAndSizeAndProductId(Integer colorId, Integer sizeId, Integer productId) {
        return clientRepository.findByProductDetailColorAndSizeAndProductId(colorId, sizeId, productId);
    }

    @Override
    public BigDecimal findDiscountedPriceByProductDetailId(Integer productDetailId) {
        return clientRepository.findDiscountedPriceByProductDetailId(productDetailId);
    }

    @Override
    public VoucherClientResponse findVoucherApplyByID(Integer id) {
        return clientRepository.findVoucherApplyByID(id);
    }

    @Override
    public List<Cart> findListCartByIdCustomer(Integer idCustomer) {
        return clientRepository.findListCartByIdCustomer(idCustomer);
    }

    @Override
    public void deleteCartByCustomerIdAndProductDetailId(Integer customerId, Integer productDetailId) {
        clientRepository.deleteCartByCustomerIdAndProductDetailId(customerId, productDetailId);
    }

    @Override
    public List<BillDetail> getListBillDetailByID(Integer idBill) {
        return clientRepository.getListBillDetailByID(idBill);
    }

    @Override
    public List<AddressShip> getListAddressShipByIDCustomer(Integer idBill) {
        return clientRepository.getListAddressShipByIDCustomer(idBill);
    }

    @Override
    public List<AddressShip> getListAddressShipByIDCustomer() {
        return clientRepository.getListAddressShipByIDCustomer();
    }


}
