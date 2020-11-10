package com.example.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.ResponseContainer;
import com.example.sms.service.SmsRequest;
import com.example.sms.service.SmsResponse;
import com.example.sms.service.SmsSenderService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/sms/api")
@Slf4j
public class SmsController {
	@Autowired
	private SmsSenderService smsService;
	
	
	@PostMapping("/send")
	public ResponseContainer<SmsResponse> sendSmsCommand(@RequestBody SmsRequest smsReq) {
		ResponseContainer<SmsResponse> response = ResponseContainer.emptyResponse();
		try {
			SmsResponse smsRes = smsService.sendSmsMessage(smsReq);
			response.setPayload(smsRes);
		} catch(Exception e) {
			log.error("Sms send failed request {}\nerror: {}", smsReq, e);
		}
		return response;
	}
	
}
