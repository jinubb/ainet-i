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
@Table(name = "vehicle")
public class Vehicle {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; //차량id
	
	@Column(name="OWNER_ID")
	private Long ownerId; // 오너id
	
	@Column(name="PLATE_NO")
	private String plateNo; //차대번호
	
	@Column(name="MODEL")
	private String model; //차종
	
	// @Transient
	/*
	@Column(name="FUEL")
	private Integer fuel; // 연료량
	
	@Column(name="ODOMETER")
	private Integer odometer; // 총 주행거리
	
	@Column(name="PLACE")
	private String place; // 위치
	
	@Column(name="CONTROL_USER_TYPE")
	private String controlUserType; // 제어권한을 보유한 유저 타입
	
	@Column(name="CONTROL_USER_ID")
	private Long controlUserId; // 제어권한을 보유한 유저id
	*/
}
