package com.example.hlsiidb.service.impl;

import com.commonuser.entity.Role;
import com.commonuser.entity.User;
import com.example.hlsiidb.dao.RoleDao;
import com.example.hlsiidb.dao.UserDao;
//import com.example.hlsiidb.entity.Role;
//import com.example.hlsiidb.entity.User;
import com.example.hlsiidb.service.AdminService;
import com.example.hlsiidb.vo.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;


    /**
     * 获取全部用户和角色信息
     *
     * @return
     */
    @Override
    public List<User> findAllUserRoleInfo() {
        List<User> users = userDao.findAllUser();

        for (User user : users) {
            Role role = roleDao.findSimpleRoleListByUserId(user.getId());
            user.setRole(role);
        }
        return users;
    }

    /**
     * 根据用户名获取用户信息和角色信息
     *
     * @param id
     * @return
     */
    @Override
    public User findUserRoleInfoByUserId(int id) {
        User user = userDao.findByUserId(id);

        Role role = roleDao.findSimpleRoleListByUserId(user.getId());
        user.setRole(role);
        return user;
    }


    /**
     * 插入新用户
     *
     * @param username
     * @param password
     * @param createTime 格式 '1970-01-01 00:00:00'
     * @return
     */
//    @Override
//    public boolean creatNewUserInfo(String username, String password, String createTime) {
//        if (userDao.findByUsername(username) != null) {
//            return false;
//        }
//        return userDao.insertUser(username, password, createTime);
//    }

    /**
     * 通过用户名ID,同时删除用户角色关联表内容
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteUserInfo(int userId) {
        if (userDao.findByUserId(userId) == null) {
            return false;
        } else {
            int userid = userDao.findByUserId(userId).getId();
            boolean f1 = userDao.deleteUR(userid);
            boolean f2 = userDao.deleteUser(userid);
            return f1 & f2;
        }
    }

    /**
     * 通过用户名修改用户信息
     *
     * @param username
     * @param password
     * @param createTime
     * @return
     */
//    @Override
//    public boolean updateUserInfo(String username, String password, String organization, String department, String createTime) {
//        return userDao.updateUser(username, organization, department, createTime);
//    }


    //修改用户角色的接口

    /**
     * 获取所有用户信息
     *
     * @return
     */
    @Override
    public List<User> findAllUser() {
        return userDao.findAllUser();

    }


    /**
     * 获取所有角色信息
     *
     * @return
     */
    @Override
    public List<Role> findAllRole() {
        return roleDao.findAllRole();
    }


//    /**
//     * 为用户增加新角色
//     *
//     * @param username
//     * @param rolename
//     * @return
//     */
//    @Override
//    public boolean addRoleToUser(String username, String rolename) {
//        User user = userDao.findByUsername(username);
//        Role role = roleDao.findRoleByRolename(rolename);
//        return userDao.addUR(user.getId(), role.getId());
//    }

    @Override
    public ReturnCode changeUserType(int userId, String type) {
        boolean is = userDao.changeUserType(userId, type);
        if (is) {
            return new ReturnCode(true, "用户类型修改成功!");
        } else {
            return new ReturnCode(false, "用户类型修改失败!");
        }
    }

    /**
     * 修改用户角色
     *
     * @param userId
     * @param newRole
     * @return
     */
    @Override
    public boolean updateRoleOfUser(int userId, String oldRole, String newRole) {
        User user = userDao.findByUserId(userId);
        Role roleOld = roleDao.findRoleByRolename(oldRole);
        Role roleNew = roleDao.findRoleByRolename(newRole);
        return userDao.updateUR(user.getId(), roleOld.getId(), roleNew.getId());
    }

    /**
     * 删除用户角色
     *
     * @param userId
     * @param rolename
     * @return
     */
    @Override
    public boolean deleteRoleOfUser(int userId, String rolename) {
        User user = userDao.findByUserId(userId);
        Role role = roleDao.findRoleByRolename(rolename);
        return userDao.deleteUR(user.getId(), role.getId());
    }

    @Override
    public User findUserByName(String userName) {
        User user = userDao.findByUsername(userName);
        Role role = roleDao.findSimpleRoleListByUserId(user.getId());
        user.setRole(role);
        return user;
    }

}
