package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@SpringBootApplication(scanBasePackages={"com.example"}, exclude= {RedisRepositoriesAutoConfiguration.class})
@EnableJpaAuditing
@EnableRedisHttpSession
public class KeyshareLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyshareLoginApplication.class, args);
	}

}
