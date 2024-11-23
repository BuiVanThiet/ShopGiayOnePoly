package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.Shift.TimekeepingFilterRequest;
import com.example.shopgiayonepoly.repositores.TimekeepingRepository;
import com.example.shopgiayonepoly.service.TimekeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimekeepingImplement implements TimekeepingService {
    @Autowired
    TimekeepingRepository timekeepingRepository;
    @Override
    public List<Object[]> getAllTimekeeping(TimekeepingFilterRequest timekeepingFilterRequest) {
        return timekeepingRepository.getAllTimekeeping(
                timekeepingFilterRequest.getSearchTerm(),
                timekeepingFilterRequest.getStartDate(),
                timekeepingFilterRequest.getEndDate(),
                timekeepingFilterRequest.getStartTime(),
                timekeepingFilterRequest.getEndTime(),
                timekeepingFilterRequest.getTimekeeping_typeCheck()
        );
    }

    @Override
    public void getCheckIn(Integer idStaff, String note) {
        timekeepingRepository.getCheckIn(idStaff,note);
    }
    @Override
    public void getCheckOut(Integer idStaff, String note) {
        timekeepingRepository.getCheckOut(idStaff,note);
    }

    @Override
    public List<Object[]> getCheckStaffAttendanceYet(
            Integer idStaff,
            Integer timekeepingTypeCheck
    ) {
        return timekeepingRepository.getCheckStaffAttendanceYet(idStaff,timekeepingTypeCheck);
    }
}
