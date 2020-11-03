package com.example.service.apicall;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class SmsService extends CallBase {
	private String sendSmsCommand;
	
	public String getSendSmsCommand() {
		return String.format("%s/%s", uriBase(), sendSmsCommand);
	}
}
