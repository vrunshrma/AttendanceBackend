package com.cedrus.attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "POST", "PUT","PATCH", "DELETE").allowedHeaders("*")
						.allowedOrigins("http://localhost:3000","http://192.168.1.50:3000",
								"https://admin-portal-git-main-vs653525-8662s-projects.vercel.app");
			}
		};
	}
}