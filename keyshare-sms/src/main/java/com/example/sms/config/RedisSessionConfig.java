package com.example.sms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * 
 * @author david
 * Cookie 대신 x-auth-token 헤더를 사용하고자 하는 경우 주석부분 해제,
 * Cookie 기반 세션 관리를 하고자 하는 경우 httpSessionIdResolver메소드 주석 처리. 
 *
 */
@Configuration
public class RedisSessionConfig {
	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	
	@Bean
	public LettuceConnectionFactory connectionFactory() {
	
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
		return factory; 
	}


	@Bean
	public HttpSessionIdResolver httpSessionIdResolver() {
		return HeaderHttpSessionIdResolver.xAuthToken(); 
	}
}

