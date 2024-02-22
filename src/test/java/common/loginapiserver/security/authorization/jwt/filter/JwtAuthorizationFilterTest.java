package common.loginapiserver.security.authorization.jwt.filter;

import common.loginapiserver.common.MockAuthentication;
import common.loginapiserver.common.MockExpirationUtils;
import common.loginapiserver.common.MockOAuthTokenRepository;
import common.loginapiserver.security.authorization.jwt.filter.port.TokenService;
import common.loginapiserver.security.authorization.jwt.domain.MemberJwtDetails;
import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.OAuthTokenService;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import common.loginapiserver.security.authorization.jwt.utils.ExpirationUtils;
import common.loginapiserver.security.authorization.jwt.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class JwtAuthorizationFilterTest {
    static OAuthTokenRepository oAuthTokenRepository;
    static TokenService tokenService;
    static JwtAuthorizationFilter jwtAuthorizationFilter;
    @BeforeAll
    static void init() {
        oAuthTokenRepository = new MockOAuthTokenRepository();
        tokenService = new OAuthTokenService(oAuthTokenRepository);
    }

    @Test
    void http_request에_JWT토큰_쿠키가_없으면_filter를_통과한다(@Mock FilterChain filterChain) throws ServletException, IOException {
        // given
        String mockSecretKey = "sdfasd231ffesdgxdfg1234avasvasafd18699fadas3439";
        ExpirationUtils expirationUtils = new MockExpirationUtils(10 * 1000L, 10 * 1000L);
        JwtUtils jwtUtils = new JwtUtils(mockSecretKey, expirationUtils);
        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtils, tokenService);

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // then
        BDDMockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void http_request에_JWT가_존재하면_이를_파싱해서_securityContext에_저장한다(@Mock HttpServletRequest request, @Mock FilterChain filterChain) throws ServletException, IOException {
        // given
        String mockSecretKey = "sdfasd231ffesdgxdfg1234avasvasafd18699fadas3439";
        ExpirationUtils expirationUtils = new MockExpirationUtils(10 * 1000L, 10 * 1000L);
        JwtUtils jwtUtils = new JwtUtils(mockSecretKey, expirationUtils);

        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(new MockAuthentication("member1", Arrays.asList("ROLE_USER")));
        String accessToken = memberJwtDetails.getAccessToken();
        Cookie cookie = new Cookie(JwtUtils.ACCESS_TOKEN_KEY, accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        Cookie[] cookies = Arrays.asList(cookie).stream().toArray(Cookie[]::new);
        BDDMockito.given(request.getCookies()).willReturn(cookies);
        HttpServletResponse response = new MockHttpServletResponse();

        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtils, tokenService);
        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);
        // then
        BDDMockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
        Assertions.assertEquals(SecurityContextHolder.getContext().getAuthentication().getName(), "member1");
        Assertions.assertEquals(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")), true);
    }

    @Test
    void accessToken이_만료되고_refreshToken이_정상인_경우_AccessToken을_재발급하여_securityContext에_저장한다(@Mock HttpServletRequest request, @Mock FilterChain filterChain) throws ServletException, IOException {
        // given
        String mockSecretKey = "sdfasd231ffesdgxdfg1234avasvasafd18699fadas3439";
        ExpirationUtils expirationUtils = new MockExpirationUtils(0 * 1000L, 15 * 1000L);
        JwtUtils jwtUtils = new JwtUtils(mockSecretKey, expirationUtils);

        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(new MockAuthentication("member2", Arrays.asList("ROLE_USER")));
        String accessToken = memberJwtDetails.getAccessToken();
        String refreshToken = memberJwtDetails.getRefreshToken();
        OAuthToken saved = oAuthTokenRepository.save(OAuthToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build());
        Cookie cookie = new Cookie(JwtUtils.ACCESS_TOKEN_KEY, accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        Cookie[] cookies = Arrays.asList(cookie).stream().toArray(Cookie[]::new);
        BDDMockito.given(request.getCookies()).willReturn(cookies);
        HttpServletResponse response = new MockHttpServletResponse();

        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtils, tokenService);
        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // then
        BDDMockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
        Assertions.assertEquals(SecurityContextHolder.getContext().getAuthentication().getName(), "member2");
        Assertions.assertEquals(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")), true);
        Assertions.assertNotEquals(oAuthTokenRepository.findById(saved.getId()), Optional.empty());
    }

    @Test
    void accessToken와_refreshToken_모두_만료인_경우_Exception이_발생한다(@Mock HttpServletRequest request, @Mock FilterChain filterChain) throws ServletException, IOException {
        // given
        String mockSecretKey = "sdfasd231ffesdgxdfg1234avasvasafd18699fadas3439";
        ExpirationUtils expirationUtils = new MockExpirationUtils(0 * 1000L, 0 * 1000L);
        JwtUtils jwtUtils = new JwtUtils(mockSecretKey, expirationUtils);

        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(new MockAuthentication("member3", Arrays.asList("ROLE_USER")));
        String accessToken = memberJwtDetails.getAccessToken();
        String refreshToken = memberJwtDetails.getRefreshToken();
        OAuthToken saved = oAuthTokenRepository.save(OAuthToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
        Cookie cookie = new Cookie(JwtUtils.ACCESS_TOKEN_KEY, accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        Cookie[] cookies = Arrays.asList(cookie).stream().toArray(Cookie[]::new);
        BDDMockito.given(request.getCookies()).willReturn(cookies);
        HttpServletResponse response = new MockHttpServletResponse();

        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtils, tokenService);
        // when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);
        // then
        BDDMockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
        Assertions.assertEquals(oAuthTokenRepository.findById(saved.getId()), Optional.empty());
    }


}
