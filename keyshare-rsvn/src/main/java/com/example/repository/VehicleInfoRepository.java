package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.VehicleInfo;

@Repository
public interface VehicleInfoRepository extends JpaRepository<VehicleInfo, Long>{

}
