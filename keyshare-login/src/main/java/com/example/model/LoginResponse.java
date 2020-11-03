package com.example.model;

import com.example.entity.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {
	private String authToken;
	private User loginUser;
}
