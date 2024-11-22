package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.Shift;
import com.example.shopgiayonepoly.repositores.ShiftRepository;
import com.example.shopgiayonepoly.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftImplement implements ShiftService {
    @Autowired
    ShiftRepository shiftRepository;

    @Override
    public List<Shift> findAll() {
        return shiftRepository.findAll();
    }

    @Override
    public <S extends Shift> S save(S entity) {
        return shiftRepository.save(entity);
    }

    @Override
    public Optional<Shift> findById(Integer integer) {
        return shiftRepository.findById(integer);
    }

    @Override
    public void delete(Shift entity) {
        shiftRepository.delete(entity);
    }

    @Override
    public Page<Shift> findAll(Pageable pageable) {
        return shiftRepository.findAll(pageable);
    }
    @Override
    public List<Object[]> getAllShiftByTime(
            String startTimeBegin,
            String startTimeEnd,
            String endTimeBegin,
            String endTimeEnd,
            Integer statusShift,
            Integer status
    ) {
        return shiftRepository.getAllShiftByTime(
                startTimeBegin,
                startTimeEnd,
                endTimeBegin,
                endTimeEnd,
                statusShift,
                status
        );
    }

    @Override
    public List<Object[]> getAllStaffByShift(
            Integer idShiftCheck,
            String searchTerm,
            Integer checkShift
    ) {
        return shiftRepository.getAllStaffByShift(idShiftCheck,searchTerm,checkShift);
    }
}
