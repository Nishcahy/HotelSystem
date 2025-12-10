package com.hotelbooking.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequest {
    
    @NotNull
    private String fullName;
    
    private Integer age;
    
    @NotNull
    private String phone;
}
