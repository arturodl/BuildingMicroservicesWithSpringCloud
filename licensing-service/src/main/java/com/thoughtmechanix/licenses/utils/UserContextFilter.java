package com.thoughtmechanix.licenses.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println(">>>>>> Executing the Filter inside licensing service.");


        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        logger.debug(">>>>> I am entering the licensing service id with auth token: " , httpServletRequest.getHeader("Authorization"));

        UserContextHolder.getContext().setCorrelationId(  httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

        logger.debug(">>>>> License Service Incoming Correlation id inside licensing service: {}", UserContextHolder.getContext().getCorrelationId());

        //System.out.println(">>>>>>> The CorrelationId inside licensing service is: "+ httpServletRequest.getHeader(UserContext.CORRELATION_ID));
        //System.out.println(">>>>>>> The CorrelationId inside licensing service is [using threadlocal]: "+ UserContextHolder.getContext().getCorrelationId());
        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}