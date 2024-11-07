package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckMark2Request;
import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckRequest;
import com.example.shopgiayonepoly.dto.response.bill.CategoryProductResponse;
import com.example.shopgiayonepoly.dto.response.bill.ImageProductResponse;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.Category;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.repositores.BillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BillDetailImplement implements com.example.shopgiayonepoly.service.BillDetailService {
    @Autowired
    BillDetailRepository billDetailRepository;

    @Override
    public List<BillDetail> findAll() {
        return billDetailRepository.findAll();
    }

    @Override
    public <S extends BillDetail> S save(S entity) {
        return billDetailRepository.save(entity);
    }

    @Override
    public Optional<BillDetail> findById(Integer integer) {
        return billDetailRepository.findById(integer);
    }

    @Override
    public long count() {
        return billDetailRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        billDetailRepository.deleteById(integer);
    }

    @Override
    public void delete(BillDetail entity) {
        billDetailRepository.delete(entity);
    }

    @Override
    public List<BillDetail> findAll(Sort sort) {
        return billDetailRepository.findAll(sort);
    }

    @Override
    public Page<BillDetail> findAll(Pageable pageable) {
        return billDetailRepository.findAll(pageable);
    }
    @Override
    public Page<BillDetail> getBillDetailByIdBill(Integer idBill, Pageable pageable) {
        return billDetailRepository.getBillDetailByIdBill(idBill,pageable);
    }

    @Override
    public List<ProductDetail> getAllProductDetail() {
        return this.billDetailRepository.getAllProductDetail();
    }
    @Override
    public ProductDetail getProductDetailById(Integer id) {
        return this.billDetailRepository.getProductDetailById(id);
    }
    @Override
    public Integer getBillDetailExist(Integer idBill, Integer idPDT) {
        List<Integer> result = this.billDetailRepository.getBillDetailExist(idBill, idPDT);
        return result.isEmpty() ? -1 : result.get(0);
    }
    @Override
    public List<BillDetail> getBillDetailByIdBill(Integer idBill) {
        return billDetailRepository.getBillDetailByIdBill(idBill);
    }

    @Override
    public Integer getFirstBillDetailIdByIdBill(Integer idBill) {
        return   this.billDetailRepository.getFirstBillDetailIdByIdBill(idBill);
    }

    @Override
    public List<ProductDetail> getProductDetailSale(ProductDetailCheckRequest productDetailCheckRequest) {
        return this.billDetailRepository.findProductDetailSale(
                productDetailCheckRequest.getNameProduct(),
                productDetailCheckRequest.getIdSize(),
                productDetailCheckRequest.getIdColor(),
                productDetailCheckRequest.getIdMaterial(),
                productDetailCheckRequest.getIdManufacturer(),
                productDetailCheckRequest.getIdOrigin(),
                productDetailCheckRequest.getIdCategories()
        );
    }
    @Override
    public BigDecimal getTotalAmountByIdBill(Integer id) {
        return this.billDetailRepository.getTotalAmountByIdBill(id);
    }

    @Override
    public List<Category> getAllCategores() {
        return this.billDetailRepository.getAllCategores();
    }
    @Override
    public List<Object[]> findProductDetailSaleTest(ProductDetailCheckMark2Request productDetailCheckRequest,Integer idBill) {
        return this.billDetailRepository.findProductDetailSaleTest(
                productDetailCheckRequest.getNameProduct(),            // Tên sản phẩm
                productDetailCheckRequest.getIdCategories(),           // Danh sách danh mục
                productDetailCheckRequest.getIdColors(),               // Danh sách màu sắc
                productDetailCheckRequest.getIdSizes(),                // Danh sách kích thước
                productDetailCheckRequest.getIdManufacturers(),        // Danh sách nhà sản xuất
                productDetailCheckRequest.getIdMaterials(),            // Danh sách chất liệu
                productDetailCheckRequest.getIdOrigins(),              // Danh sách nơi xuất xứ
                productDetailCheckRequest.getIdSoles(),                 // Danh sách đế giày
                idBill
        );
    }
    @Override
    public List<ImageProductResponse> getImageByBill(Integer id) {
        return this.billDetailRepository.getImageByBill(id);
    }
    @Override
    public List<CategoryProductResponse> getCategoryByBill(Integer id) {
        return this.billDetailRepository.getCategoryByBill(id);
    }

}
