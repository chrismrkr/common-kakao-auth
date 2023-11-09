package common.loginapiserver.oauth.jwtAuthorizationFilter;

import common.loginapiserver.security.oauth2.OAuthTokenInfoRepository;
import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.oauth2.jwt.OAuthTokenInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class
JwtAuthorizationIntegrateTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    OAuthTokenInfoRepository oAuthTokenInfoRepository;
    @Autowired
    EntityManager em;
    Key secretKey;
    @BeforeEach
    void setSecretKey(@Value("${oauth2.jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    @DisplayName("1. 유효한 AccessToken")
    void validAccessToken() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessTokenExpiration = Date.from(localDateTime.plusHours(1L).atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTokenExpiration = Date.from(localDateTime.plusHours(3L).atZone(ZoneId.systemDefault()).toInstant());
        String accessToken = getAccessToken(accessTokenExpiration);
        String refreshToken = getRefreshToken(refreshTokenExpiration);
        oAuthTokenInfoRepository.save(new OAuthTokenInfo(accessToken, refreshToken));

        // when
        ResultActions perform = mockMvc.perform(get("/data")
                .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        Optional<OAuthTokenInfo> byAccessToken = oAuthTokenInfoRepository.findByAccessToken(accessToken);
        Assertions.assertNotNull(byAccessToken);
    }

    @Test
    @DisplayName("2. 만료된 AccessToken & 유효한 RefreshToken")
    @Transactional
    void expiredAccessTokenAndValidRefreshToken() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessTokenExpiration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTokenExpiration = Date.from(localDateTime.plusHours(3L).atZone(ZoneId.systemDefault()).toInstant());
        String accessToken = getAccessToken(accessTokenExpiration);
        String refreshToken = getRefreshToken(refreshTokenExpiration);
        oAuthTokenInfoRepository.save(new OAuthTokenInfo(accessToken, refreshToken));

        // when
        ResultActions perform = mockMvc.perform(get("/data")
                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        em.flush();
        em.clear();
        Optional<OAuthTokenInfo> byAccessToken = oAuthTokenInfoRepository.findByAccessToken(accessToken);
        Assertions.assertEquals(Optional.empty(), byAccessToken);
    }

    @Test
    @DisplayName("3. 만료된 AccessToken & 만료된 RefreshToken")
    void expiredAccessTokenAndRefreshToken() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessTokenExpiration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTokenExpiration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String accessToken = getAccessToken(accessTokenExpiration);
        String refreshToken = getRefreshToken(refreshTokenExpiration);
        oAuthTokenInfoRepository.save(new OAuthTokenInfo(accessToken, refreshToken));

        // when
        ResultActions perform = mockMvc.perform(get("/data")
                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        // then
        String redirectedUrl = perform.andReturn().getResponse().getRedirectedUrl();
        log.info("[Redirect URL] {}", redirectedUrl);
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
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
