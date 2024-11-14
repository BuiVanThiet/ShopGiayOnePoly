package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.service.CustomerService;
import com.example.shopgiayonepoly.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff-api")
public class StaffRestController {
    @Autowired
    StaffService staffService;
    String keyWord = "";
    @GetMapping("/all-staff-status-dislike-0/{page}")
    public List<StaffResponse> getAllListStaffDislike0(@PathVariable("page") String page) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1, 5);
        Page<StaffResponse> pageStaff = staffService.searchStaffByKeywordPage(keyWord,pageable);
        return pageStaff.getContent();
    }

    @GetMapping("/max-page-staff-status-dislike-0")
    public Integer getMaxPageStaffDislike0() {
        System.out.println("so du lieu la " + this.staffService.searchStaffByKeyword(keyWord).size());
        Integer pageNumber = (int) Math.ceil((double) this.staffService.searchStaffByKeyword(keyWord).size() / 5);
        return pageNumber;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchStaffByKey(@RequestBody Map<String, String> data) {
        String key = data.get("key").trim();
        keyWord = key;
        return ResponseEntity.ok(keyWord);
    }
}
