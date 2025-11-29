package com.no1project.reservation.security;

import com.no1project.reservation.model.SampleUser;
import com.no1project.reservation.repository.SampleUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SampleUserRepository userRepository;

    public CustomUserDetailsService(SampleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * username = email として扱う
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SampleUser user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new CustomUserDetails(user);
    }
}
