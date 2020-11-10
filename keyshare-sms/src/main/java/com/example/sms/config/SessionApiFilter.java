package com.example.sms.config;


import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.common.ResponseContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class SessionApiFilter implements Filter {
	static final String AUTH_TOKEN_HEADER = "X-Auth-Token";
	private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;
		log.info("request method {}",httpReq.getMethod());

		if(procCrossOriginPreflight(httpReq, httpRes)) return;

		String authToken = getAuthToken(httpReq);
		HttpSession session = httpReq.getSession(false);
		if(session == null) {
			log.info("no session bound to {}", authToken);
			sendUnauthorizedErrorResponse(httpRes);
		} else {
			try {
				chain.doFilter(request, response);
		    } catch (Exception e) {
		    	e.printStackTrace();
		        httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Api Request");
		    }
			
		}
	}
	private String getAuthToken(HttpServletRequest req) {
		/*
		Enumeration<String> names = req.getHeaderNames();
		String val;
		while(names.hasMoreElements()) {
			val = names.nextElement();
			log.info("{} : {}" ,val, req.getHeader(val)); 
		}
		*/
		Enumeration<String> authTokens = req.getHeaders(AUTH_TOKEN_HEADER);
		while(authTokens.hasMoreElements()) {
			return authTokens.nextElement();
		}
		return null;
	}
	private void sendUnauthorizedErrorResponse(ServletResponse response) throws JsonProcessingException, IOException {
		ResponseContainer<Void> errorResponse = ResponseContainer.emptyResponse();
		Map<String, Object> reqProps = new HashMap<>();
		reqProps.put("message", "Unauthorized Api Request made");
		reqProps.put("trace", "No valid auth token!!");
		errorResponse.setHttpError(HttpServletResponse.SC_UNAUTHORIZED, reqProps);
		HttpServletResponse httpRes = (HttpServletResponse) response;
		httpRes.setHeader("Access-Control-Allow-Origin", "*");
		httpRes.setContentType("application/json");
		httpRes.getWriter().write(getSerializedJsonBytes(errorResponse));
		httpRes.getWriter().flush();
	}
	private String getSerializedJsonBytes(ResponseContainer<Void> errResponse) throws JsonProcessingException {
		return objectMapper.writeValueAsString(errResponse);
	}
	private boolean procCrossOriginPreflight(HttpServletRequest req, HttpServletResponse res) throws IOException {
		if(req.getMethod().contentEquals("OPTIONS")) {
			/*String.format("%s://%s:%s", req.getScheme(), req.getServerName(), req.getServerPort())*/
			res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
			res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
			res.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
			res.getWriter().flush();
			log.info("sent pre-flight reponse");
			return true;
		}
		return false;
	}
}
