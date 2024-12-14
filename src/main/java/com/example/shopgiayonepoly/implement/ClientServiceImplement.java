package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.bill.SearchBillByStatusRequest;
import com.example.shopgiayonepoly.dto.response.bill.BillResponseManage;
import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.AddressShip;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.Cart;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.repositores.ClientRepository;
import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import com.example.shopgiayonepoly.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductIClientResponse> getAllProduct() {
        return clientRepository.getAllProduct();
    }

    @Override
    public List<ProductIClientResponse> filterProducts(List<Integer> categoryIds,
                                                       List<Integer> manufacturerIds,
                                                       List<Integer> materialIds,
                                                       List<Integer> originIds,
                                                       Integer minPrice,
                                                       Integer maxPrice,
                                                       String priceSort) {
        if (categoryIds != null && categoryIds.isEmpty()) {
            categoryIds = null;
        }
        if (manufacturerIds != null && manufacturerIds.isEmpty()) {
            manufacturerIds = null;
        }
        if (materialIds != null && materialIds.isEmpty()) {
            materialIds = null;
        }
        if (originIds != null && originIds.isEmpty()) {
            originIds = null;
        }

        return clientRepository.filterProducts(categoryIds, manufacturerIds, materialIds, originIds, minPrice, maxPrice, priceSort);
    }

    @Override
    public List<ProductIClientResponse> searchProducts(String keyword) {
        return clientRepository.searchProducts(keyword);
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
    public List<ColorClientResponse> findDistinctColorsByProductDetailId(Integer productDetailId) {
        return clientRepository.findDistinctColorsByProductDetailId(productDetailId);
    }

    @Override
    public List<SizeClientResponse> findDistinctSizesByProductDetailId(Integer productDetailId) {
        return clientRepository.findDistinctSizesByProductDetailId(productDetailId);
    }

    @Override
    public List<ProductDetailClientRespone> findByProductDetailColorAndSizeAndProductId(Integer colorId, Integer sizeId, Integer productId) {
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
    public Integer getQuantityProductDetailByID(Integer idProductDetail) {
        return clientRepository.getQuantityProductDetailByID(idProductDetail);
    }

    @Override
    public List<AddressShip> getListAddressShipByIDCustomer() {
        return clientRepository.getListAddressShipByIDCustomer();
    }

    ////////////////////////////
    @Override
    public Page<BillResponseManage> getAllBillByStatusDiss0(Integer idCustomer, String nameCheck, SearchBillByStatusRequest searchBillByStatusRequest, Date start, Date end, Pageable pageable) {
        return this.clientRepository.getAllBillByStatusDiss0(idCustomer, nameCheck, searchBillByStatusRequest.getStatusSearch(), start, end, pageable);
    }

    @Override
    public List<BillResponseManage> getAllBillByStatusDiss0(Integer idCustomer, String nameCheck, SearchBillByStatusRequest searchBillByStatusRequest, Date start, Date end) {
        return this.clientRepository.getAllBillByStatusDiss0(idCustomer, nameCheck, searchBillByStatusRequest.getStatusSearch(), start, end);
    }

    @Override
    public Integer getAvailableStockFromProductDetail(Integer productDetailId) {
        // Giả sử ProductDetail có trường quantity để lưu số lượng tồn kho
        ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);
        if (productDetail != null) {
            return productDetail.getQuantity();
        }
        return 0;
    }


}
