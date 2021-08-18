package com.songran.helper;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class AdminFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpSession session = servletRequest.getSession();
		
		try {				
			Long uId = (Long) session.getAttribute("adminId");
			if (uId <= 0) {
				servletResponse.sendRedirect("/login");
			}
			chain.doFilter(request, response);

		} catch (Exception e) {
			session.setAttribute("message", new MessageManager(e.getMessage(), "alert-danger"));
			servletResponse.sendRedirect("/login");
		}
	}

	@Bean
	public FilterRegistrationBean<AdminFilter> loggingFilter() {
		FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new AdminFilter());
		registrationBean.addUrlPatterns("/admin/*");

		return registrationBean;

	}

}
