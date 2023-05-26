package com.springboot.cric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Application
{
	@Value("${cors.allowed-origins}")
	private String corsAllowedOrigins;

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					.addMapping("/**")
					.allowedOrigins(corsAllowedOrigins.split(", "))
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
					.allowedHeaders("Accept", "Origin", "X-Requested-With", "Content-Type", "Referer", "User-Agent", "Access-Control-Allow-Origin")
					.maxAge(3 * 24 * 3600);
			}
		};
	}
}
