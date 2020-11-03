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
@Table(name = "keyshare")
public class Keyshare {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; //예약번호
	
	@Column(name="START_DT")
	@Temporal(TemporalType.TIMESTAMP) //TIMESTAMP
	private Date startDate; // 공유 시작 시간 

	@Column(name="END_DT")
	@Temporal(TemporalType.TIMESTAMP) //TIMESTAMP
	private Date endDate; // 공유 반납 시간
	
	@Column(name="REAL_START_DT")
	@Temporal(TemporalType.TIMESTAMP) //TIMESTAMP
	private Date realStartDate; // 실제 공유 시작 시간(차량오너 수락) 

	@Column(name="REAL_END_DT")
	@Temporal(TemporalType.TIMESTAMP) //TIMESTAMP
	private Date realEndDate; // 실제 공유 반납 시간(공유사용자 반납 or 차량오너 권한회수)

	@Column(name="VEHICLE_ID")
	private Long vehicleId; //예약 차량 id
	
	@Column(name="OWNER_USER_ID")
	private Long ownerUserId; //차량 오너 id
	
	@Column(name="SHARE_USER_ID")
	private Long shareUserId; //공유 사용자 id
	
	@Column(name="SHARE_CODE")
	private String shareCode; // 공유 코드

	@Column(name="SHARE_STAT_CD")
	private String shareStatCode; //예약 이용 상태
	
	@Column(name="SHARE_REMARK")
	private String shareRemark; // 반납 시 특이사항
	
	@Column(name="RETURN_REMARK")
	private String returnRemark; // 반납 시 특이사항
}
