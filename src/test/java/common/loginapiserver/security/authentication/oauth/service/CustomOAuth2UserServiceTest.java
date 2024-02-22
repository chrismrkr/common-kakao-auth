package common.loginapiserver.security.authentication.oauth.service;

import common.loginapiserver.common.MockMemberRepository;
import common.loginapiserver.common.MockMemberRoleRepository;
import common.loginapiserver.common.MockOAuth2RequestUtils;
import common.loginapiserver.common.MockRoleRepository;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import common.loginapiserver.member.service.port.MemberRepository;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.domain.Role;
import common.loginapiserver.role.service.port.RoleRepository;
import common.loginapiserver.security.authentication.oauth.utils.OAuth2RequestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CustomOAuth2UserServiceTest {
    static MemberRepository mockMemberRepository;
    static MemberRoleRepository mockMemberRoleRepository;
    static RoleRepository mockRoleRepository;
    static OAuth2RequestUtils mockOAuth2RequestUtils;
    static OAuth2UserService oAuth2UserService;
    @BeforeAll
    static void init() {
        mockMemberRepository = new MockMemberRepository();
        mockMemberRoleRepository = new MockMemberRoleRepository();
        mockRoleRepository = new MockRoleRepository();
    }

    @Test
    void 등록된_계정이_아닌_경우_새롭게_생성한다(@Mock OAuth2UserRequest userRequest) {
        // given
        List<String> authorities = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        Member member = Member.builder()
                .loginId("member1")
                .password("")
                .nickname("member1")
                .email("email1@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.KAKAO)
                .build();
        mockOAuth2RequestUtils = new MockOAuth2RequestUtils(member, authorities);
        oAuth2UserService = new CustomOAuth2UserService(
                mockMemberRepository,
                mockMemberRoleRepository,
                mockRoleRepository,
                mockOAuth2RequestUtils);
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("KAKAO")
                .authorizationGrantType(new AuthorizationGrantType("MOCK")).build();
        BDDMockito.given(userRequest.getClientRegistration()).willReturn(clientRegistration);
        // when
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        // then
        Assertions.assertNotNull(mockMemberRepository.findByLoginId(member.getLoginId()));
        Assertions.assertEquals(mockMemberRoleRepository.findByLoginId(member.getLoginId()).size(), 2);
        Assertions.assertEquals(mockMemberRoleRepository.findByLoginId(member.getLoginId())
                                .get(0).getRole().getRoleName(), "ROLE_ADMIN");
        Assertions.assertEquals(mockMemberRoleRepository.findByLoginId(member.getLoginId())
                .get(1).getRole().getRoleName(), "ROLE_USER");
    }

    @Test
    void 등록된_계정인_경우_회원_정보를_OAuth_정보와_동기화한다(@Mock OAuth2UserRequest userRequest) {
        // given

        Member member = Member.builder()
                .loginId("member2")
                .password("")
                .nickname("member2")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.KAKAO)
                .build();
        mockMemberRepository.save(member);
        Role role = Role.builder()
                .roleName("ROLE_USER")
                .build();
        mockRoleRepository.save(role);
        mockMemberRoleRepository.link(member, role);

        List<String> authorities = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        Member memberUpdate = Member.builder()
                .loginId("member2")
                .password("")
                .nickname("member2-update")
                .email("email-update@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.KAKAO)
                .build();
        mockOAuth2RequestUtils = new MockOAuth2RequestUtils(memberUpdate, authorities);
        oAuth2UserService = new CustomOAuth2UserService(
                mockMemberRepository,
                mockMemberRoleRepository,
                mockRoleRepository,
                mockOAuth2RequestUtils);
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("KAKAO")
                .authorizationGrantType(new AuthorizationGrantType("MOCK")).build();
        BDDMockito.given(userRequest.getClientRegistration()).willReturn(clientRegistration);
        // when
        Assertions.assertEquals(mockMemberRepository.findByLoginId(member.getLoginId()).get().getNickname(), member.getNickname());
        Assertions.assertEquals(mockMemberRepository.findByLoginId(member.getLoginId()).get().getEmail(), member.getEmail());
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        // then
        Assertions.assertNotNull(mockMemberRepository.findByLoginId(memberUpdate.getLoginId()));
        Assertions.assertEquals(mockMemberRepository.findByLoginId(memberUpdate.getLoginId()).get().getNickname(), memberUpdate.getNickname());
        Assertions.assertEquals(mockMemberRepository.findByLoginId(memberUpdate.getLoginId()).get().getEmail(), memberUpdate.getEmail());
    }

    @Test
    void 지원하지_않는_OAuthType은_Exception을_반환한다(@Mock OAuth2UserRequest userRequest) {
        // given
        List<String> authorities = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        Member member = Member.builder()
                .loginId("member3")
                .password("")
                .nickname("member3")
                .email("email3@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        mockOAuth2RequestUtils = new MockOAuth2RequestUtils(member, authorities);
        oAuth2UserService = new CustomOAuth2UserService(
                mockMemberRepository,
                mockMemberRoleRepository,
                mockRoleRepository,
                mockOAuth2RequestUtils);
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("NORMAL")
                .authorizationGrantType(new AuthorizationGrantType("MOCK")).build();
        BDDMockito.given(userRequest.getClientRegistration()).willReturn(clientRegistration);
        // when
        Assertions.assertThrows(IllegalArgumentException.class , () ->
        oAuth2UserService.loadUser(userRequest));
    }
}
