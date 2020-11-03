package com.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KeyshareRequest {
	private String startDateTime; //공유 시작 시간
	private String endDateTime; //공유 반납 시간
	private Long vehicleId; //예약 차량 id
	private String shareRemark; //공유 특이사항
}
