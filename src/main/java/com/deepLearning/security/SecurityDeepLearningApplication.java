package com.deepLearning.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SecurityDeepLearningApplication is the entry point for the Spring Boot application.
 * <p>
 * This class is annotated with {@code @SpringBootApplication}, which enables auto-configuration,
 * component scanning, and configuration for the application.
 * <p>
 * The {@code main} method uses {@link SpringApplication#run(Class, String...)} to launch the application.
 * <p>
 * <b>Usage Example:</b>
 * <pre>
 * public static void main(String[] args) {
 *     SpringApplication.run(SecurityDeepLearningApplication.class, args);
 * }
 * </pre>
 *
 * In a production system, you may add initialization logic or custom startup routines by implementing
 * {@link org.springframework.boot.CommandLineRunner} or {@link org.springframework.boot.ApplicationRunner}.
 */
@SpringBootApplication
public class SecurityDeepLearningApplication {

	/**
	 * The main method, used to launch the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecurityDeepLearningApplication.class, args);
	}
}