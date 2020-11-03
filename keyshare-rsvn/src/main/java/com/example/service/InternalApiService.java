package com.example.service;

import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.common.Error.InternalService;
import com.example.common.ResponseContainer;
import com.example.model.PushNotificationRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InternalApiService {

	@Autowired
	private RestApiUrl apiUrls;

	private RestTemplate template = new RestTemplate();

	@Autowired
	private HttpSession session;

	@Value("${user-auth.login}")
	private String aicarLoginUrl;
	
	@Value("${demonUserId}")
	private String aicarLoginId;
	
	@Value("${demonUserPwd}")
	private String aicarLoginPwd;
	
	@Value("${keyshare-login-url}")
	private String loginUrl;
	
	private ParameterizedTypeReference<ResponseContainer<Void>> voidTypeRef = new ParameterizedTypeReference<ResponseContainer<Void>>() {};
	private ParameterizedTypeReference<ResponseContainer<Map<String, String>>> stringMapTypeRef = new ParameterizedTypeReference<ResponseContainer<Map<String, String>>>() {};
	private ParameterizedTypeReference<ResponseContainer<List<String>>> stringListTypeRef = new ParameterizedTypeReference<ResponseContainer<List<String>>>() {};

	
	
	public void sendNotificationToToken(PushNotificationRequest notiReq) {
		HttpEntity<PushNotificationRequest> request = new HttpEntity<>(notiReq, createDefaultHeaders());
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrls.getPushNotification().getSendNotificationToToken());
		ResponseEntity<ResponseContainer<Void>> responseEntity = template.exchange(builder.toUriString(), HttpMethod.POST, request, voidTypeRef);
		
		ResponseContainer<Void> response = responseEntity.getBody();
		if (!response.isSuccess()) log.error("서비스 호출 실패: {}", response);
		//else log.info("서비스 호출 응답결과: {}", response);
	}
	
	<T> T extractEntityBody(ResponseEntity<ResponseContainer<T>> responseEntity) {
		ResponseContainer<T> response = responseEntity.getBody();
		if (!response.isSuccess()) {
			throw new RuntimeException(String.format("%s Service Call failed: %s", InternalService.serviceName(response.getDetail()), response.getMessage()));
			//throw new RuntimeException(String.format("%s service call failed: %s", InternalService.from(response.getDetail()).getName(), response.getMessage()));
		}
		return response.getPayload();
	}
	
	private HttpHeaders createDefaultHeaders() {
//		HttpHeaders headers = new HttpHeaders();
//		String xAuthToken = session.getId();
//		
//		headers.add("X-AUTH-TOKEN", xAuthToken);
//		return headers;
		return createDefaultAuthHeaders(session.getId());
	}

	public HttpHeaders createDefaultAuthHeaders(String xAuthToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", xAuthToken);
		return headers;
	}
	
	public String getTokenByDemon() {
		String uri = String.format("%s?loginId=%s&password=%s", aicarLoginUrl, aicarLoginId, aicarLoginPwd);
		ResponseEntity<ResponseContainer<Map<String, String>>> responseEntity = template.exchange(uri, HttpMethod.GET,
				null, stringMapTypeRef);
		return extractEntityBody(responseEntity).get("authToken");
	}

	public List<String> getFcmToken(Long ownerUserId) {
		String uri = String.format("%s/fcm/%s", loginUrl, ownerUserId);
		ResponseEntity<ResponseContainer<List<String>>> responseEntity = template.exchange(uri, HttpMethod.GET,
				null, stringListTypeRef);
		return extractEntityBody(responseEntity);
	}
}