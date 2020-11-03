package com.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsRequest {
	public static final String SMS = "SMS";
	
	private boolean dryRun = false;
	private String payload;
	private String receiver;
	private String title;
	private String type;
	
	public void setReceiver(String receiver) {
		this.receiver = receiver.replaceAll("-", "");
	}
	public void setType(String type) {
		this.type = type.toUpperCase();
	}
	
	public SmsRequest(String message, String phoneNo, String smsType) {
		payload = message;
		if (phoneNo != null) setReceiver(phoneNo);
		setType(smsType);
	}
	public SmsRequest() {}
}
