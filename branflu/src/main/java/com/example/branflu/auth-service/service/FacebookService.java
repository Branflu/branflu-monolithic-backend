package com.example.branflu.service;

import com.example.branflu.entity.FacebookUser;
import com.example.branflu.repository.FacebookUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FacebookService {

    private final FacebookUserRepository facebookUserRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${facebook.client.id}")
    private String clientId;

    @Value("${facebook.client.secret}")
    private String clientSecret;

    @Value("${FACEBOOK_REDIRECT_URI}")
    private String redirectUri;

    public String getFacebookAccessToken(String code) {
        String tokenUrl = "https://graph.facebook.com/v18.0/oauth/access_token"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&client_secret=" + clientSecret
                + "&code=" + code;

        ResponseEntity<Map> response = restTemplate.getForEntity(tokenUrl, Map.class);
        return (String) response.getBody().get("access_token");
    }

    public FacebookUser fetchAndSaveUser(String accessToken) {
        String url = "https://graph.facebook.com/me"
                + "?fields=id,name,email,picture"
                + "&access_token=" + accessToken;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> userData = response.getBody();

        String userId = (String) userData.get("id");
        String name = (String) userData.get("name");
        String email = (String) userData.get("email");

        String profilePictureUrl = "";
        if (userData.containsKey("picture")) {
            Map pictureData = (Map) ((Map) userData.get("picture")).get("data");
            profilePictureUrl = (String) pictureData.get("url");
        }

        FacebookUser user = facebookUserRepository.findByFacebookUserId(userId)
                .orElse(FacebookUser.builder()
                        .facebookUserId(userId)
                        .createdAt(LocalDateTime.now())
                        .build());

        user.setName(name);
        user.setEmail(email);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setAccessToken(accessToken);

        Integer facebookFollowersCount = fetchFacebookFollowerCount(accessToken);
        Integer instagramFollowersCount = fetchInstagramDetailsAndSave(accessToken, user);

        user.setFacebookFollowersCount(facebookFollowersCount);
        user.setInstagramFollowersCount(instagramFollowersCount);
        user.setUpdatedAt(LocalDateTime.now());

        return facebookUserRepository.save(user);
    }

    private Integer fetchFacebookFollowerCount(String accessToken) {
        try {
            String pagesUrl = "https://graph.facebook.com/me/accounts?access_token=" + accessToken;
            Map pages = restTemplate.getForObject(pagesUrl, Map.class);
            if (pages != null && pages.containsKey("data")) {
                var pagesList = (java.util.List<Map<String, Object>>) pages.get("data");
                if (!pagesList.isEmpty()) {
                    String pageId = (String) pagesList.get(0).get("id");
                    String pageAccessToken = (String) pagesList.get(0).get("access_token");

                    String followerUrl = "https://graph.facebook.com/" + pageId
                            + "?fields=followers_count&access_token=" + pageAccessToken;
                    Map result = restTemplate.getForObject(followerUrl, Map.class);
                    if (result != null && result.containsKey("followers_count")) {
                        return (Integer) result.get("followers_count");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer fetchInstagramDetailsAndSave(String accessToken, FacebookUser user) {
        try {
            String pagesUrl = "https://graph.facebook.com/me/accounts?access_token=" + accessToken;
            Map pages = restTemplate.getForObject(pagesUrl, Map.class);

            if (pages != null && pages.containsKey("data")) {
                var pagesList = (java.util.List<Map<String, Object>>) pages.get("data");

                if (!pagesList.isEmpty()) {
                    String pageId = (String) pagesList.get(0).get("id");
                    String pageAccessToken = (String) pagesList.get(0).get("access_token");

                    String instaAccountUrl = "https://graph.facebook.com/" + pageId
                            + "?fields=instagram_business_account&access_token=" + pageAccessToken;
                    Map pageDetails = restTemplate.getForObject(instaAccountUrl, Map.class);

                    if (pageDetails != null && pageDetails.containsKey("instagram_business_account")) {
                        Map instaData = (Map) pageDetails.get("instagram_business_account");
                        String instagramId = (String) instaData.get("id");

                        String profileUrl = "https://graph.facebook.com/" + instagramId
                                + "?fields=username,profile_picture_url,biography,followers_count,media_count"
                                + "&access_token=" + pageAccessToken;

                        Map result = restTemplate.getForObject(profileUrl, Map.class);
                        if (result != null) {
                            user.setInstagramUsername((String) result.get("username"));
                            user.setInstagramProfilePictureUrl((String) result.get("profile_picture_url"));
                            user.setInstagramBio((String) result.get("biography"));
                            user.setInstagramFollowersCount((Integer) result.get("followers_count"));
                            user.setInstagramMediaCount((Integer) result.get("media_count"));

                            return (Integer) result.get("followers_count");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
