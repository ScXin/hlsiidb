package com.example.hlsiidb.config;

//import com.example.hlsiidb.entity.Permission;
//import com.example.hlsiidb.entity.Role;
//import com.example.hlsiidb.entity.User;

import com.commonuser.entity.Permission;
import com.commonuser.entity.Role;
import com.commonuser.entity.User;
import com.example.hlsiidb.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 角色和权限验证时调用
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        System.out.println("授权方法doGetAuthorizationInfo");
        User newUser = (User) principalCollection.getPrimaryPrincipal();
        User user = userService.findAllUserInfoByUsername(newUser.getUsername());
        if (user == null) {
            return null;
        }
//        List<Role> roleList = user.getRoleList();
        Role role = user.getRole();
        List<String> stringRoleList = new ArrayList<>();
        List<String> stringPermissionList = new ArrayList<>();
        stringRoleList.add(role.getName());
        List<Permission> permissionList = role.getPermissionList();
//        for (Permission permission : permissionList) {
//                if (permission != null) {
//                    stringPermissionList.add(permission.getName());
//                }
//            }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(stringRoleList);
        simpleAuthorizationInfo.addStringPermissions(stringPermissionList);

        return simpleAuthorizationInfo;
    }

    /**
     * 用户登陆时认证调用
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        System.out.println("认证方法doGetAuthenticationInfo");

        //从token中获取用户名，token代表用户输入的信息


        String username = (String) authenticationToken.getPrincipal();

        //获取salt
        User user = userService.findAllUserInfoByUsername(username);
        if (user == null) {
            return null;
        }
        //取密码
        String pwd = user.getPassword();

        ByteSource salt = ByteSource.Util.bytes(username);

        if (pwd == null || "".equals(pwd)) {
            return null;
        }

        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, this.getClass().getName());
    }
}
