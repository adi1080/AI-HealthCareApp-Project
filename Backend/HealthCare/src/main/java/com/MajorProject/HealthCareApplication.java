package com.MajorProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan
@EnableScheduling
@EnableFeignClients
@EnableAsync
@EnableJpaRepositories
@SpringBootApplication
public class HealthCareApplication {
	public static void main(String[] args) {
		SpringApplication.run(HealthCareApplication.class, args);
	}
}
