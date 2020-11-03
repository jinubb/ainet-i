package com.example.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {
	private String name;
	private String email;
	private String phone;
	private String profileImage; // 프로필 사진 URL
	private String thumbnailImage; // 썸네일 사진 URL
	private String ageRange; // 연령대
	private String birthday; // 생일
	private String birthyear; // 생년
	private String gender; // 성별
	private String ci; // 연계정보
}

