package com.example.hlsiidb.service.impl;

import com.commonuser.entity.Role;
import com.commonuser.entity.User;
import com.example.hlsiidb.dao.RegisterDao;
import com.example.hlsiidb.dao.RoleDao;
import com.example.hlsiidb.dao.UserDao;
//import com.example.hlsiidb.entity.Role;
//import com.example.hlsiidb.entity.User;
//import com.example.hlsiidb.entity.UserQuery;
//import com.example.hlsiidb.service.RegisterService;
import com.example.hlsiidb.service.UserService;
import com.example.hlsiidb.vo.ReturnCode;
//import org.antlr.v4.runtime.InterpreterRuleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;


    @Autowired
    RegisterDao registerDao;

//    @Override
//    public int getIdByUserName(String userName) {
//        return userDao.getIdByUserName(userName);
//    }

    /**
     * 通过用户名获取有用户、角色和权限所有信息
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public User findByUserId(int id) {
        User user = userDao.findByUserId(id);
        //用户角色集合
        Role role = roleDao.findRoleListByUserId(user.getId());
        user.setRole(role);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findAllUserInfoByUsername(String userName) {
        User user = userDao.findByUsername(userName);
        if (user == null) {
            return null;
        }
        Role role = roleDao.findRoleListByUserId(user.getId());
        user.setRole(role);
        return user;
    }

//    /**
//     * 通过用户id仅获取用户信息
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public User findSimpleUserInfoById(int userId) {
//        return userDao.findById(userId);
//    }

    /**
     * 通过用户名仅获取用户信息
     *
     * @param
     * @return
     */
//    @Override
//    public User findSimpleUserInfoByUsername(String username) {
//        return userDao.findByUsername(username);
//    }
    @Override
    @Transactional(readOnly = false)
    public boolean addRegisterToUser(int id, String userType) {
        return userDao.addRegisterToUser(id, userType);
    }

    @Override
    public List<String> getAllUserRegisteredUserName() {
        return userDao.getAllUserRegisteredUserName();
    }

    @Override
    public List<String> getAllUserRegisteredEmail() {
        return userDao.getAllUserRegisteredEmail();
    }

    @Override
    @Transactional(readOnly = true)
    public String forgetPwd(String userName) {
        return userDao.forgetPwd(userName);
    }


    @Override
    public ReturnCode findUserAndPwd(String email) {
        String userName = userDao.findUserAndPwd(email);
        if (userName != null) {
            int radomInt = new Random().nextInt(999999);
            String randomPwd = radomInt + "";
            boolean resetSuccess = userDao.resetPwd(userName, randomPwd);
            if (resetSuccess) {
                String content = "你的用户名：" + userName + ",密码：" + randomPwd;
                if (registerDao.sendEmailForFindUser(email, content)) {
                    return new ReturnCode(true, "您的用户名和密码已发送到邮箱!");
                } else {
                    return new ReturnCode(false, "密码已重置，但邮件发送失败!");
                }
            } else {
                return new ReturnCode(false, "密码重置失败!");
            }
        } else {
            return new ReturnCode(false, "改邮箱没注册!");
        }
    }

    @Override
    public ReturnCode updateUser(String userName, String organization, String department, String email) {
        if(userDao.updateUser(userName, organization, department, email)){
            return new ReturnCode(true,"用户信息更新成功!");
        }else{
            return new ReturnCode(false,"用户信息更新失败!");
        }
    }

    @Override
    public ReturnCode changePwd(String userName, String newPwd) {
        return userDao.changePwd(userName, newPwd);
    }
}
