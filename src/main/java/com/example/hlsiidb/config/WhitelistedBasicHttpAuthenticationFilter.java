package com.example.hlsiidb.config;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ScXin
 * @date 7/13/2020 1:34 PM
 */
public class WhitelistedBasicHttpAuthenticationFilter extends BasicHttpAuthenticationFilter {


    private Set<String> whitelist = Collections.emptySet();

    public void setWhitelist(String list) {
        whitelist = new HashSet<String>();
        Collections.addAll(whitelist, list.split(",")); //make sure there are no spaces in the string!!!!
    }
    @Override
    protected boolean isEnabled (ServletRequest request, ServletResponse response) throws ServletException, IOException
    {

        System.out.println("hashshhshhshshshshshshshshshshshshshshshshshshshshshshshshshshsh");
        if (whitelist.contains(request.getRemoteAddr())) {
            return false;
        }
        return super.isEnabled(request, response);
    }
}
