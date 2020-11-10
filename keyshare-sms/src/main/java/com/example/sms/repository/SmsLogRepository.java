package com.example.sms.repository;

import java.util.List; /*multi recod select*/

import org.springframework.data.domain.Page;/*multi record pagination select*/
import org.springframework.data.domain.Pageable;/*multi record pagination select*/
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sms.entity.SmsLog;

//import org.springframework.data.jpa.repository.Modifying;/*for statements like update, delete, this annotation has to be declared*/
import org.springframework.data.jpa.repository.Query;

import java.lang.Long;


@Repository
public interface SmsLogRepository extends JpaRepository<SmsLog, Long>{

	@Query(value="select * from sms_log", nativeQuery=true)
	public List<SmsLog> getAllByNativeQuery();
	
	@Query(value="select * from sms_log", nativeQuery=true)
	public Page<SmsLog> getAllByNativeQuery(Pageable pageable);
}
