package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.SmsOtp;

@Repository
public interface SmsOtpRepository extends JpaRepository<SmsOtp, Long>{
	public Optional<SmsOtp> findByOtp(String otp);
	
	public Optional<SmsOtp> findTop1ByOtpOrderByExpiredDateDesc(String otp);
}
