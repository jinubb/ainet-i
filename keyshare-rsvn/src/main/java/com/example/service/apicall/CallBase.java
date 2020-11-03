package com.example.service.apicall;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class CallBase {
	private String host;
	private Integer port;
	private String baseUri;
	public String uriBase() {
		return String.format("http://%s:%s/%s", host, port, baseUri);
	}
	
	
}
