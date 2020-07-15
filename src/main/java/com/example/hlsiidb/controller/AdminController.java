package com.example.hlsiidb.controller;

//import com.example.hlsiidb.entity.Role;
//import com.example.hlsiidb.entity.User;

import com.commonuser.entity.Register;
import com.commonuser.entity.Role;
import com.commonuser.entity.User;

import com.example.hlsiidb.service.AdminService;
import com.example.hlsiidb.service.RegisterService;
import com.example.hlsiidb.service.UserService;
import com.example.hlsiidb.vo.ReturnCode;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
//import io.swagger.models.auth.In;
//import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hlsii/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @Autowired
    RegisterService registerService;

    //用户查询方法
    @ApiOperation(value = "获取全部用户和角色信息")
    @GetMapping("getAllURInfo")
    public List<User> getAllUserAndRoleInfo() {
        return adminService.findAllUserRoleInfo();
    }


    @ApiOperation(value = "通过用户名id获取用户和角色信息")
    @GetMapping("getURInfoByUserId/{id}")
    public User getUserAndRoleInfoByUserId(@PathVariable("id") String id) {
        int userId = Integer.parseInt(id);
        return adminService.findUserRoleInfoByUserId(userId);
    }


    @ApiOperation(value = "通过用户id删除用户,同时删除用户角色管理表内容")
    @GetMapping("deleteUserInfo/{id}")
    public boolean deleteUser(@PathVariable("id") int id) {
        return adminService.deleteUserInfo(id);
    }

//    @ApiOperation(value = "通过用户名更新用户信息")
//    @PostMapping("updateUserInfo/{username}/{password}/{organization}/{department}/{createTime}")
//    public boolean updateUser(@PathVariable("username") String username,
//                              @PathVariable("password") String password,
//                              @PathVariable("organization") String organization,
//                              @PathVariable("department") String department,
//                              @PathVariable("createTime") String createTime) {
//        return adminService.updateUserInfo(username, password, organization, department, createTime);
//    }

    //用户角色管理方法
//    @ApiOperation(value = "获取所有用户信息")
//    @GetMapping("getAllUserInfo")
//    public List<User> getAllUserInfo() {
//        return adminService.findAllUserRoleInfo();
//    }

    @ApiOperation(value = "获取所有角色信息")
    @GetMapping("getAllRoleInfo")
    public List<Role> getAllRoleInfo() {
        return adminService.findAllRole();
    }

//    @ApiOperation(value = "通过用户名和角色名为用户新增角色")
//    @PostMapping("addNewURInfo/{username}/{rolename}")
//    public boolean addNewUserAndRoleInfo(@PathVariable("username") String username, @PathVariable("rolename") String rolename) {
//        Role role= getUserAndRoleInfoByUsername(username).getRole();
//        for (Role role : roles) {
//            if (role.getName().equals(rolename)) {
//                return false;
//            }
//        }
//        return adminService.addRoleToUser(username, rolename);
//    }
//
//    @ApiOperation(value = "通过用户id和角色名修改用户角色")
//    @PostMapping("updateURInfo/{id}/{rolenameOld}/{rolenameNew}")
//    public boolean updateUserAndRoleInfo(@PathVariable("id") String id, @PathVariable("rolenameOld") String rolenameOld, @PathVariable("rolenameNew") String rolenameNew) {
//        int userId = Integer.parseInt(id);
//        return adminService.updateRoleOfUser(userId, rolenameOld, rolenameNew);
//    }

//    @ApiOperation(value = "通过用户id和角色名删除用户角色")
//    @GetMapping("deleteURInfo/{userId}/{rolename}")
//    public boolean deleteUserAndRoleInfo(@PathVariable("userId") int userId, @PathVariable("rolename") String rolename) {
//        return adminService.deleteRoleOfUser(userId, rolename);
//    }

    @ApiOperation(value = "将注册表的用户注册信息加入到用户表user中")
    @PostMapping("addUserByRegister/{id}/{userType}")
    public boolean insertUserByRegisterInfo(@PathVariable("id") int id,
                                            @PathVariable("userType") String userType) {
        return userService.addRegisterToUser(id, userType);
    }

    @ApiOperation(value = "修改用户的类型")
    @PostMapping("changeUserType/{id}/{userType}")
    public ReturnCode changeUserType(@PathVariable("id") int id,
                                     @PathVariable("userType") String userType) {
        return adminService.changeUserType(id, userType);
    }


    @PostMapping("getAllRegisteredUserName")
    public List<String> getAllUserRegisteredUserName() {
        return userService.getAllUserRegisteredUserName();
    }


    @GetMapping("getAllRegisters")
    public List<Register> getAllRegisters() {
        return registerService.getAllRegisters();
    }


    //    @DeleteMapping("deleteRegister/{userName}")
//    @ApiOperation("删除注册的用户")
//    public boolean deleteRegister(@PathVariable("userName") String userName){
//        return registerService.deleteRegister(userName);
//    }
    @DeleteMapping("deleteRegister/{userId}")
    @ApiOperation("根据id删除注册的用户")
    public boolean deleteRegister(@PathVariable("userId") int userId) {

        return registerService.deleteRegister(userId);
    }


}
