package common.loginapiserver.oauth.jwtAuthorizationFilter;

import common.loginapiserver.security.oauth2.JwtAuthorizationFilter;
import common.loginapiserver.security.oauth2.OAuthTokenInfoService;
import common.loginapiserver.security.oauth2.UsernameAuthenticationToken;
import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.oauth2.jwt.OAuthTokenInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.BDDMockito.*;
@ExtendWith(MockitoExtension.class)
public class JwtAuthorizationFilterUnitTest {
    JwtAuthorizationFilter jwtAuthorizationFilter;
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    OAuthTokenInfoService oAuthTokenInfoService;

    Key secretKey;

    @BeforeEach
    void setSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode("aaaaaaaaaaaaaaaaaaaaaabbbaaaaaaaaabbbbbbbaaaaaaaaaaaaaaaabbbbbbbbb");
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }
    @Test
    @DisplayName("1. 유효한 accessToken")
    void validAccessToken(@Mock HttpServletRequest req, @Mock HttpServletResponse res, @Mock FilterChain chain) throws ServletException, IOException {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessTokenExpirationDate = Date.from(localDateTime.plusHours(1L).
                                                    atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTokenExpirationDate = Date.from(localDateTime.plusHours(3L).
                                                    atZone(ZoneId.systemDefault()).toInstant());
        String accessToken = getAccessToken(accessTokenExpirationDate);
        String refreshToken = getRefreshToken(refreshTokenExpirationDate);
        Authentication authentication = new UsernameAuthenticationToken("testName", null);
        given(req.getCookies())
                .willReturn(new Cookie[]{new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken)});
        given(jwtTokenProvider.getAuthentication(accessToken))
                .willReturn(authentication);
        given(jwtTokenProvider.isExpired(accessToken))
                .willReturn(false);
        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtTokenProvider, oAuthTokenInfoService);
        doNothing().when(chain).doFilter(req, res);
        // when
        jwtAuthorizationFilter.doFilterInternal(req, res, chain);

        //then
        Authentication securityContext = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertEquals(authentication.getPrincipal(), securityContext.getPrincipal());
    }
    @Test
    @DisplayName("2. 만료된 accessToken & 유효한 RefreshToken: accessToken 갱신")
    void expiredAccessTokenAndValidRefreshToken(@Mock HttpServletRequest req, @Mock HttpServletResponse res, @Mock FilterChain chain) {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessTokenExpirationDate = Date.from(localDateTime.
                atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTokenExpirationDate = Date.from(localDateTime.plusHours(3L).
                atZone(ZoneId.systemDefault()).toInstant());
        String accessToken = getAccessToken(accessTokenExpirationDate);
        String refreshToken = getRefreshToken(refreshTokenExpirationDate);
        OAuthTokenInfo oAuthTokenInfo = new OAuthTokenInfo(accessToken, refreshToken);
        Authentication authentication = new UsernameAuthenticationToken("testName", null);
        given(req.getCookies())
                .willReturn(new Cookie[]{new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken)});
        given(jwtTokenProvider.isExpired(accessToken))
                .willReturn(true);
        given(oAuthTokenInfoService.findOAuthTokenInfo(accessToken))
                .willReturn(oAuthTokenInfo);
        given(jwtTokenProvider.getAuthentication(accessToken))
                .willReturn(authentication);
    }
    @Test
    @DisplayName("3. 만료된 accessToken & 만료된 RefreshToken: throw RuntimeException")
    void expiredAccessTokenAndRefreshToken() {

    }

    private String getAccessToken(Date expireDate) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setSubject("testName")
                .claim("auth", "ROLE_USER")
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return accessToken;
    }
    private String getRefreshToken(Date expireDate) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return refreshToken;
    }

}
