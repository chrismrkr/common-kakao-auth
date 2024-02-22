package common.loginapiserver.security.authentication.oauth.service;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.service.port.MemberRepository;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.domain.Role;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import common.loginapiserver.role.service.port.RoleRepository;
import common.loginapiserver.security.authentication.oauth.utils.OAuth2RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;
    private final OAuth2RequestUtils oAuth2RequestUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = oAuth2RequestUtils.getOAuthUser(userRequest);
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2User result = this.processOAuth2User(oAuth2User, oauthType);
        return result;
    }

    @Transactional
    private OAuth2User processOAuth2User(OAuth2User oAuth2User, String oAuthType) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(oAuth2User.getName().toString());
        if(optionalMember.isEmpty()) {
            createNewMember(oAuth2User, oAuthType);
        } else {
            Member presentMember = optionalMember.get();
            updateMemberInfo(presentMember, oAuth2User, oAuthType);
        }
        return oAuth2User;
    }

    private void updateMemberInfo(Member member, OAuth2User oAuth2User, String oAuthType) {
        if(oAuthType.equals("KAKAO")) {
            updateKakaoMember(member, oAuth2User);
        }
        else {
            throw new IllegalArgumentException("OAuthType not supported");
        }
    }
    private void createNewMember(OAuth2User oAuth2User, String oAuthType) {
        if(oAuthType.equals("KAKAO")) {
            createKakaoMember(oAuth2User);
        }
        else {
            throw new IllegalArgumentException("OAuthType not supported");
        }
    }
    private void updateKakaoMember(Member member, OAuth2User oAuth2User) {
        HashMap<String, Object> accountAttributes = oAuth2User.getAttribute("kakao_account");
        String nickname = ((HashMap<String, Object>)accountAttributes.get("profile")).get("nickname").toString();
        String email = accountAttributes.get("email").toString();
        // 1. Update Member
        member.updateNickname(nickname);
        member.updateEmail(email);

        // 2. Update Role: Role Mapping 삭제하고 다시 생성함(개선 필요)
        List<MemberRole> memberRoleList = memberRoleRepository.findByLoginId(oAuth2User.getName());
        memberRoleList.forEach(memberRole -> {
            memberRoleRepository.delete(memberRole);
        });

        oAuth2User.getAuthorities().forEach((attr) -> {
            String authority = attr.getAuthority();
            if(authority.startsWith("ROLE_")) {
                linkMemberWithRole(member, authority);
            }
        });
    }
    private void createKakaoMember(OAuth2User oAuth2User) {
        HashMap<String, Object> accountAttributes = oAuth2User.getAttribute("kakao_account");
        String nickname = ((HashMap<String, Object>)accountAttributes.get("profile")).get("nickname").toString();
        String email = accountAttributes.get("email").toString();
        // 1. Create new Member
        Member member = Member.builder()
                .loginId(oAuth2User.getName())
                .nickname(nickname)
                .email(email)
                .authType(AuthType.KAKAO)
                .build();
        Member savedMember = memberRepository.save(member);
        // 2. Map Member with Roles
        oAuth2User.getAuthorities().forEach((attr) -> {
            String authority = attr.getAuthority();
            if(authority.startsWith("ROLE_")) {
                linkMemberWithRole(savedMember, authority);
            }
        });
    }
    private void linkMemberWithRole(Member member, String authority) {
        Role role = roleRepository.getByRoleName(authority);
        memberRoleRepository.link(member, role);
    }
}
