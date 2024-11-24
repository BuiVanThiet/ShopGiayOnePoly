package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ShiftService {
    List<Shift> findAll();

    <S extends Shift> S save(S entity);

    Optional<Shift> findById(Integer integer);

    void delete(Shift entity);

    Page<Shift> findAll(Pageable pageable);

    List<Object[]> getAllShiftByTime(
            String startTimeBegin,
            String startTimeEnd,
            String endTimeBegin,
            String endTimeEnd,
            Integer statusShift,
            Integer status
    );

    List<Object[]> getAllStaffByShift(
            Integer idShiftCheck,
            String searchTerm,
            Integer checkShift
    );
}
