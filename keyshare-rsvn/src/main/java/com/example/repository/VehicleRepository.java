package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Vehicle;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>{
	public List<Vehicle> findAllByOwnerId(Long ownerId);
	
//	public Optional<Vehicle> findByControlUserIdAndControlUserType(Long controlUserId, String controlUserType);
	
	public List<Vehicle> findAllByIdIn(List<Long> vehicleId);
}
