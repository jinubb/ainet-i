package com.example.sms.service;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.common.UserSession;
import com.example.sms.config.SmsConfigProps;
import com.example.sms.entity.SmsLog;
import com.example.sms.repository.SmsLogRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsSenderService {
	@Autowired
	private SmsConfigProps configProps;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpSession session;
//	@Value("${msg.send-timeout-secs}")
//	private int deviceMsgSendTimeoutSecs;
	@Autowired
	private SmsLogRepository smsLogRepo;
	public SmsResponse sendSmsMessage(SmsRequest req) {
		SmsResponse smsRes = sendMessage(req);
		SmsLog smsLog = new SmsLog();
		smsLog.setCreatedDate(new Date());
		smsLog.setErrCnt(String.valueOf(smsRes.getErrorCount()));
		smsLog.setMessage(req.getPayload());
		smsLog.setTitle(req.getTitle());
		
		smsLog.setMessageType(smsRes.getType());
		smsLog.setResultCode(String.valueOf(smsRes.getResultCode()));
		smsLog.setMessageId(smsRes.getMessageId());
		smsLog.setRecepientNo(req.getReceiver());
		
		try {
			UserSession user = getUserSession();
			if(user != null) smsLog.setUserEmail(user.getEmail());
		} catch(Exception e) {
			smsLog.setUserEmail("user session unavailable");
		}
		
		smsLogRepo.save(smsLog);
		return smsRes;
	}
	public SmsResponse sendMessage(SmsRequest req) {
		req.normalizeMessage();
		log.info("normalized sms request: {}", req);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("key", configProps.getApiKey());
		map.add("user_id", configProps.getApiUserId());
		map.add("sender", configProps.getSenderNo());
		map.add("receiver", req.getReceiver());
		
		switch(MessageType.valueOf(req.getType())) {
		case LMS:
		case MMS:
			String title = req.getTitle();
			if(title != null) {
				map.add("title", title);
			}
			break;
		case SMS:
			break;
			
		}
		map.add("msg", req.getPayload());
		map.add("msg_type", req.getType().toString());
		map.add("testmode_yn", req.isDryRun()? "Y": "N");
		HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(map, headers);
		ResponseEntity<SmsResponse> response = restTemplate.postForEntity(configProps.getServiceUrl(), httpRequest, SmsResponse.class);
		SmsResponse smsRes = response.getBody();
		
		return smsRes;

	}
	/*
	public void sendDeviceCommandMessage(EventMessage<String> message) {
		long elapsed = (System.currentTimeMillis() - message.getTimestamp())/10000;
		SmsRequest req = new SmsRequest();
		log.info("command request elapsed time in sec {}", elapsed);
		if(elapsed > deviceMsgSendTimeoutSecs) {
			log.warn("Device Command SMS request became stale elapsedTimeSec:{} limitSecs: {}", elapsed, deviceMsgSendTimeoutSecs);
			req.setDryRun(true);
		}
		

		req.setReceiver(message.getTransactionId());//트랜잭션 아이디에 디바이스 sim 전화번호를 전송했음. 
		req.setPayload(message.getPayload());
		req.setDryRun(false);
		//req.setType("SMS");//default		
		SmsResponse smsRes = sendMessage(req);//not from apiController, but from messagebroker meaning that this is a request for device command
		
		SmsLog smsLog = new SmsLog();
		smsLog.setRecepientNo(req.getReceiver());
		smsLog.setCreatedDate(new Date());
		smsLog.setErrCnt(String.valueOf(smsRes.getErrorCount()));
		smsLog.setMessage(message.getPayload());
		smsLog.setMessageType(smsRes.getType());
		smsLog.setResultCode(String.valueOf(smsRes.getResultCode()));
		smsLog.setSourceId(message.getSourceId());//cmd_log 의 primary key
		smsLog.setMessageId(smsRes.getMessageId());
		//smsLog.setUserEmail(null);
		smsLogRepo.save(smsLog);
	}
	*/
	@Bean
	public RestTemplate restTemplate() {
	    RestTemplate restTemplate = new RestTemplate();
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
	    restTemplate.getMessageConverters().add(converter);
	    return restTemplate;
	}

	private UserSession getUserSession() {
		return (UserSession)session.getAttribute("loginInfo");
	}
	
}
