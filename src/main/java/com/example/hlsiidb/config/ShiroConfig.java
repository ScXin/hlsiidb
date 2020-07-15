package com.example.hlsiidb.config;


import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.hlsiidb.util.Config.REDIS_PATH;

@Configuration
public class ShiroConfig {

    /**
     * 拦截器
     * 【【根据controller逻辑进行修改】】
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //必须设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //访问需要登录的接口，却没有登陆，调用此接口
        shiroFilterFactoryBean.setLoginUrl("/hlsii/pub/needLogin");

        //没有权限，未授权会调用此方法，先验证登陆再验证权限
        shiroFilterFactoryBean.setUnauthorizedUrl("/hlsii/pub/notPermit");

        //设置自定义filter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
//        filterMap.put("authcBasic", new WhitelistedBasicHttpAuthenticationFilter());
        filterMap.put("roleOrFilter", new CustomRolesAuthorizationFilter());

//        filterMap.put("authc", new ShiroFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);


        //拦截器路径。【坑】部分路径无法拦截，时有时无：因为使用了HashMap,要改成linkedHashMap
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //swagger2测试时需要开放
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/configuration/security", "anon");
        filterChainDefinitionMap.put("/configuration/ui", "anon");

        //退出过滤器
//        filterChainDefinitionMap.put("/hlsii/pub/logout", "logout");

        //游客模式
        filterChainDefinitionMap.put("/hlsii/pub/**", "anon");
        filterChainDefinitionMap.put("/beam/status", "anon");

        //登陆用户才可以访问
//        filterChainDefinitionMap.put("/authc/**", "authc");

        //管理员角色才可以访问
        filterChainDefinitionMap.put("/hlsii/admin/**", "roleOrFilter[admin]");
        filterChainDefinitionMap.put("/hlsii/userLog/**", "roleOrFilter[admin]");
        filterChainDefinitionMap.put("/hlsii/user/**", "roleOrFilter[user],roleOrFilter[admin]");

        //有编辑权限才可以访问
//        filterChainDefinitionMap.put("/video/update", "perms[video_update]");


        //【坑2】：过滤链是顺序执行的，从上而下，所以/**要放到最后
        //authc : url定义必须通过认证才可以访问
        //anon :  url可以匿名访问
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }


    /**
     * 设置SecurityManager
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //设置realm（推荐放到最后，不然某些情况会不生效）
        securityManager.setRealm(customRealm());

        //如果不是前后端分离，则不必设置下面的sessionManager
        securityManager.setSessionManager(sessionManager());

        //使用自定义的cacheManager
        securityManager.setCacheManager(cacheManager());

        return securityManager;
    }


    /**
     * 自定义realm
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();

        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }


    /**
     * 采用散列函数设置数据库中密码加密方式，用于与输入密码进行匹配校验
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();

        //设置散列算法：这里使用的MD5算法
        credentialsMatcher.setHashAlgorithmName("md5");

        //散列次数，好比散列2次，相当于md5(md5(xxxx))
        credentialsMatcher.setHashIterations(2);

        credentialsMatcher.setHashSalted(true);

        return credentialsMatcher;
    }


    /**
     * 自定义会话管理器
     *
     * @return
     */
    @Bean
    public SessionManager sessionManager() {

        MySessionManager customSessionManager = new MySessionManager();

        //超时时间，默认 30分钟，会话超时；方法里面的单位是毫秒
        customSessionManager.setGlobalSessionTimeout(1800000);

        customSessionManager.setDeleteInvalidSessions(true);

        //将重写的RedisSessionDao 接入到shiro 中的sessionManager
        customSessionManager.setSessionDAO(redisSessionDAO());
        customSessionManager.setSessionValidationSchedulerEnabled(true);
        customSessionManager.setDeleteInvalidSessions(true);
        //customSessionManager.setSessionIdCookie();

        //配置session持久化
        customSessionManager.setSessionDAO(redisSessionDAO());
//
//        //设置sessionid名字生效
//        customSessionManager.setSessionIdCookie(getSessionIdCookie());

        return customSessionManager;
    }


    /**
     * 设置redisManager
     *
     * @return
     */
    public RedisManager getRedisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(REDIS_PATH);
        redisManager.setPort(6379);
        return redisManager;
    }

    /**
     * 可以修改session名字
     *
     * @return
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("nsrlSession");
        return simpleCookie;
    }

    /**
     * 配置具体cache实现类
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());

        //设置过期时间，单位是秒，20s
        redisCacheManager.setExpire(1800);

        return redisCacheManager;
    }


    /**
     * 自定义session持久化
     *
     * @return
     */
    @Bean
    public RedisSessionDao redisSessionDAO() {

        RedisSessionDao redisSessionDao = new RedisSessionDao();

        //设置sessionId生成器
//        redisSessionDAO.setSessionIdGenerator(new CustomSessionIdGenerator());

        return redisSessionDao;
    }


    /**
     * 管理shiro一些bean的生命周期 即bean初始化 与销毁
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * api controller 层面
     * 加入注解的使用，不加入这个AOP注解不生效(shiro的注解 例如 @RequiresGuest)
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 用来扫描上下文寻找所有的Advistor(通知器),
     * 将符合条件的Advisor应用到切入点的Bean中，需要在LifecycleBeanPostProcessor创建后才可以创建
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

}
