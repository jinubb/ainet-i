package com.example.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {
	private String uuid;
	private String otp;
	private String fcmToken;
}
