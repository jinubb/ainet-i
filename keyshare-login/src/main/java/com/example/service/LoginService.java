package com.example.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.common.UserSession;
import com.example.entity.LoginToken;
import com.example.entity.SmsOtp;
import com.example.entity.User;
import com.example.model.LoginRequest;
import com.example.model.LoginResponse;
import com.example.model.OtpRequest;
import com.example.model.Profile;
import com.example.model.ShareLoginRequest;
import com.example.model.SmsRequest;
import com.example.model.UuidRequest;
import com.example.repository.LoginTokenRepository;
import com.example.repository.SmsOtpRepository;
import com.example.repository.UserRepository;
import com.example.util.UserType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LoginTokenRepository loginTokenRepository;
	
	@Autowired
	SmsOtpRepository smsOtpRepository;
	
	@Autowired
	InternalApiService apiService;
	
	private static final int tokenExpiredTime = 600; // 토큰 유효시간 10분
	private static final int otpExpiredTime = 180; // otp 유효시간 3분
	private static Calendar cal = Calendar.getInstance();
	
	private String createToken(UserSession newUserSession, int expiredTime) {
		HttpSession newSession = createSession(); //새로운 세션 생성
		newSession.setAttribute("loginInfo", newUserSession);
		newSession.setMaxInactiveInterval(tokenExpiredTime);
		log.info("새로운 토큰생성  : "+newSession.getId());
		log.info("토큰 만료시간(sec) : "+tokenExpiredTime);
		return newSession.getId();
	}
	
	private UserSession createUserSession(User loginUser) {
		UserSession newUserSession = new UserSession();
		newUserSession.setUserType(loginUser.getType());
		newUserSession.setId(loginUser.getId());
		newUserSession.setName(loginUser.getName());
		newUserSession.setEmail(loginUser.getEmail());
		newUserSession.setPhone(loginUser.getPhone());
		return newUserSession;
	}
	
	private HttpSession createSession() {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession newSession = servletRequestAttribute.getRequest().getSession(true);
	    return newSession;
	}
	
	//공유 사용자 로그인
	@Transactional
	public LoginResponse shareLogin(ShareLoginRequest req) {
		LoginResponse res = new LoginResponse();
		User loginShareUser = new User();
		//카카오 로그인 정보(이메일 or 전화번호)로 등록된 공유 사용자 검색
		Optional<Object> optEmail = Optional.ofNullable(req.getProfile().getEmail());
		Optional<User> optUser = Optional.empty();
		//이메일 정보가 존재하는 경우
		if(optEmail.isPresent()) {
			String userEmail = String.valueOf(optEmail.get());
			optUser = userRepository.findByEmailAndType(userEmail,UserType.SHARE.code());
		}else { //전화번호 정보가 존재하는 경우
			String phone = Optional.ofNullable(req.getProfile().getPhone()).orElseThrow(() -> new RuntimeException("사용자의 이메일 정보와 전화번호 정보가 모두 존재하지 않습니다."));
			optUser = userRepository.findByPhoneAndType(phone, UserType.SHARE.code());
		}
		
		//기존에 등록된 공유사용자인 경우
		if(optUser.isPresent()){
			loginShareUser = optUser.get();
		}else { //새로운 공유사용자 등록
			loginShareUser = createShareUser(req.getProfile());
		}
		
		//uuid저장
		saveUuidAndFcmToken(req.getUuid(),req.getFcmToken(),loginShareUser.getId(),loginShareUser.getType());
		
		//유저 토큰 정보 발급
		res.setAuthToken(createToken(createUserSession(loginShareUser), tokenExpiredTime));
		res.setLoginUser(loginShareUser);
		return res;
	}
	
	//새로운 공유사용자 등록
	private User createShareUser(Profile profile) {
		User newShareUser = new User();
		newShareUser.setType(UserType.SHARE.code());
		newShareUser.setEmail(profile.getEmail());
		newShareUser.setName(profile.getName());
		newShareUser.setPhone(profile.getPhone());
		newShareUser.setProfileImage(profile.getProfileImage());
		newShareUser.setThumbnailImage(profile.getThumbnailImage());
		newShareUser.setAgeRange(profile.getAgeRange());
		newShareUser.setBirthday(profile.getBirthday());
		newShareUser.setBirthyear(profile.getBirthyear());
		newShareUser.setGender(profile.getGender());
		newShareUser.setCi(profile.getCi());
		return userRepository.save(newShareUser);
	}
	
	//토큰삭제
	public void logoutSession() {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession logoutSession = Optional.ofNullable(servletRequestAttribute.getRequest().getSession(false)).orElseThrow(() -> new RuntimeException("토큰이 유효하지 않습니다."));		
		logoutSession.invalidate();
	}
	
	//uuid 자동 로그인
	public LoginResponse checkUuid(UuidRequest req) {
		LoginResponse res = new LoginResponse();
		LoginToken loginToken = loginTokenRepository.findByUuidAndUserType(req.getUuid(),req.getUserType().toUpperCase()).orElseThrow(() -> new RuntimeException("등록되지 않은 device uuid입니다."));
		User loginUser = findByUserId(loginToken.getUserId());
		res.setAuthToken(createToken(createUserSession(loginUser), tokenExpiredTime));
		res.setLoginUser(loginUser);
		return res;
	}

	public User findByUserId(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("등록되지 않은 유저 id입니다."));
	}

	//OTP sms발송
	public void sendSmsOtp(OtpRequest req) {
		User user = userRepository.findByPhoneAndType(req.getPhone(),UserType.OWNER.code()).orElseThrow(() -> new RuntimeException("등록되지 않은 전화번호입니다."));
		String newOtp = createOtp(4); // otp생성
		smsService(newOtp, user.getPhone());
		SmsOtp newSmsOtp = new SmsOtp();
		newSmsOtp.setOtp(newOtp);
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, otpExpiredTime);
		newSmsOtp.setExpiredDate(cal.getTime());
		log.info("otp 유효기간 : " + String.valueOf(cal.getTime()));
		newSmsOtp.setUserId(user.getId());
		newSmsOtp.setValid("Y");
		smsOtpRepository.save(newSmsOtp);
	}
	
	//난수 생성
	private String createOtp(int num) {
		final int otpNum = num;
		String newOtp = new String();
		for(int i=0;i<otpNum;i++) {
			char randomChar = (char)((int)(Math.random()*10)+48);
			newOtp += randomChar;
		}
		log.info("새로운 otp 생성 : "+newOtp);
		return newOtp;
	}
	
	//sms발송
	private void smsService(String newOtp, String phone) {
		SmsRequest smsReq = new SmsRequest();
		smsReq.setPayload(String.format("AiNET-I 인증번호 : %s", newOtp));
		smsReq.setReceiver(phone);
		smsReq.setType(SmsRequest.SMS);
		apiService.sendSmsCommand(smsReq, apiService.getTokenByDemon());
	}

	//OTP 인증 로그인
	@Transactional
	public LoginResponse loginUser(LoginRequest req) {
		LoginResponse res = new LoginResponse();
		
		SmsOtp smsOtp = smsOtpRepository.findById(req.getOtp()).orElseThrow(() -> new RuntimeException("존재하지 않는 otp입니다."));
		//otp 유효기간 검사
		isValidOtp(smsOtp);
	
		User loginUser = findByUserId(smsOtp.getUserId());
		//uuid 저장
		saveUuidAndFcmToken(req.getUuid(), req.getFcmToken(), loginUser.getId(), loginUser.getType());
		//otp 유효성 변경
		smsOtp.setValid("N");
		smsOtpRepository.save(smsOtp);
		//유저 토큰 정보 발급
		res.setAuthToken(createToken(createUserSession(loginUser), tokenExpiredTime));
		res.setLoginUser(loginUser);
		return res;
	}
	
	private boolean isValidOtp(SmsOtp smsOtp) {
		if(smsOtp.getExpiredDate().before(new Date())) {
			throw new RuntimeException("유효기간이 지났습니다.");
		}else if(smsOtp.getValid().equals("N")) {
			throw new RuntimeException("이미 사용된 인증코드입니다.");
		}	
		return true;
	}

	//uuid 저장(자동로그인)
	private LoginToken saveUuidAndFcmToken(String uuid, String fcmToken, Long userId, String userType) {
		LoginToken loginToken = new LoginToken();
		loginToken.setUuid(uuid);
		loginToken.setFcmToken(fcmToken);
		loginToken.setUserId(userId);
		loginToken.setUserType(userType);
		return loginTokenRepository.save(loginToken);
	}

	public List<String> getFcmToken(Long userId) {
		List<LoginToken> listLoginToken = loginTokenRepository.findAllByUserIdAndUserType(userId, UserType.OWNER.code());
		if(listLoginToken.isEmpty()) {
			throw new RuntimeException("FCM device token이 존재하지 않습니다.");
		}
		List<String> listFcmToken = new ArrayList<>();
		for(LoginToken loginToken : listLoginToken) {
			listFcmToken.add(loginToken.getFcmToken());
		}
		return listFcmToken;
	}
}
