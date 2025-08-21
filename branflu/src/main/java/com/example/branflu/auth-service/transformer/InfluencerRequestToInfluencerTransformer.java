package com.example.branflu.transformer;

import com.example.branflu.entity.Influencer;
import com.example.branflu.enums.Role;
import com.example.branflu.payload.request.InfluencerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfluencerRequestToInfluencerTransformer {
    private final ModelMapper modelMapper;
    public Influencer transform(InfluencerRequest request, Influencer existingInfluencer) {
        modelMapper.map(request, existingInfluencer);
        existingInfluencer.setRole(Role.INFLUENCER); // fix here
        return existingInfluencer;
    }

    public Influencer transform(InfluencerRequest influencerRequest){
        return modelMapper.map(influencerRequest,Influencer.class);


    }

}
