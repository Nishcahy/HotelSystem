package com.hotelbooking.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotelbooking.auth.entity.Roles;
import com.hotelbooking.auth.repository.RoleRepo;

@Configuration
public class RoleInitialiserConfig {

	// This bean runs after the application context is loaded
	@Bean
	public CommandLineRunner initializeRoles(RoleRepo roleRepository) {
		return args -> {

			// Check if ROLE_CUSTOMER exists
			if (roleRepository.findByRole("ROLE_CUSTOMER").isEmpty()) {
				Roles customerRole = new Roles();
				customerRole.setRole("ROLE_CUSTOMER");
				roleRepository.save(customerRole);
				System.out.println("Role 'ROLE_CUSTOMER' initialized.");
			}
			if (roleRepository.findByRole("ROLE_HOTEL_OWNER").isEmpty()) {
	            Roles ownerRole = new Roles();
	            ownerRole.setRole("ROLE_HOTEL_OWNER");
	            roleRepository.save(ownerRole);
	            System.out.println("Role 'ROLE_HOTEL_OWNER' initialized.");
	        }
	        
	        // Check if ROLE_SUPER_ADMIN exists
	        if (roleRepository.findByRole("ROLE_SUPER_ADMIN").isEmpty()) {
	            Roles superAdminRole = new Roles();
	            superAdminRole.setRole("ROLE_SUPER_ADMIN");
	            roleRepository.save(superAdminRole);
	            System.out.println("Role 'ROLE_SUPER_ADMIN' initialized.");
	        }
	    };
	   
	}
}
