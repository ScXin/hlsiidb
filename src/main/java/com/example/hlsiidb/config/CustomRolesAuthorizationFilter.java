package com.example.hlsiidb.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 自定义角色filter：只要有对应角色就可以访问，而不需要满足所有角色
 */
public class CustomRolesAuthorizationFilter extends AuthorizationFilter {
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = this.getSubject(request, response);

        //获取当前访问路径所需要的集合
        String[] rolesArray = (String[])((String[])mappedValue);

        if (rolesArray != null && rolesArray.length != 0) {
            Set<String> roles = CollectionUtils.asSet(rolesArray);
            //当前subject是roles中的任意一个则有权限访问
            for (String role : roles){
                if(subject.hasRole(role)){
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }
}