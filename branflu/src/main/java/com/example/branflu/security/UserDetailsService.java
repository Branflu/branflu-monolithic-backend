//package com.example.branflu.security;
//
//import com.example.branflu.entity.User;
//import com.example.branflu.repository.InfluencerRepository;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Service
//public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
//    private InfluencerRepository influencerRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user =this.influencerRepository.findInfluencerByPayPalEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"+username) );
//        return new org.springframework.security.core.userdetails.User(user.getPayPalEmail(), user.getPassword(), new ArrayList<>());
//    }
//}
