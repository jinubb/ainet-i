package com.example.common;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSession implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String userType;

	private Long id;
	
	private String name;
	
	private String email;

	private String phone;
}
