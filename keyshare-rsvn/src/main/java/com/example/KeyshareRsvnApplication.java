package com.example;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication(scanBasePackages={"com.example"})
@EnableJpaAuditing
@EnableRedisHttpSession
public class KeyshareRsvnApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyshareRsvnApplication.class, args);
	}

}
