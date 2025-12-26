package com.inventoryservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.RoomType;

@Repository
public interface RoomTyprRepo extends JpaRepository<RoomType, Long>{
	@Query("SELECT r FROM RoomType r WHERE r.hotel.hotelId = :hotelId")
	List<RoomType> findByHotelByHotelId(Long hotelId);
}
