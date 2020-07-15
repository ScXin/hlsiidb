package com.example.hlsiidb.controller;

/**
 * @author ScXin
 * @date 7/14/2020 12:06 PM
 */

import com.example.hlsiidb.service.WhiteIPService;
import com.example.hlsiidb.util.IPUtils;
import com.example.hlsiidb.util.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author : HUHY    http://www.cnblogs.com/huhongy/
 * @Project_name:xxxxxxx
 * @date:2020/1/14 9:46
 * @email:hhy_0602@163.com
 * @description:{todo}
 */


public class IPInterceptor implements HandlerInterceptor {


    @Autowired
    WhiteIPService whiteIPService;

    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        System.out.println("hahahahhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahahhahahahhahahhahah");
        //先获取相关需要验证的ip列表
        //过滤ip,若用户在白名单内，则放行
        String ipAddress = IPUtils.getRealIP(request);
        //所用需要验证的ip,暂时批量验证
        List<String> listIps = whiteIPService.getAllWhiteIP();
        if (listIps == null || listIps.size() == 0 || !listIps.contains(ipAddress)) {
            response.getWriter().print("请检查ip白名单配置是否正确");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    /**
     * 吧ip拆分
     */


}