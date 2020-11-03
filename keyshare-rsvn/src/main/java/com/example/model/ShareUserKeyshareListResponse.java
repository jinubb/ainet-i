package com.example.model;

import com.example.entity.Keyshare;
import com.example.entity.Vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShareUserKeyshareListResponse {
	private Keyshare keyshare;
	private Vehicle vehicle;
}
