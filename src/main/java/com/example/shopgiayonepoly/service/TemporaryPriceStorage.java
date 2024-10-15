package com.example.shopgiayonepoly.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class TemporaryPriceStorage {
    private Map<Integer, BigDecimal> originalPriceMap = new HashMap<>();

    public void storeOriginalPrice(Integer productId, BigDecimal originalPrice) {
        originalPriceMap.put(productId, originalPrice);
    }

    public BigDecimal getOriginalPrice(Integer productId) {
        return originalPriceMap.get(productId);
    }

    public void removeOriginalPrice(Integer productId) {
        originalPriceMap.remove(productId);
    }
}
