package com.example.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.ResponseContainer;
import com.example.entity.Keyshare;
import com.example.entity.Vehicle;
import com.example.entity.VehicleInfo;
import com.example.model.KeyshareRequest;
import com.example.model.KeyshareResponse;
import com.example.model.ReturnRequest;
import com.example.model.ReturnResponse;
import com.example.model.ShareCodeRequest;
import com.example.model.ShareUserKeyshareListResponse;
import com.example.model.VehicleImage;
import com.example.service.KeyshareService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/keyshare")
public class KeyshareController {
	private static Logger logger = LoggerFactory.getLogger(KeyshareController.class);

	@Autowired
	private KeyshareService service;
	
	@ApiOperation(value = "새로운 공유 키 생성")
	@PostMapping()
	public ResponseContainer<KeyshareResponse> createKeyshare(@RequestBody KeyshareRequest req) {
		ResponseContainer<KeyshareResponse> response = ResponseContainer.emptyResponse();		
		try {
			response.setPayload(service.save(req));
		} catch(Exception e) {
			logger.error("save:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "사용자의 보유중인 차량 리스트")
	@GetMapping("/user/vehicle/list")
	public ResponseContainer<List<Vehicle>> getVehicleList(){
		ResponseContainer<List<Vehicle>> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.getVehicleList());
		} catch(Exception e) {
			logger.error("vehicle list:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "새로운 공유 코드 등록")
	@PutMapping("/shareuser/sharecode")
	public ResponseContainer<Keyshare> putShareCode(@RequestBody ShareCodeRequest req){
		ResponseContainer<Keyshare> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.putShareCode(req));
		} catch(Exception e) {
			logger.error("list:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "공유 사용자 공유 키와 차량정보 리스트")
	@GetMapping("/shareuser/list")
	public ResponseContainer<List<ShareUserKeyshareListResponse>> getKeyshareListByShareUser(){
		ResponseContainer<List<ShareUserKeyshareListResponse>> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.getKeyshareListByShareUser());
		} catch(Exception e) {
			logger.error("list:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "공유 키 반납")
	@PutMapping("/shareuser/return")
	public ResponseContainer<ReturnResponse> returnKeyshare(@RequestBody ReturnRequest req){
		ResponseContainer<ReturnResponse> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.returnKeyshare(req));
		} catch(Exception e) {
			logger.error("return:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "차량 상세정보")
	@GetMapping("/vehicle/{vehicleId}/detail")
	public ResponseContainer<Vehicle> getVehicleDetail(@PathVariable("vehicleId")Long vehicleId){
		ResponseContainer<Vehicle> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.findVehicleById(vehicleId));
		} catch(Exception e) {
			logger.error("vehicle detail:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "차량 이미지 경로")
	@GetMapping("/vehicle/{vehicleId}/image")
	public ResponseContainer<VehicleInfo> getVehicleImage(@PathVariable("vehicleId")Long vehicleId){
		ResponseContainer<VehicleInfo> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.getVehiecleImage(vehicleId));
		} catch(Exception e) {
			logger.error("vehicle image:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "차량id별 키 공유 리스트")
	@GetMapping("/{vehicleId}/list")
	public ResponseContainer<List<Keyshare>> getListByVehicleId(@PathVariable("vehicleId")Long vehicleId){
		ResponseContainer<List<Keyshare>> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.getListByVehicleId(vehicleId));
		} catch(Exception e) {
			logger.error("list:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "공유 키 상세정보")
	@GetMapping("/{keyshareId}/detail")
	public ResponseContainer<Keyshare> getKeyshareDetail(@PathVariable("keyshareId")Long keyshareId){
		ResponseContainer<Keyshare> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.findKeyshareById(keyshareId));
		} catch(Exception e) {
			logger.error("keyshare detail:\n{}",e);
			response.setError(e);
		}
		return response;
	}

	@ApiOperation(value = "제어 권한 확인")
	@GetMapping("/control/{vehicleId}")
	public ResponseContainer<Void> isControl(@PathVariable("vehicleId")Long vehicleId){
		ResponseContainer<Void> response = ResponseContainer.emptyResponse();
		try {
			service.isControl(vehicleId);
			response.setSuccess(true);
		} catch(Exception e) {
			logger.error("is control:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "공유 사용자 제어 요청")
	@PutMapping("/control/{keyshareId}/request")
	public ResponseContainer<Keyshare> requiredControl(@PathVariable("keyshareId")Long keyshareId){
		ResponseContainer<Keyshare> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.requestControl(keyshareId));
		} catch(Exception e) {
			logger.error("control request:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "오너 사용자 제어 수락")
	@PutMapping("/control/{keyshareId}/accept")
	public ResponseContainer<Keyshare> acceptControl(@PathVariable("keyshareId")Long keyshareId){
		ResponseContainer<Keyshare> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.acceptControl(keyshareId));
		} catch(Exception e) {
			logger.error("control:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	@ApiOperation(value = "오너 사용자 제어 거절")
	@PutMapping("/control/{keyshareId}/reject")
	public ResponseContainer<Keyshare> rejectControl(@PathVariable("keyshareId")Long keyshareId){
		ResponseContainer<Keyshare> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.rejectControl(keyshareId));
		} catch(Exception e) {
			logger.error("control:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	
	
	/*
	@ApiOperation(value = "로그인 유저 정보")
	@GetMapping("/user/info")
	public ResponseContainer<UserSession> getUserSession(){
		ResponseContainer<UserSession> response = ResponseContainer.emptyResponse();
		try {
			response.setPayload(service.getUserSession());
		} catch(Exception e) {
			logger.error("info:\n{}",e);
			response.setError(e);
		}
		return response;
	}
	*/
	
}
