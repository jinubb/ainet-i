package com.example.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum KeyshareStat {
	RESERVED("DR"),		   //예약 
	IN_USE("DP"),		   //운행 중
	CONTROL_REQUIRED("CR"),//제어 권한 요청
	CONTROL_REJECT("CJ"),  //제어 권한 거절
	RETURN_REQUIRED("RR"), //반납 예정
	RETURN_DELAYED("RD"),  //반납 지연 
	COMPLETED("RC"),	   //반납 완료 
	CANCELLED("EC"),	   //예약 취소
	DELAY_START("DS"),	   //반납 지연으로 인한 운행 시작 지연.
	UNKNOWN("");
	public final static String typeCode = "RVS";
	public final static String RSVN = "예약";
	public final static String RSVN_COMPLETE = "예약완료";
	private final String code;
	
	
	public final static List<String> WaitingAndUseStatCodes = Arrays.asList(KeyshareStat.IN_USE.code, KeyshareStat.RETURN_DELAYED.code, KeyshareStat.RETURN_REQUIRED.code, KeyshareStat.RESERVED.code, KeyshareStat.DELAY_START.code);
	public final static List<String> StartScheduledStatCodes =  Arrays.asList(KeyshareStat.RESERVED.code(), KeyshareStat.DELAY_START.code());
	public final static List<String> CancellAvailableStatCodes = Arrays.asList(KeyshareStat.RESERVED.code(), KeyshareStat.DELAY_START.code()); 


	private KeyshareStat(String code) {
		this.code = code;
	}

	public String code() { return code; }
	public static String getTypeCode() { return typeCode; }
	public static KeyshareStat toStat(String code) {
		switch(code) {
		case "DR":
			return RESERVED;
		case "DP":
			return IN_USE;
		case "CR":
			return CONTROL_REQUIRED;
		case "CJ":
			return CONTROL_REJECT;
		case "RR":
			return RETURN_REQUIRED;
		case "RC":
			return COMPLETED;
		case "RD":
			return RETURN_DELAYED;
		case "EC":
			return CANCELLED;
		case "DS":
			return DELAY_START;
		
			default:
				return UNKNOWN;
		}
	}
	public static void checkStatusIsInUse(KeyshareStat stat) {
		switch (stat) {
			case IN_USE:
			case RETURN_DELAYED:	
			case RETURN_REQUIRED:
				break;
			default:
				throw new RuntimeException("운행중인 예약상태가 아닙니다.");
		}
	}
	
	public static List<String> getDoneStatList() {
		return toCodeList(Arrays.asList(KeyshareStat.CANCELLED, KeyshareStat.COMPLETED, KeyshareStat.CONTROL_REJECT));
	}
	
	public static List<String> getInUseStatList() {
		return toCodeList(Arrays.asList(KeyshareStat.CONTROL_REQUIRED, KeyshareStat.IN_USE, KeyshareStat.RESERVED));
	}
	
	public static List<String> getNotUsingStatList() {
		return toCodeList(Arrays.asList(KeyshareStat.CANCELLED, KeyshareStat.COMPLETED, KeyshareStat.CONTROL_REJECT, KeyshareStat.CONTROL_REQUIRED, KeyshareStat.RESERVED));
	}
	
	public static List<String> getUsingStatList() {
		return toCodeList(Arrays.asList(KeyshareStat.RETURN_DELAYED, KeyshareStat.IN_USE, KeyshareStat.RETURN_REQUIRED, KeyshareStat.DELAY_START));
	}
	
	
	
	
	public static List<String> getStatList() {
		return toCodeList(Arrays.asList(KeyshareStat.CANCELLED, KeyshareStat.COMPLETED,  KeyshareStat.RESERVED, KeyshareStat.DELAY_START, KeyshareStat.IN_USE, KeyshareStat.RETURN_REQUIRED, KeyshareStat.RETURN_DELAYED));
	}
	
	public static List<String> getAvailableUpdateStats() {
		return toCodeList(Arrays.asList(KeyshareStat.CANCELLED, KeyshareStat.COMPLETED, KeyshareStat.IN_USE));
	}
	public static List<String> getStatListInUse() {
		return toCodeList(Arrays.asList(KeyshareStat.IN_USE, KeyshareStat.RETURN_DELAYED, KeyshareStat.RETURN_REQUIRED));
	}
	public static List<String> getStatListExceptCancelStat(){
		return toCodeList(Arrays.asList(KeyshareStat.DELAY_START, KeyshareStat.COMPLETED,  KeyshareStat.RESERVED,  KeyshareStat.IN_USE,  KeyshareStat.RETURN_REQUIRED,  KeyshareStat.RETURN_DELAYED));
	}
	public static List<String> toCodeList(List<KeyshareStat> statList){
		List<String> codeList = new ArrayList<>();
		statList.stream().forEach(stat -> codeList.add(stat.code()));
		return codeList;
	}
	public static List<String> getTargetStatList(String status){
		String upperStat = status.toUpperCase();
		List<String> codeList = new ArrayList<>();
		switch(upperStat) {
			case "ALL":
				codeList = getStatList(); break;
			case "NORMAL":
				codeList = toCodeList(Arrays.asList(KeyshareStat.RESERVED, KeyshareStat.IN_USE, KeyshareStat.RETURN_REQUIRED, KeyshareStat.RETURN_DELAYED)); break;
			case "CANCEL":
				codeList = toCodeList(Arrays.asList(KeyshareStat.CANCELLED)); break;
			case "RECENT":
				codeList = toCodeList(Arrays.asList(KeyshareStat.COMPLETED, KeyshareStat.IN_USE, KeyshareStat.RETURN_DELAYED, KeyshareStat.RETURN_REQUIRED)); break;

		}
		return codeList;
	}
}
