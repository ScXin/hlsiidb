package com.example.hlsiidb.service.impl;

import com.example.hlsiidb.dao.WhiteIPDao;
import com.example.hlsiidb.service.WhiteIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ScXin
 * @date 7/14/2020 12:15 PM
 */
@Service
public class WhiteIPServiceImpl implements WhiteIPService {

    @Autowired
    private WhiteIPDao whiteIPDao;

    @Override
    public List<String> getAllWhiteIP() {
        return whiteIPDao.egtAllWhiteIP();
    }

}
