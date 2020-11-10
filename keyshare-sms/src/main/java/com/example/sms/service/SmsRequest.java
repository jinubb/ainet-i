package com.example.sms.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsRequest {
	private String title;
	private String payload;
	private String receiver;
	private String type = MessageType.SMS.toString();
	boolean dryRun;
	
	public void normalizeMessage() {
		switch(MessageType.valueOf(type)) {
		case SMS:
			if(payload.length() > 90) payload = payload.substring(0, 87) + "..";
			if(title != null) title = null;//title is for LMS and MMS
			break;
		case LMS:
		case MMS:
			if(title != null && title.length() > 44) title = title.substring(0, 41) + "..";
			break;
		}
	}
}
