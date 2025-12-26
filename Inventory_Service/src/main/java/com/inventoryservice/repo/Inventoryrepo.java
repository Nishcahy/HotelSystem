package com.inventoryservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventoryservice.entity.RoomInventory;

public interface Inventoryrepo extends JpaRepository<RoomInventory, Long> {
	
	Optional<RoomInventory> findByRoomTypeRoomTypeId(Long roomTypeId);
	
	@Modifying
    @Query("UPDATE RoomInventory r SET r.availableCount = r.availableCount + :amount " +
           "WHERE r.roomType.roomTypeId = :roomTypeId AND (r.availableCount + :amount) >= 0")
    int updateInventory(@Param("roomTypeId") Long roomTypeId, @Param("amount") int amount);

    @Query("SELECT r.availableCount FROM RoomInventory r WHERE r.roomType.roomTypeId = :roomTypeId")
    Integer findCountByRoomTypeId(@Param("roomTypeId") Long roomTypeId);
}
