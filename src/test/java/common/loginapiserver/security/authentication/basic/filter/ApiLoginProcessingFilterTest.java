package common.loginapiserver.security.authentication.basic.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.loginapiserver.common.*;
import common.loginapiserver.common.utils.PasswordEncoderUtils;
import common.loginapiserver.member.controller.dto.LoginRequestDto;
import common.loginapiserver.member.controller.port.MemberService;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import common.loginapiserver.member.service.MemberServiceImpl;
import common.loginapiserver.member.service.port.MemberRepository;
import common.loginapiserver.member.service.port.MemberRoleRepository;
import common.loginapiserver.role.service.port.RoleRepository;
import common.loginapiserver.security.authentication.basic.filter.ApiLoginProcessingFilter;
import common.loginapiserver.security.authentication.basic.provider.ApiAuthenticationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import java.io.IOException;

public class ApiLoginProcessingFilterTest {
    static ApiLoginProcessingFilter apiLoginProcessingFilter;
    static AuthenticationManager mockAuthenticationManager;
    static AuthenticationProvider apiAuthenticationProvider;
    static MemberService memberService;
    static MemberRepository memberRepository;
    static MemberRoleRepository memberRoleRepository;
    static RoleRepository roleRepository;
    static PasswordEncoderUtils passwordEncoderUtils;
    @BeforeAll
    static void init() {
        memberRepository = new MockMemberRepository();
        memberRoleRepository = new MockMemberRoleRepository();
        roleRepository = new MockRoleRepository();
        passwordEncoderUtils = new PasswordEncoderUtils();
        memberService = new MemberServiceImpl(memberRepository, memberRoleRepository, roleRepository, passwordEncoderUtils);
        apiAuthenticationProvider = new ApiAuthenticationProvider(passwordEncoderUtils, memberService);
        mockAuthenticationManager = new MockAuthenticationManager(apiAuthenticationProvider);
        apiLoginProcessingFilter = new ApiLoginProcessingFilter("/api/login");
    }

    @Test
    void loginRequestDto를_처리하여_authenticationToken을_반환한다() throws IOException, ServletException {
        // given
        apiLoginProcessingFilter.setAuthenticationManager(mockAuthenticationManager);
        memberService.createMember(
                Member.builder()
                .loginId("member")
                .password("12345678")
                .nickname("member-nick")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build());
        // when
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        byte[] members = objectMapper.writeValueAsBytes(new LoginRequestDto("member", "12345678"));
        httpServletRequest.setContent(members);
        Authentication authentication = apiLoginProcessingFilter.attemptAuthentication(httpServletRequest, new MockHttpServletResponse());

        // then
        Assertions.assertEquals(authentication.getPrincipal(), "member");
        Assertions.assertEquals(authentication.getAuthorities().size(), 1);
    }

    @Test
    void 잘못된_비밀번호를_입력하면_Exception을_반환한다() throws IOException, ServletException {
        // given
        apiLoginProcessingFilter.setAuthenticationManager(mockAuthenticationManager);
        memberService.createMember(
                Member.builder()
                        .loginId("member2")
                        .password("12345678")
                        .nickname("member-nick")
                        .email("email@email.com")
                        .phoneNumber("01012341234")
                        .authType(AuthType.NORMAL)
                        .build());
        // when
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        byte[] members = objectMapper.writeValueAsBytes(new LoginRequestDto("member2", "12344321"));
        httpServletRequest.setContent(members);

        // then
        Assertions.assertThrows(BadCredentialsException.class, () -> apiLoginProcessingFilter.attemptAuthentication(httpServletRequest, new MockHttpServletResponse()));
    }

    @Test
    void 로그인ID가_공백이면_Exception을_반환한다() throws JsonProcessingException {
        // given
        apiLoginProcessingFilter.setAuthenticationManager(mockAuthenticationManager);
        memberService.createMember(
                Member.builder()
                        .loginId("member3")
                        .password("12345678")
                        .nickname("member-nick")
                        .email("email@email.com")
                        .phoneNumber("01012341234")
                        .authType(AuthType.NORMAL)
                        .build());
        // when
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        byte[] members = objectMapper.writeValueAsBytes(new LoginRequestDto("", "12344321"));
        httpServletRequest.setContent(members);

        // then
        Assertions.assertThrows(BadCredentialsException.class, () -> apiLoginProcessingFilter.attemptAuthentication(httpServletRequest, new MockHttpServletResponse()));
    }

    @Test
    void 비밀번호가_공백이면_Exception을_반환한다() throws JsonProcessingException {
        // given
        apiLoginProcessingFilter.setAuthenticationManager(mockAuthenticationManager);
        memberService.createMember(
                Member.builder()
                        .loginId("member4")
                        .password("12345678")
                        .nickname("member-nick")
                        .email("email@email.com")
                        .phoneNumber("01012341234")
                        .authType(AuthType.NORMAL)
                        .build());
        // when
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        byte[] members = objectMapper.writeValueAsBytes(new LoginRequestDto("member4", ""));
        httpServletRequest.setContent(members);

        // then
        Assertions.assertThrows(BadCredentialsException.class, () -> apiLoginProcessingFilter.attemptAuthentication(httpServletRequest, new MockHttpServletResponse()));
    }
}
