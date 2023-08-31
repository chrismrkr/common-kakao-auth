package common.loginapiserver.security.service;

import common.loginapiserver.entity.Member;
import common.loginapiserver.entity.embeddable.OAuth2Info;
import common.loginapiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        return this.processOAuth2User(oAuth2User, oauthType);
    }

    @Transactional
    private OAuth2User processOAuth2User(OAuth2User oAuth2User, String oAuthType) {
        OAuth2Info oAuth2Info = getOAuth2Info(oAuth2User, oAuthType);
        Optional<Member> optionalMember = memberRepository.findByOAuth2Id(oAuth2Info.getOAuth2Id());
        if(optionalMember.isEmpty()) {
            createNewMember(oAuth2Info);
        } else {
            Member presentMember = optionalMember.get();
            updateMemberOAuth2Info(presentMember, oAuth2Info);
        }
        return oAuth2User;
    }
    private void updateMemberOAuth2Info(Member member, OAuth2Info oAuth2Info) {
        member.updateOAuth2Info(oAuth2Info);
    }
    private void createNewMember(OAuth2Info oAuth2Info) {
        Member build = Member.builder().oAuth2Info(oAuth2Info).build();
        memberRepository.save(build);
    }
    private OAuth2Info getOAuth2Info(OAuth2User oAuth2User, String oAuthType) {
        return new OAuth2Info(oAuth2User.getAttributes(), oAuthType);
    }
}
