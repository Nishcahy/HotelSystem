package com.inventoryservice.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.inventoryservice.entity.BookingEvent;
import com.inventoryservice.repo.Inventoryrepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelInventoryService implements IHotelInventoryService {
	private Inventoryrepo invetoryrepo;
	private final RedisTemplate<String, Object> redisTemplate;
	
	private static final String REDIS_INV_KEY = "hotel:inventory:";
	
	@KafkaListener(topics = "new-hotel-bookings", groupId = "inventory-group")
    @Transactional
    public void consumeBookingEvent(BookingEvent event) {
        // Update Database
        inventoryrepo.updateInventory(event.getRoomTypeId(), -1);

        // Update Redis Hash (Atomic Increment/Decrement)
        String key = REDIS_INV_KEY + event.getHotelId();
        redisTemplate.opsForHash().increment(key, event.getRoomTypeId().toString(), -1);
        
        System.out.println("Inventory synced for RoomType: " + event.getRoomTypeId());
    }
	
	
	
}
