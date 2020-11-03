package com.example.service;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.common.UserSession;
import com.example.entity.Keyshare;
import com.example.entity.Vehicle;
import com.example.model.ControlUser;
import com.example.model.KeyshareRequest;
import com.example.model.KeyshareResponse;
import com.example.model.PushNotificationRequest;
import com.example.model.ReturnRequest;
import com.example.model.ReturnResponse;
import com.example.model.ShareCodeRequest;
import com.example.model.ShareUserKeyshareListResponse;
import com.example.repository.KeyshareRepository;
import com.example.repository.VehicleRepository;
import com.example.util.KeyshareStat;
import com.example.util.UserType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class KeyshareService {
	@Autowired
	private KeyshareRepository keyshareRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	InternalApiService apiService;
	
	@Autowired
	private HttpSession session;
	
	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private static Calendar cal = Calendar.getInstance();
	
	private static int validControlRequestTime = 30; // 키 공유 시작시간으로부터 제어 요청 가능 시간 (분)
	
	static {
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+0900"));
	}
		
	//새로운 키 공유 생성
	public KeyshareResponse save(KeyshareRequest req) throws ParseException {
		UserSession loginInfo = getOwnerSession(); // 오너 사용자인지 확인
		//오너가 보유중인 차량id인지 확인
		isOwnerVehicle(req.getVehicleId(), loginInfo.getId());
		//예약시간 유효성 검증
		isValidDate(req);
		//예약시간 무제한 확인
		req = getUnlimitedTime(req);
		Keyshare keyshare = keyshareRequestToEntity(req);
		//새로운 공유코드 생성
		final int codeLength = 8; // 공유코드 자리 수
		String newShareCode = createShareCode(codeLength);
		keyshare.setShareCode(newShareCode);
		keyshare.setOwnerUserId(loginInfo.getId());
		keyshare.setShareStatCode(KeyshareStat.RESERVED.code());
		keyshare = keyshareRepository.save(keyshare);
		log.info("새로운 예약 등록 id : "+keyshare.getId());
		return entityToKeyshareResponse(keyshare);
	}
	
	//난수 생성
	private String createShareCode(int num) {
		final int otpNum = num;
		String newOtp = new String();
		for(int i=0;i<otpNum;i++) {
			char randomChar = (char)((int)(Math.random()*10)+48);
			newOtp += randomChar;
		}
		log.info("새로운 공유코드 생성 : "+newOtp);
		return newOtp;
	}
		
	//공유시간 무제한 확인
	private KeyshareRequest getUnlimitedTime(KeyshareRequest req) {
		if(req.getStartDateTime().equals(req.getEndDateTime())) {
			try {
				Date eDate = formatter.parse(req.getEndDateTime());
				cal.setTime(eDate);
				cal.add(Calendar.YEAR, 10);
				req.setEndDateTime(formatter.format(cal.getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return req;
	}
	
	//공유시간 유효성 확인
	private boolean isValidDate(KeyshareRequest req) {
		Date sDate = null;
		Date eDate = null;
		try {
			sDate = formatter.parse(req.getStartDateTime());
			eDate = formatter.parse(req.getEndDateTime());
		} catch (ParseException e) {
			throw new RuntimeException("유효하지 않은 시간 형식입니다.");
		}
		if(sDate.before(new Date())) {
			throw new RuntimeException("현재시간 이전에 시작하는 예약입니다.");
		}
		List<Keyshare> listKeyshare = keyshareRepository.findAllByVehicleIdAndStartDateBetweenOrEndDateBetweenAndRsvnStatCodeNotIn(req.getVehicleId(), sDate, eDate, KeyshareStat.getDoneStatList());
		if(listKeyshare.size() > 0) {
			throw new RuntimeException("예약된 공유시간과 겹치는 시간입니다.");
		}
		if(sDate.after(eDate)) {
			throw new RuntimeException("예약 종료시간은 시작시간보다 나중이어야합니다.");
		}
		return true;
	}

	//오너 로그인 세션
	private UserSession getOwnerSession() {
		UserSession loginInfo = getUserSession();
		if(!loginInfo.getUserType().equals("OW")) {
			throw new RuntimeException("오너 사용자가 아닙니다.");
		}
		return loginInfo;
	}
	
	//공유 사용자 로그인 세션
	private UserSession getShareSession() {
		UserSession loginInfo = getUserSession();
		if(!loginInfo.getUserType().equals("SH")) {
			throw new RuntimeException("공유 사용자가 아닙니다.");
		}
		return loginInfo;
	}
	
	public UserSession getUserSession() {
		return (UserSession) session.getAttribute("loginInfo");
	}
	
	//오너의 차량인지 검사
	private boolean isOwnerVehicle(Long vehicleId, Long ownerId) {
		Vehicle vehicle = findVehicleById(vehicleId);
		if(!vehicle.getOwnerId().equals(ownerId)) {
			throw new RuntimeException("오너 소유의 차량 id가 아닙니다.");
		}
		return true;
	}

	//Request to entity
	private Keyshare keyshareRequestToEntity(KeyshareRequest req) throws ParseException {
		Keyshare entity = new Keyshare();
		entity.setStartDate(formatter.parse(req.getStartDateTime()));
		entity.setEndDate(formatter.parse(req.getEndDateTime()));
		entity.setShareRemark(req.getShareRemark());
		entity.setVehicleId(req.getVehicleId());
		return entity;
	}
	
	//Entity to response
	private KeyshareResponse entityToKeyshareResponse(Keyshare entity) {
		KeyshareResponse res = new KeyshareResponse();
		res.setKeyshare(entity);
		return res;
	}
	
	//공유 사용자 공유 키-차량 목록
	public List<ShareUserKeyshareListResponse> getKeyshareListByShareUser() {
		List<ShareUserKeyshareListResponse> listRes = new ArrayList<ShareUserKeyshareListResponse>();
		UserSession loginInfo = getShareSession();
		List<Keyshare> listKeyshare = keyshareRepository.findAllByShareUserIdOrderByStartDateDesc(loginInfo.getId());
		for(Keyshare keyshare : listKeyshare) {
			Vehicle vehicle = findVehicleById(keyshare.getVehicleId());
			ShareUserKeyshareListResponse res = new ShareUserKeyshareListResponse();
			res.setKeyshare(keyshare);
			res.setVehicle(vehicle);
			listRes.add(res);
		}
		return listRes;
	}
	
	//차량 상세정보 가져오기
	public Vehicle findVehicleById(Long vehicleId) {
		return vehicleRepository.findById(vehicleId).orElseThrow(() -> new RuntimeException("등록되지 않은 차량 id입니다."));
	}
	
	//키 공유 상세정보 가져오기
	public Keyshare findKeyshareById(Long keyshareId) {
		return keyshareRepository.findById(keyshareId).orElseThrow(() -> new RuntimeException("등록되지 않은 예약 id입니다."));
	}
	
	//차량리스트 가져오기
	public List<Vehicle> getVehicleList() {
		UserSession loginInfo = getOwnerSession();
		return vehicleRepository.findAllByOwnerId(loginInfo.getId());
	}

	//키 공유 리스트 가져오기
	public List<Keyshare> getListByVehicleId(Long vehicleId) {
		return keyshareRepository.findAllByVehicleId(vehicleId);
	}
	
	//공유코드 등록
	public Keyshare putShareCode(ShareCodeRequest req) {
		UserSession loginInfo = getShareSession();
		Keyshare keyshare = keyshareRepository.findByShareCode(req.getShareCode()).orElseThrow(() -> new RuntimeException("공유코드에 해당하는 공유 키가 없습니다."));
		keyshare.setShareUserId(loginInfo.getId());
		keyshare.setShareCode(null);
		return keyshareRepository.save(keyshare);
	}
	
	//로그인한 사용자 차량의 제어 권한 확인
	public boolean isControl(Long vehicleId) {
		ControlUser controlUser = getControlUser(vehicleId);
		UserSession loginInfo = getUserSession();
		if(!(loginInfo.getUserType().equals(controlUser.getUserType()) && loginInfo.getId().equals(controlUser.getUserId()))) {
			throw new RuntimeException("제어 권한이 없는 사용자입니다.");
		}
		return true;
	}
	
	//제어 유저 반환
	private ControlUser getControlUser(Long vehicleId) {
		ControlUser controlUser = new ControlUser();
		Vehicle vehicle = findVehicleById(vehicleId);
		List<Keyshare> listKeyshare = keyshareRepository.findAllByVehicleIdAndShareStatCodeIn(vehicleId, KeyshareStat.getUsingStatList());
		if(listKeyshare.size() > 1) {
			throw new RuntimeException("Error : 한 차량에 운행중인 예약이 2개 이상입니다!!");
		}
		if(listKeyshare.isEmpty()) {
			controlUser.setUserId(vehicle.getOwnerId());
			controlUser.setUserType(UserType.OWNER.code());
		}else {
			controlUser.setUserId(listKeyshare.get(0).getShareUserId());
			controlUser.setUserType(UserType.SHARE.code());
		}
		return controlUser;
	}
	
	//제어 요청(공유 사용자)
	public Keyshare requestControl(Long keyshareId) throws JsonMappingException, JsonProcessingException {
		Keyshare keyshare = new Keyshare();
		keyshare = findKeyshareById(keyshareId);
		
		//요청 유효성 검사
		isValidRequest(keyshare);
		
		//오너 사용자의 device로 예약 id Notification
		sendRequestControlPushNotification(apiService.getFcmToken(keyshare.getOwnerUserId()),keyshare.getId());

		//keyshare 상태코드변경 : CR
		keyshare.setShareStatCode(KeyshareStat.CONTROL_REQUIRED.code());
		keyshare = keyshareRepository.save(keyshare);
		return keyshare;
	}
	
	private void sendRequestControlPushNotification(List<String> listFcmToken, Long keyshareId) throws JsonMappingException, JsonProcessingException {
		PushNotificationRequest pushNoti = new PushNotificationRequest("제어 권한 요청", "제어 권한 요청 메시지입니다.");
		Map<String, String> data = new HashMap<String, String>();
		data.put("keyshareId", String.valueOf(keyshareId));
		pushNoti.setCustomData(data);
		for(String fcmToken : listFcmToken) {
			pushNoti.setToken(fcmToken);
			apiService.sendNotificationToToken(pushNoti);
		}
	}

	//제어요청 유효성 확인
	private boolean isValidRequest(Keyshare keyshare) {
		Date afterCurrentDate = new Date();
		cal.setTime(afterCurrentDate);
		cal.add(Calendar.MINUTE, validControlRequestTime);
		afterCurrentDate= cal.getTime();
		//다른 공유 사용자가 제어하고있는 경우
		if(getControlUser(keyshare.getVehicleId()).getUserType().equals(UserType.SHARE.code())) {
			throw new RuntimeException("다른 공유 사용자가 운행하고 있는 차량입니다.");
		}
		//공유 요청 시간 검사
		if(afterCurrentDate.before(keyshare.getStartDate())) {
			throw new RuntimeException("공유시작 "+validControlRequestTime+"분 전부터 제어 권한을 요청할 수 있습니다.");
		}
		
		if(new Date().after(keyshare.getEndDate())) {
			throw new RuntimeException("현재시간이 종료시간보다 이전인경우 제어권한을 요청할 수 없습니다.");
		}
		return true;
	}

	//제어 요청 상태인 예약인지 확인
	private boolean isRequestControl(Keyshare keyshare) {
		isControl(keyshare.getVehicleId());
		if(KeyshareStat.toStat(keyshare.getShareStatCode()) != KeyshareStat.CONTROL_REQUIRED) {
			throw new RuntimeException("제어 요청되지 않은 예약입니다.");
		}
		return true;
	}
	
	//스탯 코드 변경
	private Keyshare changeShareStatCode(Keyshare keyshare, String statCode) {
		keyshare.setShareStatCode(statCode);
		return keyshare;
	}
	
	//제어 요청 수락
	public Keyshare acceptControl(Long keyshareId) {
		Keyshare keyshare = findKeyshareById(keyshareId);
		isRequestControl(keyshare);
		//keyshare 상태코드 변경 : DP
		keyshare = changeShareStatCode(keyshare, KeyshareStat.IN_USE.code());
		//keyshare 실제 운행 시작시간 변경
		keyshare.setRealStartDate(new Date());
		return keyshareRepository.save(keyshare);
	}

	//제어 요청 거절
	public Keyshare rejectControl(Long keyshareId) {
		Keyshare keyshare = findKeyshareById(keyshareId);
		isRequestControl(keyshare);
		//keyshare 상태코드 변경 : CJ
		keyshare = changeShareStatCode(keyshare, KeyshareStat.CONTROL_REJECT.code());
		return keyshareRepository.save(keyshare);
	}
	
	//공유 키 반납
	@Transactional
	public ReturnResponse returnKeyshare(ReturnRequest req) {
		ReturnResponse res = new ReturnResponse();
		UserSession loginInfo = getShareSession(); 
		Keyshare keyshare = findKeyshareById(req.getKeyshareId());
		//로그인 사용자가 해당 예약의 공유 사용자인지 검사
		if(!keyshare.getShareUserId().equals(loginInfo.getId())) {
			throw new RuntimeException("해당 예약에 등록된 공유사용자가 아닙니다.");
		}
		//운행중인 상태인지 검사
		isUsingShareStatCode(keyshare.getShareStatCode());
		//반납 특이사항, 실제 종료운행종료시간, stat코드 변경 : RC	
		keyshare.setReturnRemark(req.getReturnRemark());
		res.setKeyshare(updateReturnKeyshare(keyshare));
		return res;
	}

	private Keyshare updateReturnKeyshare(Keyshare keyshare) {
		keyshare.setRealEndDate(new Date());
		keyshare.setShareStatCode(KeyshareStat.COMPLETED.code());
		return keyshareRepository.save(keyshare);
	}

	private boolean isUsingShareStatCode(String shareStatCode) {
		for(String statCode : KeyshareStat.getUsingStatList()) {
			if(shareStatCode.equals(statCode)) {
				return true;
			}
		}
		throw new RuntimeException("사용중인 차량만 반납하실수 있습니다.");
	}
}
