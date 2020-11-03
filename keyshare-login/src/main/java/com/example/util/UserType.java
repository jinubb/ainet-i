package com.example.util;

public enum UserType {
	OWNER("OW"), 
	SHARE("SH"),
	SERVICE("SE"),
	UNKNOWN("");
	
	private final String code;
	private UserType(String code) {
		this.code = code;
	}
	
	public String code() {
		return code;
	}
	
	public static UserType toType(String code) {
		switch(code) {
			case "OW":
				return OWNER;
			case "SH":
				return SHARE;
			case "SE":
				return SERVICE;
			default:
				throw new RuntimeException(String.format("존재하지 않는 유저타입 코드 '%s' 입니다.", code));
		}
	}
	public static boolean isShareType(String code) {
		switch(code) {
			case "SH":
				return true;
			default:
				return false;
		}
	}
	
	public static boolean isOwnerType(String code) {
		switch(code) {
			case "OW":
				return true;
			default:
				return false;
		}
	}


}
