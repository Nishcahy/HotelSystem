package com.hotelbooking.auth.service;



import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hotelbooking.auth.repository.UserRepo; // Assuming your user repo is named this
import lombok.RequiredArgsConstructor;

// ⭐️ NEW SERVICE: Connects DB to Spring Security
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    // You need a UserRepository interface defined (e.g., UserRepository extends JpaRepository<Users, Long>)
    private final UserRepo userRepository; 

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Fetch the user by email (which is the 'username' for Spring Security)
        return userRepository.findByEmail(email)
            // 2. Throw the required exception if not found
            .orElseThrow(() -> 
                new UsernameNotFoundException("User not found with email: " + email)
            );
    }
}