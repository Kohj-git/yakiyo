package com.yakiyo.user.service;

import com.yakiyo.user.domain.User;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException
    {
        log.info("구글에서 받아온 UserRequest: " + oAuth2UserRequest);

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        log.info("oauth에서 받아온 정보 : "+ oAuth2User.getAttributes());

        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");
        String googleId = (String) oAuth2User.getAttributes().get("sub");

        userRepo.findByEmail(email)
                .orElseGet(() -> userRepo.save(
                        User.builder()
                                .email(email)
                                .name(name)
                                .nickname(name)
                                .Id(googleId)
                                .build()
                ));
        return oAuth2User;

    }


}
