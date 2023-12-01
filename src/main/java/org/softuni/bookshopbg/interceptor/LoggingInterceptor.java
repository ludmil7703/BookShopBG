package org.softuni.bookshopbg.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);
    private String IP_ADDRESS ;

    private  UserService userService;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){


        String ipAddress = request.getHeader("X-Forward-For");

        if(ipAddress== null){

            ipAddress = request.getRemoteAddr();
        }

        System.out.println(ipAddress);
        this.IP_ADDRESS = ipAddress;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model){
        LOG.info("Handler execution is complete");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception){
        LOG.info("Request is complete");
    }

    public String getIpAddress() {
        return IP_ADDRESS;
    }
}
