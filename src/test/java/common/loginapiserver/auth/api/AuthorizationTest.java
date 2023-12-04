package common.loginapiserver.auth.api;

import common.loginapiserver.domain.dto.MemberRequestDto;
import common.loginapiserver.domain.entity.Member;
import common.loginapiserver.security.oauth2.OAuthTokenInfoService;
import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.oauth2.jwt.MemberJwtTokenInfo;
import common.loginapiserver.security.oauth2.jwt.OAuthTokenInfo;
import common.loginapiserver.security.provider.MemberContext;
import common.loginapiserver.security.token.ApiAuthenticationToken;
import common.loginapiserver.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthorizationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    OAuthTokenInfoService oAuthTokenInfoService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Value("${oauth2.jwt.secret}")
    String secretKey;
    @Test
    @DisplayName("[Authorization Success] Valid AccessToken")
    void success() throws Exception {
        // given : accessToken 생성
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("test1")
                .password("1234")
                .authType("NORMAL")
                .build();
        Member savedMember = memberService.createMember(memberRequestDto);
        MemberContext memberContext = (MemberContext)memberService.loadMemberByLoginId(savedMember.getLoginId());
        ApiAuthenticationToken authenticationToken = new ApiAuthenticationToken(savedMember.getLoginId(), null, memberContext.getAuthorities());
        MemberJwtTokenInfo memberJwtTokenInfo = jwtTokenProvider.generateJwtToken(authenticationToken);

        String accessToken = memberJwtTokenInfo.getAccessToken(); // encode (test1, ROLE_USER)

        // when
        ResultActions perform = mockMvc.perform(get("/test")
                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[response] {}", response.getContentAsString());
    }

    @Test
    @DisplayName("[Authorization Success] expired AccessToken & valid RefreshToken")
    void success2() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("test2")
                .password("1234")
                .authType("NORMAL")
                .build();
        Member savedMember = memberService.createMember(memberRequestDto);
        MemberContext memberContext = (MemberContext)memberService.loadMemberByLoginId(savedMember.getLoginId());
        ApiAuthenticationToken authentication = new ApiAuthenticationToken(savedMember.getLoginId(), null, memberContext.getAuthorities());

        String name = authentication.getName();
        String authorities = authentication.getAuthorities()
                .stream()
                .filter((authority) -> authority.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        String accessToken = Jwts.builder() // expired
                .setSubject(name)
                .claim("auth", authorities)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        String refreshToken = Jwts.builder() // not expired
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 30 * 60 * 1000L))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        oAuthTokenInfoService.saveOAuthTokenInfo(accessToken, refreshToken);

        // when
        ResultActions perform = mockMvc.perform(get("/test")
                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[response] {}", response.getContentAsString());
    }

    @Test
    @DisplayName("[Authorization Fail] expired AccessToken & expired RefreshToken")
    void fail1() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("test3")
                .password("1234")
                .authType("NORMAL")
                .build();
        Member savedMember = memberService.createMember(memberRequestDto);
        MemberContext memberContext = (MemberContext)memberService.loadMemberByLoginId(savedMember.getLoginId());
        ApiAuthenticationToken authentication = new ApiAuthenticationToken(savedMember.getLoginId(), null, memberContext.getAuthorities());

        String name = authentication.getName();
        String authorities = authentication.getAuthorities()
                .stream()
                .filter((authority) -> authority.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        String accessToken = Jwts.builder() // expired
                .setSubject(name)
                .claim("auth", authorities)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        String refreshToken = Jwts.builder() // expired
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        oAuthTokenInfoService.saveOAuthTokenInfo(accessToken, refreshToken);

        // when
        ResultActions perform = mockMvc.perform(get("/test")
                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[response] {}", response.getContentAsString());

    }
}
