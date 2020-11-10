package com.example.sms.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsResponse {
	@JsonProperty("result_code")
	private Integer resultCode;// when less than 0, message will contain the reason of failure
	private String message;
	@JsonProperty("msg_id")
	private String messageId;
	@JsonProperty("success_cnt")
	private Integer successCount;
	@JsonProperty("err_cnt")
	private Integer errorCount;
	@JsonProperty("msg_type")
	private String type;//message type: SMS|LMS|MMS
}
