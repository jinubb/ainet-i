package com.example.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;

import com.example.service.apicall.PushNotification;
import com.example.service.apicall.SmsService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix="prada.api.internal.service")
@Getter
@Setter
@ToString

/*
prada.api.internal.service.sys-co.host=172.31.25.13
prada.api.internal.service.sys-co.port=9084
prada.api.internal.service.sys-co.api-uri.group-list=/sys/co/api/code/group/list

prada.api.internal.service.dept-user.host=172.31.25.13
prada.api.internal.service.dept-user.port=9086

prada.api.internal.service.sms-service.host=172.31.24.249
prada.api.internal.service.sms-service.port=8080

prada.api.internal.service.device-cmd.host=172.31.25.13
prada.api.internal.service.device-cmd.port=9081

prada.api.internal.service.pkng-mgmt.host=172.31.25.13
prada.api.internal.service.pkng-mgmt.port=9088

prada.api.internal.service.ccard-mgmt.host=172.31.25.13
prada.api.internal.service.ccard-mgmt.port=8101

prada.api.internal.service.vhc-mgmt.host=172.31.25.13
prada.api.internal.service.vhc-mgmt.port=9087
*/

public class RestApiUrl {
	private SmsService smsService;
	private PushNotification pushNotification;
}