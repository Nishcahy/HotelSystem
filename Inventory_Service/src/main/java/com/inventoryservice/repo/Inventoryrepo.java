package com.inventoryservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventoryservice.entity.RoomInventory;

public interface Inventoryrepo extends JpaRepository<RoomInventory, Long> {
	void updateInventory();
	Optional<RoomInventory> findByRoomTypeRoomTypeId(Long roomTypeId);
}
