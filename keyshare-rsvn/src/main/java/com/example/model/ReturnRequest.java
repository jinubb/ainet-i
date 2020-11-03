package com.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReturnRequest {
	private Long keyshareId;
	private String returnRemark; // 특이 사항
}
