package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.Shift.TimekeepingFilterRequest;

import java.util.List;

public interface TimekeepingService {
    List<Object[]> getAllTimekeeping(TimekeepingFilterRequest timekeepingFilterRequest);

    void getCheckIn(Integer idStaff, String note);

    void getCheckOut(Integer idStaff, String note);

    List<Object[]> getCheckStaffAttendanceYet(
            Integer idStaff,
            Integer timekeepingTypeCheck
    );
}
