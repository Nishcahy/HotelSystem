package com.inventoryservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.Hotel;

@Repository
public interface Hotelrepo extends JpaRepository<Hotel, Long> {
	List<Hotel> findByOwnerUserId(Long ownerUserId);
}
