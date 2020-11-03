package com.example.common;

import java.sql.SQLException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) //null인필드 삭제
public class ResponseContainer<T> {
	private T payload;
	private boolean success;
	private String message;
	private ErrDetail detail;
	
	public static<P> ResponseContainer<P> emptyResponse() {
		return new ResponseContainer<P>();
	}
	
	public void setPayload(T payload) {
		this.payload = payload;
		this.success = true;
	}
	
	public void setError(Throwable t) {
		this.success = false;
		this.detail = new ErrDetail();
		this.message = t.getMessage();
		Throwable cause;
		
		cause = t.getCause();
		if(cause != null) {
			while(true) {
				if(cause.getCause()!= null) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
			if(cause instanceof SQLException) {
				detail.code = ((SQLException)cause).getErrorCode();
			}
			detail.message = cause.getMessage();
		}
	}
	
	@Getter
	@Setter
	@ToString
	public class ErrDetail {
		private int code;
		private String message;
	}
	
	public void setHttpError(int code, Map<String, Object> reqProps) {
		this.success = false;
		this.setMessage((String)reqProps.get("message"));
		this.detail = new ErrDetail();
		detail.setCode(code);
		detail.message = String.valueOf(reqProps.get("trace"));
	}
	
	
}
