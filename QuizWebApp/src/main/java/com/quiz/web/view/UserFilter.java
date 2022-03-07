package com.quiz.web.view;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quiz.web.util.LogUtil;
import com.quiz.web.util.WebUtil;

@WebFilter(urlPatterns = "/*")
public class UserFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest r1, ServletResponse r2, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) r1;
        HttpServletResponse resp = (HttpServletResponse) r2;
        String uri = req.getRequestURI();
        LogUtil.log("UserFilter", uri);
        if (uri.endsWith("/sign-in.jsp") || uri.endsWith("/sign-up.jsp") ||  
            uri.endsWith("/favicon.ico") || uri.endsWith("/user")) {
            chain.doFilter(r1, r2);
        } else if (req.getHeader(WebUtil.REQ_ID) != null){
            chain.doFilter(r1, r2);
        } else if (req.getAttribute(WebUtil.REQ_ID) != null){
            chain.doFilter(r1, r2);
        } else {
            resp.sendRedirect("sign-in.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}