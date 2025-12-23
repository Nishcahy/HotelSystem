package com.inventoryservice.service;

import com.inventoryservice.entity.Hotel;

public interface IHotelService {
	Hotel createHotel(Hotel hotel);
	Hotel getHotelById(Long id);
	void deleteHotel(Long id);
	boolean isHotelOwner(Long hotelId, Long userId);
}
