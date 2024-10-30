package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.bill.SearchBillByStatusRequest;
import com.example.shopgiayonepoly.dto.response.bill.BillResponseManage;
import com.example.shopgiayonepoly.dto.response.bill.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.dto.response.bill.ClientBillInformationResponse;
import com.example.shopgiayonepoly.dto.response.bill.InformationBillByIdBillResponse;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.repositores.BillRepository;
import com.example.shopgiayonepoly.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillImplement implements BillService {
    @Autowired
    BillRepository billRepository;

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public <S extends Bill> S save(S entity) {
        return billRepository.save(entity);
    }

    @Override
    public Optional<Bill> findById(Integer integer) {
        return billRepository.findById(integer);
    }

    @Override
    public long count() {
        return billRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        billRepository.deleteById(integer);
    }

    @Override
    public List<Bill> findAll(Sort sort) {
        return billRepository.findAll(sort);
    }

    @Override
    public Page<Bill> findAll(Pageable pageable) {
        return billRepository.findAll(pageable);
    }
    @Override
    public List<Bill> getBillByStatusNew(Pageable pageable) {
        return billRepository.getBillByStatusNew(pageable);
    }

    @Override
    public List<Customer> getClientNotStatus0() {
        return this.billRepository.getClientNotStatus0();
    }

    @Override
    public BillTotalInfornationResponse findBillVoucherById(Integer id) {
        return this.billRepository.findBillVoucherById(id);
    }

    @Override
    public List<ClientBillInformationResponse> getClientBillInformationResponse(Integer idClient) {
        return this.billRepository.getClientBillInformationResponse(idClient);
    }

    @Override
    public Page<Voucher> getVouCherByBill(Integer idBill,String keyword, Pageable pageable) {
        return billRepository.getVoucherByBill(idBill,keyword,pageable);
    }

    @Override
    public List<Voucher> getVoucherByBill(Integer idBill, String keyword) {
        return billRepository.getVoucherByBill(idBill,keyword);
    }

//    @Override
//    public Page<BillResponseManage> getAllBillByStatusDiss0(String nameCheck, Integer status, Pageable pageable) {
//        return this.billRepository.getAllBillByStatusDiss0(nameCheck,status,pageable);
//    }
//
//    @Override
//    public List<BillResponseManage> getAllBillByStatusDiss0(String nameCheck, Integer status) {
//        return this.billRepository.getAllBillByStatusDiss0(nameCheck,status);
//    }
    @Override
    public Page<BillResponseManage> getAllBillByStatusDiss0(String nameCheck, SearchBillByStatusRequest searchBillByStatusRequest, Pageable pageable) {
        return this.billRepository.getAllBillByStatusDiss0(nameCheck,searchBillByStatusRequest.getStatusSearch(),pageable);
    }

    @Override
    public List<BillResponseManage> getAllBillByStatusDiss0(String nameCheck, SearchBillByStatusRequest searchBillByStatusRequest) {
        return this.billRepository.getAllBillByStatusDiss0(nameCheck,searchBillByStatusRequest.getStatusSearch());
    }
    @Override
    public InformationBillByIdBillResponse getInformationBillByIdBill(Integer idBill) {
        return this.billRepository.getInformationBillByIdBill(idBill);
    }
    @Override
    public List<Object[]> getInfoPaymentByIdBill(Integer id) {
        return this.billRepository.getInfoPaymentByIdBill(id);
    }

    @Override
    public List<Object[]> getBillByIdCreatePDF(Integer id) {
        return this.billRepository.getBillByIdCreatePDF(id);
    }

    @Override
    public List<Object[]> getBillDetailByIdBillPDF(Integer id) {
        return this.billRepository.getBillDetailByIdBillPDF(id);
    }
    @Override
    public List<Object[]> getInfomationBillReturn(Integer id) {
        return this.billRepository.getInfomationBillReturn(id);
    }

    @Override
    public ProductDetail getProductDteailById(Integer id) {
        return billRepository.getProductDteailById(id);
    }

    @Override
    public String getDiscountBill(Integer idBill) {
        return this.billRepository.getDiscountBill(idBill);

    }
    @Override
    public long invoicePaid() {
        return billRepository.invoicePaid();
    }

}
