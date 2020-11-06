package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "sms_otp")
public class SmsOtp {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "OTP")
	private String otp;
	
	@Temporal(TemporalType.TIMESTAMP) //TIMESTAMP
	@Column(name = "EXPIRED_DT")
	private Date expiredDate;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "VALID", columnDefinition = "varchar(10) default 'Y'")
	private String valid; // 사용 전 "Y", 사용했을 경우 "N"
	
	@Column(name = "UUID")
	private String uuid;
}
