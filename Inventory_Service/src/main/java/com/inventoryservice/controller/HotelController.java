package com.inventoryservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventoryservice.entity.Hotel;
import com.inventoryservice.service.HotelService;
import com.inventoryservice.service.IHotelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

	private final IHotelService hotelService;

	// 1. Create Hotel - Admin or Hotel Owner can create
	@PostMapping
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('HOTEL_OWNER')")
	public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel, Authentication authentication) {
		// Automatically set the owner ID from the JWT token
		Long userId = (Long) authentication.getPrincipal(); // Or use claims check
		hotel.setOwnerUserId(userId);
		return new ResponseEntity<>(hotelService.createHotel(hotel), HttpStatus.CREATED);
	}

	// 2. Get Hotel Details - Public access (Search)
	@GetMapping("/{id}")
	public ResponseEntity<Hotel> getHotel(@PathVariable Long id) {
		return ResponseEntity.ok(hotelService.getHotelById(id));
	}

	// 3. Update Hotel - Admin or the specific Owner only
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('SUPER_ADMIN') or "
			+ "(hasRole('HOTEL_OWNER') and @hotelService.isHotelOwner(#id, authentication.principal.claims['userId']))")
	public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
		// Logic to update...
		return ResponseEntity.ok(hotelService.createHotel(hotel));
	}

	// 4. Delete Hotel - Admin or the specific Owner only
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('SUPER_ADMIN') or "
			+ "(hasRole('HOTEL_OWNER') and @hotelServiceImpl.isHotelOwner(#id, authentication.principal.claims['userId']))")
	public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
		hotelService.deleteHotel(id);
		return ResponseEntity.noContent().build();
	}
}
