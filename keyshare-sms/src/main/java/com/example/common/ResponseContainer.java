package com.example.common;


import java.sql.SQLException;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseContainer<T> {
	private T payload;
	private boolean success;
	private String message;
	private ErrDetail detail;
	private PageInfo pageInfo;
	public static<P> ResponseContainer<P> emptyResponse() {
		return new ResponseContainer<P>();
	}
	public void setPayload(T payload) {
		this.payload = payload;
		this.success = true;
	}
	public void setPage(Page<?> page) {
		pageInfo = new PageInfo();
		pageInfo.pageNo = page.getNumber();
		pageInfo.totalPages = page.getTotalPages();
		pageInfo.totalCount = page.getTotalElements();
		pageInfo.size = page.getSize();
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
	public void setHttpError(int code, Map<String, Object> reqProps) {
		this.success = false;
		this.setMessage((String)reqProps.get("message"));
		this.detail = new ErrDetail();
		detail.setCode(code);
		detail.message = String.valueOf(reqProps.get("trace"));

	}
	@Getter
	@Setter
	@ToString
	public class PageInfo {
		private Long totalCount;
		private int totalPages;
		private int pageNo;
		private Integer size;
	}
	@Getter
	@Setter
	@ToString
	public class ErrDetail {
		private int code;
		private String message;
	}
}
