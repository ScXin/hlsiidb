package com.example.hlsiidb.service;

//import com.example.hlsiidb.entity.User;

import com.commonuser.entity.User;
import com.example.hlsiidb.vo.ReturnCode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    /**
     * 根据用户名获取全部用户信息包括角色、权限
     *
     * @param username
     * @return
     */
    User findAllUserInfoByUsername(String username);

    /**
     * 根据用户名得到id
     *
     * @param userName
     * @return
     */
//    public int getIdByUserName(String userName);

    /**
     * 根据id获取用户基本信息
     *
     * @param userId
     * @return
     */
    User findByUserId(int userId);


    /**
     * 根据用户名获取用户基本信息
     *
     * @param username
     * @return
     */
//    User findSimpleUserInfoByUsername(String username);

    /**
     * 将注册表的用户注册信息加入用户表
     *
     * @param id
     * @return
     */
    @Transactional("add user and delete register")
    boolean addRegisterToUser(int id, String userType);


    /**
     * 得到所有的已注册用户名
     *
     * @return
     */
    public List<String> getAllUserRegisteredUserName();


    /**
     *
     */
    public List<String> getAllUserRegisteredEmail();

    /**
     * 忘记密码调用的方法
     *
     * @param userName
     * @return
     */
    public String forgetPwd(String userName);


    /**
     * 根据邮箱 找到用户和密码
     */
    public ReturnCode findUserAndPwd(String email);

    /**
     * 更新用户信息
     *
     * @return
     */
    public ReturnCode updateUser(String userName,String organization, String department, String email);


    /**
     * 修改用户的密码
     */
    public ReturnCode changePwd(String userName, String newPwd);
}
