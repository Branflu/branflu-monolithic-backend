package com.example.branflu.security;

import com.example.branflu.entity.User;
import com.example.branflu.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =this.userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"+username) );
        return null;
    }
}
