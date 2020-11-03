package com.example.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "user")
public class User {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; //유저 번호
	
	@Column(name="TYPE")
	private String type; // 유저 타입 , OW : 차량오너   SH : 공유 사용자   SE : 서비스 담당자 

	@Column(name="NAME")
	private String name; // 유저 이름
	
	@Column(name="PHONE")
	private String phone; // 유저 전화번호
	
	@Column(name="EMAIL")
	private String email; // 유저 이메일
	
	@Column(name="PROFILE_IMAGE")
	private String profileImage; // 프로필 사진 URL
	
	@Column(name="THUMBNAIL_IMAGE")
	private String thumbnailImage; // 썸네일 사진 URL
	
	@Column(name="AGE_RANGE")
	private String ageRange; // 연령대
	
	@Column(name="BIRTHDAY")
	private String birthday; // 생일
	
	@Column(name="BIRTHYEAR")
	private String birthyear; // 생년
	
	@Column(name="GENDER")
	private String gender; // 성별
	
	@Column(name="CI")
	private String ci; // 연계정보
	
}
