//package com.example.hlsiidb;
//
//import com.example.hlsiidb.controller.IPInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @author ScXin
// * @date 7/14/2020 12:25 PM
// */
//@Configuration
//public class InterceptorConfig  implements WebMvcConfigurer {
//
//
//    @Bean
//    IPInterceptor ipInterceptor(){
//        return new IPInterceptor();
//    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(ipInterceptor());
//
//    }
//
//}