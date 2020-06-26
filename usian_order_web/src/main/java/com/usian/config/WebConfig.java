package com.usian.config;

import com.usian.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserLoginInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration registration = registry.addInterceptor(interceptor);
        //拦截指定URL
        registration.addPathPatterns("/frontend/order/**");
    }
}
