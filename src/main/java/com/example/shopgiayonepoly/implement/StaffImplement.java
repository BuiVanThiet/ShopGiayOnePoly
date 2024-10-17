package com.example.shopgiayonepoly.implement;

import com.cloudinary.Cloudinary;
import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffRepository;
import com.example.shopgiayonepoly.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffImplement implements StaffService {
    private final Cloudinary cloudinary;

    @Autowired
    StaffRepository staffRepository;

    @Override
    public List<StaffResponse> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    @Override
    public Page<Staff> getAllStaffByPage(Pageable pageable) {
        return staffRepository.getAllStaffByPage(pageable);
    }

    @Override
    public List<StaffResponse> searchStaffByKeyword(String key) {
        return staffRepository.searchStaffByKeyword(key);
    }

    @Override
    public Page<Staff> searchStaffByKeywordPage(String key, Pageable pageable) {
        return staffRepository.searchStaffByKeywordPage(key, pageable);
    }

    @Override
    public <S extends Staff> S save(S entity) {
        return staffRepository.save(entity);
    }

    @Override
    public Optional<Staff> findById(Integer integer) {
        return staffRepository.findById(integer);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void updateStaff(StaffRequest staffRequest) {
        Staff staff = new Staff();
        BeanUtils.copyProperties(staffRequest, staff);
        staffRepository.save(staff);
    }

    @Override
    public Staff getOne(Integer integer) {
        return staffRepository.findById(integer).get();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public List<Staff> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Staff> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteStaff(Integer id) {
        staffRepository.deleteBySetStatus(id);
    }

    @Override
    public String uploadFile(MultipartFile multipartFile,Integer idStaff) throws IOException {
        String nameImage = UUID.randomUUID().toString();
        Staff staff = this.staffRepository.findById(idStaff).orElse(null);
        staff.setImage(nameImage);
        this.staffRepository.save(staff);
        // Đẩy file lên Cloudinary với tên file gốc
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", nameImage)) // Đặt tên file khi upload
                .get("url")
                .toString();
    }


}
