package com.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig — configures Cross-Origin Resource Sharing (CORS).
 *
 * By default, browsers block requests from one origin (e.g., http://localhost:5173)
 * to a different origin (e.g., http://localhost:8080) for security reasons.
 * This is called the "same-origin policy."
 *
 * CORS configuration tells the browser which cross-origin requests are allowed.
 * Without this, our React app cannot call the Spring Boot API.
 *
 * @Configuration tells Spring this class contains bean definitions.
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")             // Apply CORS to all /api/* endpoints
                        .allowedOrigins(
                            "http://localhost:5173",       // Vite dev server (default port)
                            "http://localhost:3000"        // Common React dev port
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");              // Allow all headers
            }
        };
    }
}
