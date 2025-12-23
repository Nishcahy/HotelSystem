package com.inventoryservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.inventoryservice.entity.Hotel;
import com.inventoryservice.repo.Hotelrepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {
	
	private Hotelrepo hotelrepo;
	
	@Override
	@Transactional
    // @CachePut updates the cache every time a new hotel is created
    @CachePut(value = "hotels", key = "#hotel.hotelId")
	public Hotel createHotel(Hotel hotel) {
		return hotelrepo.save(hotel);
	}

	@Override
	@Cacheable(value = "hotels", key = "#id")
	public Hotel getHotelById(Long id) {
		return hotelrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
	}

	@Override
	@Transactional
    @CacheEvict(value = "hotels", key = "#id")
	public void deleteHotel(Long id) {
		hotelrepo.deleteById(id);
	}

	@Override
	public boolean isHotelOwner(Long hotelId, Long userId) {
		return hotelrepo.findById(hotelId).map(hotel -> hotel.getOwnerUserId().equals(userId)).orElse(false);
	}

}
