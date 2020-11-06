package com.example.model;


import com.example.entity.Keyshare;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KeyshareListResponse {
	private Keyshare listKeyshare;
	private User user;
}
