package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.LoginToken;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginToken, Long>{
	public Optional<LoginToken> findByUuidAndUserType(String uuid, String userType);
	
	public List<LoginToken> findAllByUserIdAndUserType(Long userId, String userType);
	
	public Optional<LoginToken> findByUuidAndFcmTokenAndUserIdAndUserType(String uuid, String fcmToken, Long userId, String userType);
	
	public Optional<LoginToken> findByUuidAndFcmTokenAndUserType(String uuid, String fcmToken, String userType);
}
