package common.loginapiserver.security.oauth2;

import common.loginapiserver.dto.MemberRequestDto;
import common.loginapiserver.entity.Member;
import common.loginapiserver.entity.MemberRole;
import common.loginapiserver.entity.Role;
import common.loginapiserver.entity.embeddable.AuthInfo;
import common.loginapiserver.entity.enumerate.AuthType;
import common.loginapiserver.repository.MemberRepository;
import common.loginapiserver.repository.MemberRoleRepository;
import common.loginapiserver.repository.RoleRepository;
import common.loginapiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        return this.processOAuth2User(oAuth2User, oauthType);
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
    }
    private void createNewMember(OAuth2User oAuth2User, String oAuthType) {
        if(oAuthType.equals("KAKAO")) {
            createKakaoMember(oAuth2User);
        }
    }
    private void updateKakaoMember(Member member, OAuth2User oAuth2User) {
        HashMap<String, Object> accountAttributes = oAuth2User.getAttribute("kakao_account");
        String nickname = ((HashMap<String, Object>)accountAttributes.get("profile")).get("nickname").toString();
        String email = accountAttributes.get("email").toString();
        // 1. Update Member
        member.updateNickname(nickname);
        member.updateEmail(email);

        // 2. Update Role : continue...

        oAuth2User.getAuthorities().forEach((attr) -> {
            String authority = attr.getAuthority();
            if(authority.startsWith("ROLE_")) {

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
        Member saveMember = memberRepository.save(member);
        // 2. Map Member with Roles
        oAuth2User.getAuthorities().forEach((attr) -> {
            String authority = attr.getAuthority();
            if(authority.startsWith("ROLE_")) {
                Role role = roleRepository.findByRoleName(authority).orElseGet(() -> {
                    Role newRole = new Role(authority);
                    Role save = roleRepository.save(newRole);
                    return save;
                });
                MemberRole memberRole = new MemberRole(saveMember, role);
                memberRoleRepository.save(memberRole);
            }
        });
    }
}
