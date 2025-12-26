package com.inventoryservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventoryservice.service.IHotelInventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IHotelInventoryService inventoryService;

    // 1. Check Availability - Public (Used during the booking flow)
    @GetMapping("/availability/{hotelId}/{roomTypeId}")
    public ResponseEntity<Integer> getRoomAvailability(
            @PathVariable Long hotelId, 
            @PathVariable Long roomTypeId) {
        
        // This hits Redis Hash first for high performance
        Integer count = inventoryService.getAvailableRooms(hotelId, roomTypeId);
        return ResponseEntity.ok(count);
    }

    // 2. Manually Update Inventory - Admin or Hotel Owner only
    // Note: Most updates happen automatically via Kafka, but owners might need manual override
    @PostMapping("/update/{hotelId}/{roomTypeId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or " +
                  "(hasRole('HOTEL_OWNER') and @hotelService.isHotelOwner(#hotelId, authentication.principal.claims['userId']))")
    public ResponseEntity<String> manualInventoryUpdate(
            @PathVariable Long hotelId,
            @PathVariable Long roomTypeId,
            @RequestParam int change) {
        
        // This would trigger both DB update and Redis cache sync
        return ResponseEntity.ok("Inventory updated successfully");
    }
}
