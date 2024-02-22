package common.loginapiserver.security.authorization.jwt.utils;

import common.loginapiserver.common.MockAuthentication;
import common.loginapiserver.common.MockExpirationUtils;
import common.loginapiserver.security.authorization.jwt.domain.MemberJwtDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;


public class JwtUtilsTest {
    static JwtUtils jwtUtils;
    static ExpirationUtils mockExpirationUtils;

    @BeforeAll
    static void init() {
        String mockSecretKey = "sdfasd231ffesdgxdfg1234avasvasafd18699fadas3439";
        mockExpirationUtils = new MockExpirationUtils(1000L, 2000L);
        jwtUtils = new JwtUtils(mockSecretKey, mockExpirationUtils);
    }

    @Test
    void authentication으로부터_JWT를_생성할_수_있다() {
        // given : name : member, authorites : [ROLE_USER]
        String name = "member";
        List<String> authorityList = Arrays.asList("ROLE_USER");
        Authentication authentication = new MockAuthentication(name, authorityList);

        // when
        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(authentication);

        // then
        String accessToken = memberJwtDetails.getAccessToken();
        String refreshToken = memberJwtDetails.getRefreshToken();
        Assertions.assertNotNull(accessToken);
        Assertions.assertNotNull(refreshToken);
    }

    @Test
    void JWT의_AccessToken에서_인증_및_인가_정보를_파싱할_수_있다() {
        // given : name : member, authorites : [ROLE_USER]
        String name = "member";
        List<String> authorityList = Arrays.asList("ROLE_USER");
        Authentication authentication = new MockAuthentication(name, authorityList);
        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(authentication);
        String accessToken = memberJwtDetails.getAccessToken();

        // when
        Authentication result = jwtUtils.getAuthentication(accessToken);

        // then
        Assertions.assertEquals(result.getName(), "member");
        Assertions.assertEquals(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")), true);
    }

    @Test
    void 유효기간이_지난_토큰을_파싱하면_Exception이_발생한다() {
        // given
        String mockSecretKey = "sdfasd231ffesdgxdfg1234avasvasafd18699fadas3439";
        ExpirationUtils ruinedExpirationUtils = new MockExpirationUtils(0L, 0L);
        JwtUtils ruinedjwtUtils = new JwtUtils(mockSecretKey, ruinedExpirationUtils);

        String name = "member";
        List<String> authorityList = Arrays.asList("ROLE_USER");
        Authentication authentication = new MockAuthentication(name, authorityList);
        MemberJwtDetails memberJwtDetails = ruinedjwtUtils.generateJwtToken(authentication);

        // when
        // then
        Assertions.assertTrue(ruinedjwtUtils.isExpired(memberJwtDetails.getAccessToken()));
    }

    @Test
    void 유효기간이_지나지_않은_토큰은_파싱할_수_있다() {
        // given
        String mockSecretKey = "sdfasd231ffesdgxdfg1234avasvasafd18699fadas3439";
        ExpirationUtils ruinedExpirationUtils = new MockExpirationUtils(10 * 1000L, 10 * 1000L);
        JwtUtils ruinedjwtUtils = new JwtUtils(mockSecretKey, ruinedExpirationUtils);

        String name = "member";
        List<String> authorityList = Arrays.asList("ROLE_USER");
        Authentication authentication = new MockAuthentication(name, authorityList);
        MemberJwtDetails memberJwtDetails = ruinedjwtUtils.generateJwtToken(authentication);

        // when
        // then
        Assertions.assertFalse(ruinedjwtUtils.isExpired(memberJwtDetails.getAccessToken()));
    }

}
