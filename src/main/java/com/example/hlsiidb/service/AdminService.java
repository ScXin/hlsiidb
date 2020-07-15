package com.example.hlsiidb.service;

//import com.example.hlsiidb.entity.Role;
//import com.example.hlsiidb.entity.User;

import com.commonuser.entity.Role;
import com.commonuser.entity.User;
import com.example.hlsiidb.vo.ReturnCode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminService {


//用户信息管理与查询接口

    /**
     * 获取全部用户和角色信息
     *
     * @return
     */
    List<User> findAllUserRoleInfo();

    /**
     * 根据用户名获取用户信息和角色信息
     *
     * @param id
     * @return
     */
    User findUserRoleInfoByUserId(int id);

//    /**
//     * 插入新用户
//     * @param username
//     * @param password
//     * @param createTime
//     * @return
//     */
//    boolean creatNewUserInfo(String username, String password, String createTime);


    /**
     * 通过用户名删除用户
     *
     * @param
     * @return
     */
    boolean deleteUserInfo(int userId);

    /**
     * 通过用户名修改用户信息
     *
     * @param username
     * @param password
     * @param createTime
     * @return
     */
//    boolean updateUserInfo(String username, String password, String organization, String department, String createTime);


//修改用户角色的接口

    /**
     * 获取所有用户信息
     *
     * @return
     */
    List<User> findAllUser();

    /**
     * 获取所有角色信息
     *
     * @return
     */
    List<Role> findAllRole();

//    /**
//     * 为用户增加新角色
//     * @param username
//     * @param rolename
//     * @return
//     */
//    boolean addRoleToUser(String username,String rolename);


    /**
     * 通过用户名找到用户信息
     */
    public User findUserByName(String userName);

    /**
     * 修改用户角色
     *
     * @param userId
     * @param oldRole
     * @param newRole
     * @return
     */
    boolean updateRoleOfUser(int userId, String oldRole, String newRole);

    /**
     * 删除用户角色
     *
     * @param userId
     * @param rolename
     * @return
     */
    boolean deleteRoleOfUser(int userId, String rolename);


    ReturnCode changeUserType(int userIs, String type);

}
