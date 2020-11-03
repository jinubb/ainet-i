package com.example.service.apicall;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class PushNotification extends CallBase{
	private String sendNotificationToToken;
	public String getSendNotificationToToken() {
		return String.format("%s/%s", uriBase(), sendNotificationToToken);
	}
}
