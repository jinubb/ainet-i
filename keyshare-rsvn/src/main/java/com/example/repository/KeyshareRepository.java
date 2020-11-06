package com.example.repository;

import java.util.Date;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Keyshare;

@Repository
public interface KeyshareRepository extends JpaRepository<Keyshare, Long>{
	public Optional<Keyshare> findByShareCode(String shareCode);
	
	public List<Keyshare> findAllByVehicleId(Long vehicleId);
	
//	public Optional<KeyshareEntity> findTop1ByVehicleIdAndShareStatCode(Long vehicleId, String shareStatCode);
	
	//Find list of before startDate
	public List<Keyshare> findAllByVehicleIdAndShareStatCodeAndStartDateGreaterThan(Long vehicleId, String shareStatCode, Date startDate);
	
	//예약시작 1분 전까지 예약된 모든 예약을 가져옴 
	@Query(value="select * from keyshare where share_stat_cd in ('RESERVED') and start_dt <= date_add(now() , interval 1 minute)", nativeQuery=true)// 예약시작 시간 1분전까지의 조건에 맞는 모든 예약을 가져옴. 
	public List<Keyshare> findAllStatUpdateCandiates();
	
	@Query(value="select * from keyshare where vehicle_id = :vehicleId and share_stat_cd not in :statList and (start_dt between :sd and :ed or end_dt between :sd and :ed or (start_dt <= :sd and :ed <= end_dt))", nativeQuery=true)
	public List<Keyshare> findAllByVehicleIdAndStartDateBetweenOrEndDateBetweenAndRsvnStatCodeNotIn(Long vehicleId, @Param("sd") Date startDateTime, @Param("ed") Date endDateTime, List<String> statList);
	
	public Optional<Keyshare> findByShareUserId(Long shareUserId);
	
	//현재시간 이후 공유사용자id별 예약리스트 확인
	public List<Keyshare> findAllByShareUserIdAndShareStatCodeInAndStartDateAfter(Long shareUserId, List<String> listShareStatCode, Date currentDate);
	
	public List<Keyshare> findAllByShareUserIdOrderByStartDateDesc(Long shareUserId);
	//공유사용자id별 시작시간 역순으로 예약리스트 확인
//	@Query(value = "select k from Keyshare k where share_user_id = shareUserId ORDER BY start_dt DESC")
//	@Query(value="select * from keyshare where vehicle_id = :vehicleId and share_stat_cd not in :statList and (start_dt between :sd and :ed or end_dt between :sd and :ed or (start_dt <= :sd and :ed <= end_dt))", nativeQuery=true)
//	public List<Keyshare> findAllByShareUserIdAndOrderByStartDateDesc(Long shareUserId);
	
	public List<Keyshare> findAllByVehicleIdAndShareStatCodeIn(Long vehicleId, List<String> listShareStatCode);
	
	public List<Keyshare> findAllByVehicleIdAndShareStatCodeInOrderByStartDateDesc(Long vehicleId, List<String> listShareStatCode);

}
