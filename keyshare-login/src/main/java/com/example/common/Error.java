package com.example.common;

import java.util.Arrays;

import java.util.HashMap;
import java.util.Map;

import com.example.common.ResponseContainer.ErrDetail;

import lombok.Getter;

@Getter
public enum Error {
	UnAuthorizedToken(1100, "미인증된 토큰 요청."),
	IlleagalArgument(1101, "적절한 파라미터가 아닙니다."),
	UnAuthorizedCompany(1102, "해당 회사의 데이터가 이닙니다."),
	InValidRsvnCondition(1103,"예약 가능 조건에 맞지 않습니다."),
	InValidRsvnUpdateCondition(1104,"예약 수정 조건에 맞지 않습니다."),
	InValidRsvnCancelCondition(1105,"예약 취소 조건에 맞지 않습니다."),
	ResourceNotExists(1106, "존재하지 않는 리소스입니다."),
	NotSameVehicle(1107, "예약 상의 차량과 요청한 차량이 일치하지 않습니다.");
	
	private int code;
	private String message;
	private Error(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Getter
	public enum InternalService {
		Rsvn_Mgmt("rsvn-mgmt", "11"),
		Vhc_Mgmt("vhc-mgmt", "12"),
		Dept_User("dept-user", "13"),
		Pkng_Mgmt("pkng-mgmt","14"),
		Device_Mgmt("device-mgmt", "15"),
		Sys_Co("sys-co", "16"),
		User_Auth("user-auth", "17"),
		Vhc_Logs("vhc-logs", "18"),
		Log_Mgmt("log-mgmt", "19"),
		Bbs_Bulletin("bbs-bulletin", "20"),
		Bbs_Faq("bbs-faq", "21"),
		Bbs_Notice("bbs-notice", "22"),
		Cron_Mgmt("cron-mgmt", "23"),
		Tenent_Mgmt("tenant-mgmt", "24"),
		Unknown("unknown", "00");
			
		private String name;
		private String code;
		private InternalService(String serviceName, String serviceCode) {
			name = serviceName;
			code = serviceCode;
		}
		
		private static Map<String, InternalService> serviceMap = new HashMap<>();  // key: serviceCode, value: Service 
		static {
			Arrays.stream(InternalService.values()).forEach(service -> serviceMap.put(service.code, service));
		}
		
		public static InternalService from(int code) { // code : ServiceCode or ErrCode
			return code > 9 ? serviceMap.getOrDefault(Integer.toString(code).substring(0, 2), InternalService.Unknown) : InternalService.Unknown;
		}
		
		public static InternalService from(@SuppressWarnings("rawtypes") ErrDetail detail) { 
			return detail == null ? InternalService.Unknown : InternalService.from(detail.getCode());
		}
		
		public static String serviceName(@SuppressWarnings("rawtypes") ErrDetail detail) {
			String serviceName = "Unknown";
			if(detail != null) { 
				InternalService service = InternalService.from(detail.getCode());
				serviceName = service.getName();
			}
			return serviceName;
		}
		
	}
}
