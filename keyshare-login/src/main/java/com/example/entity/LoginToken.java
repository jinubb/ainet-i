package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "login_token")
public class LoginToken {
	@Id
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="USER_ID")
	private Long userId;

	@Temporal(TemporalType.TIMESTAMP) //TIMESTAMP
	@CreatedDate
	@Column(name="LOGIN_DT")
	private Date loginDate;
	
	@Column(name="USER_TYPE")
	private String userType;
	
	@Column(name="FCM_TOKEN")
	private String fcmToken;
	
}
