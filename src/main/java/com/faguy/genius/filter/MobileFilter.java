package com.faguy.genius.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.faguy.genius.model.DTO.UserDTO;


@Transactional
public class MobileFilter implements Filter {

    @Override
    public void doFilter(
      ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
 
        System.out.println("Request URI of mobile  " + req.getRequestURI());
        System.out.println("Response Status Code of Mobile  is: " + res.getStatus());
        HttpSession session=req.getSession();
        Object u=session.getAttribute("user");
        if(u==null) {	
        	System.out.println(" offfffffffffffffff " + res.getStatus());
        	req.getServletContext().getRequestDispatcher("/").forward(req, res);	
        }else {
        	System.err.println("Je suis connecté ");
        }
        chain.doFilter(request, response);

        

    }
    


    // other methods 
    @Bean
    public FilterRegistrationBean<MobileFilter> filter()
    {
        FilterRegistrationBean<MobileFilter> bean = new FilterRegistrationBean<>();
 
        bean.setFilter(new MobileFilter());
        bean.addUrlPatterns("/");  // or, use `setUrlPatterns()`
 
        return bean;
    }  
}
