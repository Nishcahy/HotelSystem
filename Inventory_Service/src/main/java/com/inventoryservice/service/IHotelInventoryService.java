package com.inventoryservice.service;

import com.inventoryservice.entity.BookingEvent;

public interface IHotelInventoryService {
	void consumeBookingEvent(BookingEvent event);
	Integer getAvailableRooms(Long hotelId, Long roomTypeId);
}
