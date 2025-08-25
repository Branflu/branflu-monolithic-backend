package com.example.branflu.security;


import com.example.branflu.entity.Business;
import com.example.branflu.entity.FacebookUser;
import com.example.branflu.entity.YoutubeInfluencer;
import com.example.branflu.repository.BusinessRepository;
import com.example.branflu.repository.FacebookUserRepository;
import com.example.branflu.repository.YoutubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;



import java.util.List;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private YoutubeRepository youtubeRepository;
    @Autowired
    private FacebookUserRepository facebookUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadInfluencerByChannelId(String channelId) throws UsernameNotFoundException {
        YoutubeInfluencer influencer = youtubeRepository.findByChannelId(channelId);
        if (influencer == null) {
            throw new UsernameNotFoundException("Influencer not found with channelId: " + channelId);
        }

        // Create UserDetails with some default role or based on influencer info
        return new User(
                influencer.getChannelId(),
                "", // No password since OAuth
                List.of(new SimpleGrantedAuthority("ROLE_INFLUENCER"))
        );

    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Business business = businessRepository.findBusinessByPayPalEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Business not found with email: " + email));

        return new User(
                business.getPayPalEmail(),
                business.getPassword(), // stored password (hashed)
                List.of(new SimpleGrantedAuthority("ROLE_" + business.getRole().name()))
        );
    }


    public UserDetails loadInfluencerByFacebookId(String facebookId) throws UsernameNotFoundException{
        FacebookUser facebookUser=facebookUserRepository.findByFacebookUserId(facebookId);
        if(facebookUser == null){
            throw new UsernameNotFoundException("Influencer not found with cannelId: " + facebookId);
        }
        return new User(
                facebookUser.getFacebookUserId(),
                "",
                List.of(new SimpleGrantedAuthority("ROLE_INFLUENCER"))
        );


    }
}
