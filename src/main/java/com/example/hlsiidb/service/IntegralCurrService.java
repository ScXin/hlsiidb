package com.example.hlsiidb.service;

import com.example.hlsiidb.entity.IntegralCurr;

import java.util.List;

public interface IntegralCurrService {
    List<IntegralCurr> getIntegralCurr(String startDate, String endDate);
}
