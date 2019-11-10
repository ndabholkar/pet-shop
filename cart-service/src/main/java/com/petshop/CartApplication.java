package com.petshop;

import com.petshop.config.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class CartApplication {

	public static void main(String[] args) {

		SpringApplication.run(CartApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean jwtFilter() {

		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/cart/*");

		return registrationBean;
	}
}
