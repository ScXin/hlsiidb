package com.example.hlsiidb.controller;

import com.commonuser.entity.Register;
import com.commonuser.entity.Role;
import com.example.hlsiidb.commdef.OperationType;
import com.example.hlsiidb.entity.JsonData;
//import com.example.hlsiidb.entity.Role;

import com.example.hlsiidb.entity.UserQuery;
import com.example.hlsiidb.service.AdminService;
import com.example.hlsiidb.service.RecordUserLogService;
import com.example.hlsiidb.service.RegisterService;
import com.example.hlsiidb.service.UserService;
import com.example.hlsiidb.vo.ReturnCode;
import io.swagger.annotations.ApiOperation;
//import oracle.jdbc.proxy.annotation.Post;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hlsii/pub")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;


    @Autowired
    RegisterService registerService;

    @Autowired
    RecordUserLogService recordUserLogService;

    @ApiOperation(value = "没有登陆时触发的接口")
    @GetMapping("needLogin")
    public JsonData needLogin() {
        return JsonData.buildSuccess("温馨提示：请使用账号登陆", -1);
    }

    @ApiOperation(value = "没有权限时触发的接口")
    @GetMapping("notPermit")
    public JsonData notPermit() {
        return JsonData.buildSuccess("温馨提示：您的账号没有权限", -2);
    }

    /**
     * 登录接口
     *
     * @param userQuery
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "登录接口")
    @PostMapping("login")
    public JsonData login(@RequestBody UserQuery userQuery, HttpServletRequest request, HttpServletResponse response) {

        Subject subject = SecurityUtils.getSubject();

        Map<String, Object> info = new HashMap<>();
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userQuery.getName(), userQuery.getPwd());
            subject.login(usernamePasswordToken);

            recordUserLogService.logOperation(OperationType.LOGIN);
            info.put("msg", "登陆成功");
            info.put("session_id", subject.getSession().getId());

            //获取用户角色列表。
            Role role = adminService.findUserByName(userQuery.getName()).getRole();

            if (role.getId() == 1) {
                info.put("role", 1);
            }
            if (!info.containsKey("role")) {
                info.put("role", 2);
            }
//            for (Role r : roles) {
//                if (r.getId() == 1) {
//                    //若为管理员，直接返回，此时role：1。
//                    info.put("role", 1);
//                    break;
//                }
//            }
//
//            //若只是普通用户则返回role：2
//            if (!info.containsKey("role")) {
//                info.put("role", 2);
//            }
            return JsonData.buildSuccess(info);

        } catch (Exception e) {
            e.printStackTrace();

            return JsonData.buildError("账号或密码错误", -3);
        }
    }


    @RequestMapping("logout")
    public JsonData findMyPlayRecord(){


        Subject subject = SecurityUtils.getSubject();

        if(subject.getPrincipals() != null ){
            recordUserLogService.logOperation(OperationType.LOGOUT);
            SecurityUtils.getSubject().logout();
            return JsonData.buildSuccess("注销成功!");
        }
        return  JsonData.buildSuccess("当前没有登陆!");
    }
    @ApiOperation(value = "注册接口")
    @PostMapping("/signUp/{userName}/{password}/{organization}/{department}/{email}")
    public ReturnCode register(@PathVariable("userName") String userName,
                               @PathVariable("password") String password,
                               @PathVariable("organization") String organization,
                               @PathVariable("department") String department,
                               @PathVariable("email") String email) {
        List<String> allUsers = userService.getAllUserRegisteredUserName();
        List<String> allEmails = userService.getAllUserRegisteredEmail();
        if (allUsers.contains(userName)) {
            return new ReturnCode(false, "该用户名已被注册！");
        }
        if (allEmails.contains(email)) {
            return new ReturnCode(false, "该邮箱已被注册!");
        }
        return registerService.insertRegister(userName, password, organization, department, email);
    }

    @ApiOperation(value = "忘记密码接口")
    @PostMapping("/forgetPwd/{userName}")
    public String forgetPassword(@PathVariable("userName") String userName) {
        if (userService.findAllUserInfoByUsername(userName) == null) {
            return "用户名不存在";
        }
        return userService.forgetPwd(userName);
    }

    @ApiOperation("通过邮箱找回用户名和密码")
    @PostMapping("/findUserAndPwd/{email}")
    public ReturnCode findUserAndPwd(@PathVariable("email") String email) {
        return userService.findUserAndPwd(email);
    }

//    @ApiOperation(value = "获取所有注册用户的接口")
//    @GetMapping("/getAllRegisters")
//    public List<Register> getAllRegisters() {
//        return registerService.getAllRegisters();
//    }


}
