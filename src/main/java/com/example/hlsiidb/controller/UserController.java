package com.example.hlsiidb.controller;

import com.commonuser.entity.User;
import com.example.hlsiidb.service.UserService;
import com.example.hlsiidb.vo.ReturnCode;
import io.swagger.annotations.ApiOperation;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ScXin
 * @date 7/10/2020 9:59 AM
 */
@RestController
@RequestMapping("/hlsii/user")
public class UserController {

    @Autowired
    UserService userService;

//    @ApiOperation("更新用户信息")
//    @PostMapping("/updateUserInfo/{userName}/{password}/{organization}/{department}/{email}")
//    public boolean updateUser(@PathVariable("userName") String userName,
//                              @PathVariable("password") String password,
//                              @PathVariable("organization") String organization,
//                              @PathVariable("department") String department,
//                              @PathVariable("email") String email) {
//        return userService.updateUser(userName, password, organization, department, email);
//    }


    @ApiOperation("修改用户密码")
    @PostMapping("/changeUserPwd/{userName}/{oldPwd}/{newPwd}")
    public ReturnCode changePwd(@PathVariable("userName") String userName,
                                @PathVariable("oldPwd") String oldPwd,
                                @PathVariable("newPwd") String newPwd) {
        User user = userService.findAllUserInfoByUsername(userName);
        String encryptOldPwd = encrypt(oldPwd, userName);
        if (!user.getPassword().equals(encryptOldPwd)) {
            return new ReturnCode(false, "原始密码输入错误!");
        }
        return userService.changePwd(userName, newPwd);
    }


    @PostMapping("/changeUserInfo/{userName}/{organization}/{department}/{email}")
    public ReturnCode changeUserInfo(@PathVariable("userName") String userName,
                                     @PathVariable("organization") String organization,
                                     @PathVariable("department") String department,
                                     @PathVariable("email") String email) {
        return userService.updateUser(userName, organization, department, email);
    }

    @GetMapping("getUserInfoByName/{userName}")
    public User getUserInfoByName(@PathVariable("userName") String userName) {
        return userService.findAllUserInfoByUsername(userName);
    }

    public String encrypt(String pwd, String salt) {
        Object s = ByteSource.Util.bytes(salt);
        SimpleHash simpleHash = new SimpleHash("MD5", pwd, s, 2);
        return simpleHash.toString();
    }

}
