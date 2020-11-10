package com.example.sms.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="sms_log")//sms log for both user and system through message broker

@Getter
@Setter
@ToString
public class SmsLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id; //



	@Column(name="result_code")
	private String resultCode; //

	@Column(name="title")
	private String title;
	
	@Column(name="msg")
	private String message; //

	@Column(name="receiver")
	private String recepientNo;
	
	@Column(name="succ_cnt")
	private Long successCnt; //

	@Column(name="err_cnt")
	private String errCnt; //

	@Column(name="msg_type")
	private String messageType; //

	@Column(name="created_dt")
	@Temporal(TemporalType.TIMESTAMP) //TIMESTAMP
	private Date createdDate; //

	@Column(name="user_email")
	private String userEmail; //USER_EMAIL OR NON IF DONE BY SYSTEM
	@Column(name="source_id")
	private String sourceId;
	
	@Column(name="msg_id")
	private String messageId;//message id from sms service provider

/*
	@Column(name="active_stat", columnDefinition="BIT(1)")
	private Boolean active;
*/	
}
