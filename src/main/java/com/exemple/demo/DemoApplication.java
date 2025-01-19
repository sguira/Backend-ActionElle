package com.exemple.demo;

import org.apache.catalina.filters.RateLimitFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
// @CrossOrigin("*")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// méthode de type bean qui permet d'ajouter le filtre rate limiting
	@Bean
	public FilterRegistrationBean<RateLimitFilter> rateLimitingFilter() {
		FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new RateLimitFilter());
		registrationBean.addUrlPatterns("/api/v1/**");

		return registrationBean;
	}

}
