package com.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 *
 * @SpringBootApplication combines three annotations:
 * - @Configuration: marks this class as a source of bean definitions
 * - @EnableAutoConfiguration: tells Spring Boot to auto-configure based on dependencies
 * - @ComponentScan: tells Spring to scan this package for components (controllers, services, etc.)
 */
@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        // This launches the embedded Tomcat server and starts the application
        SpringApplication.run(BankingApplication.class, args);
    }
}
