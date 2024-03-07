package com.jwtstudy.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");
//        PrintWriter out = response.getWriter();
//        out.println("안녕"); // 이렇게 되면 필터가 끊기기에 아래에와 같이 넘겨줘야한다.

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(req.getMethod().equals("POST")){
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            System.out.println("필터1");
                chain.doFilter(request , response); // 적어주어야 다음 필터로 넘어감

        }
        else{
            PrintWriter out = res.getWriter();
            out.println("인증안됨");
        }
        System.out.println("필터1");

    }
}
