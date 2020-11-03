package com.example.model;

import java.util.Map;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PushNotificationRequest {
    private String title;
    private String message;
    private String topic;
    private String token;
    private Map<String, String> customData;
    public PushNotificationRequest() { }

    public PushNotificationRequest(String title, String messageBody, String topicName) {
        new PushNotificationRequest(title, messageBody);
        this.topic = topicName;
    }
    
    public PushNotificationRequest(String title, String messageBody) {
      this.title = title;
      this.message = messageBody;
    }
    
    public void emptyToken() {
    	token = null;
    }
}
