package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.CanvasjsChartData;
import com.example.shopgiayonepoly.service.CanvasjsChartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class CanvasjsChartServiceImpl implements CanvasjsChartService {
    @Override
    public List<List<Map<Object, Object>>> getCanvasjsChartData() {
        return CanvasjsChartData.getCanvasjsDataList();
    }
}
