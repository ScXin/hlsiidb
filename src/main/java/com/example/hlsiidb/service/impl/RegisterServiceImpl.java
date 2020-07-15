package com.example.hlsiidb.service.impl;

import com.commonuser.entity.Register;
import com.example.hlsiidb.dao.RegisterDao;

import com.example.hlsiidb.service.RegisterService;
import com.example.hlsiidb.vo.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ScXin
 * @date 7/6/2020 1:12 PM
 */
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RegisterDao registerDao;

    @Override
    @Transactional(readOnly = false)
    public ReturnCode insertRegister(String userName, String password, String organization,
                                     String depeartment, String email) {
        return registerDao.insertRegister(userName, password, organization, depeartment, email);
    }

    @Override
    public Register getRegisterByUserName(String userName) {
        return registerDao.getRegisterByUserName(userName);
    }

    @Override
    public boolean deleteRegister(String userName) {
        return registerDao.deleteRegister(userName);
    }

    @Override
    public boolean deleteRegister(int id) {
        return registerDao.deleteRegister(id);
    }
    @Override
    public List<Register> getAllRegisters(){
        return registerDao.getAllRegisters();
    }
}
