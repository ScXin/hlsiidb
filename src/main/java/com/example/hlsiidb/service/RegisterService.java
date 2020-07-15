package com.example.hlsiidb.service;


import com.commonuser.entity.Register;
import com.example.hlsiidb.vo.ReturnCode;

import java.util.List;

/**
 * @author ScXin
 * @date 7/6/2020 1:09 PM
 */
public interface RegisterService {

    public ReturnCode insertRegister(String userName, String password, String organization,
                                     String depeartment, String email);

    public Register getRegisterByUserName(String userName);

    public boolean deleteRegister(String userName);

    public boolean deleteRegister(int id);

    public List<Register> getAllRegisters();
}
