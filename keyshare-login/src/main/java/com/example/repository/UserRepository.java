package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	public Optional<User> findByPhoneAndName(String phone, String name);
	
	public Optional<User> findByEmailAndType(String email, String userType);
	
	public Optional<User> findByPhoneAndType(String phone, String userType);
	
	public Optional<User> findByPhone(String phone);
	
	public Optional<User> findByEmailAndPhone(String email, String phone);
}
