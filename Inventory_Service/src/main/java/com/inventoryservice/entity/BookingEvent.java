package com.inventoryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEvent {
	
	private Long bookingId;
    private Long hotelId;
    private Long roomTypeId;
    private Long userId;
    
    // The status of the booking (e.g., "CREATED", "CANCELLED")
    private String status;
    
    // How many rooms were involved (usually 1, but useful for group bookings)
    private Integer quantity;
}
