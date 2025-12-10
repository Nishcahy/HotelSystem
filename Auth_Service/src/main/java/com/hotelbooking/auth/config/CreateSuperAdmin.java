package com.hotelbooking.auth.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotelbooking.auth.entity.Roles;
import com.hotelbooking.auth.entity.Users;
import com.hotelbooking.auth.repository.RoleRepo;
import com.hotelbooking.auth.service.UserService;

//... imports for Users, Roles, UserService, PasswordEncoder ...

@Configuration
public class CreateSuperAdmin{
 
 // Define super admin credentials (use environment variables in real life)
	@Value("${admin.initial.email:}") // Use a default empty value if not set
    private String adminEmail;
    
    @Value("${admin.initial.password:}")
    private String adminPassword;

 @Bean
 public CommandLineRunner initializeSuperAdmin(UserService userService, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
     return args -> {
         
         // Skip if user already exists
         if (userService.findByEmail(adminEmail).isPresent()) {
             System.out.println("Super Admin already exists. Skipping initialization.");
             return;
         }

         // 1. Get the SUPER_ADMIN Role
         Roles adminRole = roleRepo.findByRole("ROLE_SUPER_ADMIN")
             .orElseThrow(() -> new RuntimeException("ROLE_SUPER_ADMIN missing!"));

         // 2. Create the User Entity
         Users admin = new Users();
         admin.setEmail(adminEmail);
         admin.setFullName("Super Administrator");
         admin.setAge(50);
         admin.setPhone("0000000000");
         admin.setPasswordHash(passwordEncoder.encode(adminPassword));

         // 3. Assign Role and Save
         admin.setRoles(Set.of(adminRole));
         userService.save(admin); // Assuming you add a save method to your UserService
         
         System.out.println("Super Admin successfully created with email: " + adminEmail);
     };
 }
}