package com.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class User {
	private Long id; //유저 번호
	private String type; // 유저 타입 , OW : 차량오너   SH : 공유 사용자   SE : 서비스 담당자 	
	private String name; // 유저 이름
	private String phone; // 유저 전화번호
	private String email; // 유저 이메일
	private String profileImage; // 프로필 사진 URL
	private String thumbnailImage; // 썸네일 사진 URL
	private String ageRange; // 연령대
	private String birthday; // 생일
	private String birthyear; // 생년
	private String gender; // 성별
	private String ci; // 연계정보
}
