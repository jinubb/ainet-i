package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.ResponseContainer;
import com.example.model.LoginRequest;
import com.example.model.LoginResponse;
import com.example.model.OtpRequest;
import com.example.model.ShareLoginRequest;
import com.example.model.UuidRequest;
import com.example.service.LoginService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/login")
public class LoginController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginService service;
	
	@ApiOperation(value = "device UUID 확인")
	@PostMapping("/uuid")
	public ResponseContainer<LoginResponse> checkUuid(@RequestBody UuidRequest req) {
		ResponseContainer<LoginResponse> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.checkUuid(req));
		} catch(Exception e) {
			logger.error("uuid:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "휴대전화 인증번호 sms 전송")
	@PostMapping("/user/sms")
	public ResponseContainer<Void> sendSmsOtp(@RequestBody OtpRequest req) {
		ResponseContainer<Void> response = ResponseContainer.emptyResponse();		
		try {
			service.sendSmsOtp(req);
			response.setSuccess(true);;
		} catch(Exception e) {
			logger.error("login:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "sms 인증번호 로그인")
	@PostMapping("/user")
	public ResponseContainer<LoginResponse> login(@RequestBody LoginRequest req) {
		ResponseContainer<LoginResponse> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.loginUser(req));
		} catch(Exception e) {
			logger.error("login:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "공유 사용자 로그인")
	@PostMapping("/shareuser")
	public ResponseContainer<LoginResponse> shareLogin(@RequestBody ShareLoginRequest req) {
		ResponseContainer<LoginResponse> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.shareLogin(req));
		} catch(Exception e) {
			logger.error("login:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	/*
	@ApiOperation(value = "로그아웃(토큰삭제)")
	@GetMapping("/logout")
	public ResponseContainer<Void> logoutSession(){
		ResponseContainer<Void> response = ResponseContainer.emptyResponse();
		try {
			service.logoutSession();
			response.setSuccess(true);
		} catch(Exception e) {
			logger.error("logout:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	*/
	
	@ApiOperation(value = "device FCM 토큰")
	@GetMapping("/fcm/{userId}")
	public ResponseContainer<List<String>> getFcmToken(@PathVariable("userId")Long userId) {
		ResponseContainer<List<String>> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.getFcmToken(userId));
		} catch(Exception e) {
			logger.error("login:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
}
