package com.example.branflu.validator;

import com.example.branflu.enums.ErrorData;
import com.example.branflu.exception.BadRequestException;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Component
@Slf4j
public class UserRequestValidator {
    private final EmailValidator emailValidator;
    private final LinkValidator linkValidator;

    public UserRequestValidator(EmailValidator emailValidator, LinkValidator linkValidator) {
        this.emailValidator = emailValidator;
        this.linkValidator = linkValidator;
    }
    public void validate(UserRequest userRequest,InfluencerRequest influencerRequest){
        log.info("{} >> validate -> profileRequest: {}",getClass().getSimpleName(),userRequest.toString());
        validateEmail(userRequest);
        validateName(userRequest);
        validateLink(influencerRequest);
    }
    private void validateName(UserRequest userRequest){
        if(Objects.isNull(userRequest.getName()) || ObjectUtils.isEmpty(userRequest.getName())){
            throw new BadRequestException(ErrorData.NAME_MANDATORY);
        }
        if(userRequest.getName().length() > 100){
            throw new BadRequestException(ErrorData.NAME_LIMIT_EXCEED);
        }

    }
    private void validateEmail(UserRequest userRequest){
        if(!ObjectUtils.isEmpty(userRequest.getPayPalEmail())){
            emailValidator.isValidEmail(
                    userRequest.getPayPalEmail(),
                    () -> new BadRequestException(ErrorData.PAYPAL_EMAIL_INVALID)
            );
        }
    }

    private void validateLink(InfluencerRequest influencerRequest){
        if (!ObjectUtils.isEmpty(influencerRequest.getLink())){
            linkValidator.isValidLink(
                    influencerRequest.getLink(),
                    () -> new BadRequestException(ErrorData.LINK_INVALID)
            );


        }
    }
}
