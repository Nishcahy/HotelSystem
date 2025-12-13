package com.inventoryservice.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "room_inventory")
public class RoomInventory implements Serializable {
private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    // ⭐️ One-to-One link to RoomType
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false, unique = true)
    private RoomType roomType;

    // ⭐️ The crucial real-time count
    @Column(nullable = false)
    private int availableCount;

    // Total rooms of this type (optional but useful)
    private int totalRooms;

}
