package com.example.sms.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class URLFilterRegistrar {
	@Bean
	public FilterRegistrationBean<SessionApiFilter> apiSessionFilter(){
	    FilterRegistrationBean<SessionApiFilter> filterRegBean = new FilterRegistrationBean<>();
	         
	    filterRegBean.setFilter(new SessionApiFilter());
	    filterRegBean.addUrlPatterns("/sms/api/*");
	         
	    return filterRegBean;    
	}
}

