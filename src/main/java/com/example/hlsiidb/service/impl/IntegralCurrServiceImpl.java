package com.example.hlsiidb.service.impl;

import com.example.hlsiidb.dao.IntegralCurrDao;
import com.example.hlsiidb.entity.IntegralCurr;
import com.example.hlsiidb.service.IntegralCurrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntegralCurrServiceImpl implements IntegralCurrService {

    @Autowired
    private IntegralCurrDao integralCurrDao;

    @Override
    public List<IntegralCurr> getIntegralCurr(String startDate, String endDate) {
        return integralCurrDao.getIntegralHistory(startDate, endDate);
    }
}
