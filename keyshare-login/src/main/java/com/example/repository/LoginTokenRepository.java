package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.LoginToken;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginToken, String>{
	public Optional<LoginToken> findByUuidAndUserType(String uuid, String userType);
	
	public List<LoginToken> findAllByUserIdAndUserType(Long userId, String userType);
}
