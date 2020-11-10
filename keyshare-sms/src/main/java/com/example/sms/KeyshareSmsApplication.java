package com.example.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.example.sms.service.SmsSenderService;

@SpringBootApplication(scanBasePackages={"com.example"}, exclude= {RedisRepositoriesAutoConfiguration.class})
@EnableJpaAuditing
@EnableRedisHttpSession
public class KeyshareSmsApplication{
	@Autowired
	SmsSenderService service;
	public static void main(String[] args) {
		SpringApplication.run(KeyshareSmsApplication.class, args);
	}
	/*
	@Override
	public void run(String... args) throws Exception {
		SmsRequest req = new SmsRequest();
		req.setDryRun(true);
		req.setPayload("testing.... 안녕하세요");
		req.setReceiver("01025132363");
		service.sendMessage(req);
		
	}*/
}
