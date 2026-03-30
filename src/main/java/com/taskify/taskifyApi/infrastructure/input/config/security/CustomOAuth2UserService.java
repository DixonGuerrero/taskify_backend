package com.taskify.taskifyApi.infrastructure.input.config.security;

import com.taskify.taskifyApi.application.service.AuthService;
import com.taskify.taskifyApi.application.service.UserService;
import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.domain.model.Role;
import com.taskify.taskifyApi.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthService authService;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = extractEmail(oAuth2User, registrationId);
        String name = extractName(oAuth2User, registrationId);

        User user = User.builder()
                .email(email)
                .username(email)
                .firstName(name)
                .lastName("")
                .role(Role.builder().name(RoleEnum.USER).build())
                .isEnabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        authService.processOAuth2User(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(
                userDetails.getAuthorities(),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }

    private String extractEmail(OAuth2User oAuth2User, String registrationId) {
        String email = oAuth2User.getAttribute("email");

        if ("github".equals(registrationId) && email == null) {
            email = oAuth2User.getAttribute("login");
        }

        return email;
    }

    private String extractName(OAuth2User oAuth2User, String registrationId) {
        String name = oAuth2User.getAttribute("name");

        if (name == null) {
            name = oAuth2User.getAttribute("login");
        }
        if (name == null && "google".equals(registrationId)) {
            name = oAuth2User.getAttribute("given_name");
        }

        return name != null ? name : "GitHub User";
    }
}