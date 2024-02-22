package common.loginapiserver.common;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.security.authentication.oauth.utils.OAuth2RequestUtils;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class MockOAuth2RequestUtils implements OAuth2RequestUtils {
    private final Member member;
    private final List<String> authorities;

    public MockOAuth2RequestUtils(Member member, List<String> authorities) {
        this.member = member;
        this.authorities = authorities;
    }

    @Override
    public OAuth2User getOAuthUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Set<GrantedAuthority> authoritySet = new HashSet<>();
        Map<String, Object> attributes = new HashMap<>();
        String nameAttributeKey = "login_id";

        // login_id 설정
        attributes.put(nameAttributeKey, member.getLoginId());
        // 권한 설정
        authorities.forEach(authority -> {
            authoritySet.add(new SimpleGrantedAuthority(authority));
        });
        // 기타 attribute 설정
        Map<String, Object> kakaoAccount = new HashMap<>();
        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", member.getNickname());
        kakaoAccount.put("profile", profile);
        kakaoAccount.put("email", member.getEmail());
        attributes.put("kakao_account", kakaoAccount);

        return new DefaultOAuth2User(authoritySet, attributes, nameAttributeKey);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
        return null;
    }
}
