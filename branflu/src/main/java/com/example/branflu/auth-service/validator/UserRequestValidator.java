
package com.example.branflu.validator;

import com.example.branflu.enums.ErrorData;
import com.example.branflu.exception.BadRequestException;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.request.LinkRequest;
import com.example.branflu.payload.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
@Slf4j
public class UserRequestValidator {

    private final EmailValidator emailValidator;
    private final LinkValidator linkValidator;

    public UserRequestValidator(EmailValidator emailValidator, LinkValidator linkValidator) {
        this.emailValidator = emailValidator;
        this.linkValidator = linkValidator;
    }

    /**
     * Validates an influencer request (name, email, and links)
     */
    public void validateInfluencer(InfluencerRequest influencerRequest) {
        log.info("{} >> validateInfluencer -> request: {}", getClass().getSimpleName(), influencerRequest);
        validateCommonFields(influencerRequest);
        validateLinks(influencerRequest);
    }

    /**
     * Validates a business request (name and email only)
     */
    public void validateBusiness(UserRequest businessRequest) {
        log.info("{} >> validateBusiness -> request: {}", getClass().getSimpleName(), businessRequest);
        validateCommonFields(businessRequest);
    }

    /**
     * Shared name + email validator
     */
    private void validateCommonFields(UserRequest userRequest) {
        validateName(userRequest);
        validateEmail(userRequest);
    }

    private void validateName(UserRequest userRequest) {
        if (ObjectUtils.isEmpty(userRequest.getName())) {
            throw new BadRequestException(ErrorData.NAME_MANDATORY);
        }
        if (userRequest.getName().length() > 100) {
            throw new BadRequestException(ErrorData.NAME_LIMIT_EXCEED);
        }
    }

    private void validateEmail(UserRequest userRequest) {
        if (!ObjectUtils.isEmpty(userRequest.getPayPalEmail())) {
            emailValidator.isValidEmail(
                    userRequest.getPayPalEmail(),
                    () -> new BadRequestException(ErrorData.PAYPAL_EMAIL_INVALID)
            );
        }
    }

    /**
     * Validates each link in the influencer request
     */
    private void validateLinks(InfluencerRequest influencerRequest) {
        List<LinkRequest> links = influencerRequest.getLink();

        if (ObjectUtils.isEmpty(links)) {
            throw new BadRequestException(ErrorData.LINK_INVALID);
        }

        for (LinkRequest linkRequest : links) {
            String url = linkRequest.getUrl();
            log.info("Validating link: {}", url); // 👈 add this line
            linkValidator.isValidLink(url, () -> new BadRequestException(ErrorData.LINK_INVALID));
        }
    }



}
