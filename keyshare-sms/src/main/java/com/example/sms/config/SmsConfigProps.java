package com.example.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Configuration
@ConfigurationProperties(prefix="sms")
@Getter
@Setter
@ToString
public class SmsConfigProps {
	private String apiKey;
	private String senderNo;
	private String apiUserId;
	private String serviceUrl;
}
