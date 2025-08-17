package com.example.branflu.security;

import com.example.branflu.entity.User;
import com.example.branflu.entity.YoutubeInfluencer;
import com.example.branflu.repository.BusinessRepository;
import com.example.branflu.repository.InfluencerRepository;
import com.example.branflu.repository.YoutubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.List;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private YoutubeRepository youtubeRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = businessRepository.findBusinessByPayPalEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getPayPalEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
    public UserDetails loadInfluencerByChannelId(String channelId) throws UsernameNotFoundException {
        YoutubeInfluencer influencer = youtubeRepository.findByChannelId(channelId);
        if (influencer == null) {
            throw new UsernameNotFoundException("Influencer not found with channelId: " + channelId);
        }

        // Create UserDetails with some default role or based on influencer info
        return new org.springframework.security.core.userdetails.User(
                influencer.getChannelId(),
                "", // No password since OAuth
                List.of(new SimpleGrantedAuthority("ROLE_INFLUENCER"))
        );

}
}
