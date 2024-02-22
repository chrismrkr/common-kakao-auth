package common.loginapiserver.medium.auth.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthorizationTest {
//                .setIssuedAt(now)
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    OAuthTokenInfoService oAuthTokenInfoService;
//    @Autowired
//    JwtTokenProvider jwtTokenProvider;
//    @Value("${oauth2.jwt.secret}")
//    String secretKey;
//    @Test
//    @DisplayName("[Authorization Success] Valid AccessToken")
//    void success() throws Exception {
//        // given : accessToken 생성
//        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
//                .loginId("test1")
//                .password("1234")
//                .authType("NORMAL")
//                .build();
//        MemberEntity savedMemberEntity = memberService.createMember(memberRequestDto);
//        MemberContext memberContext = (MemberContext)memberService.loadMemberByLoginId(savedMemberEntity.getLoginId());
//        ApiAuthenticationToken authenticationToken = new ApiAuthenticationToken(savedMemberEntity.getLoginId(), null, memberContext.getAuthorities());
//        MemberJwtTokenInfo memberJwtTokenInfo = jwtTokenProvider.generateJwtToken(authenticationToken);
//
//        String accessToken = memberJwtTokenInfo.getAccessToken(); // encode (test1, ROLE_USER)
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/test")
//                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        // then
//        MockHttpServletResponse response = perform.andReturn().getResponse();
//        log.info("[response] {}", response.getContentAsString());
//    }
//
//    @Test
//    @DisplayName("[Authorization Success] expired AccessToken & valid RefreshToken")
//    void success2() throws Exception {
//        // given
//        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
//                .loginId("test2")
//                .password("1234")
//                .authType("NORMAL")
//                .build();
//        MemberEntity savedMemberEntity = memberService.createMember(memberRequestDto);
//        MemberContext memberContext = (MemberContext)memberService.loadMemberByLoginId(savedMemberEntity.getLoginId());
//        ApiAuthenticationToken authentication = new ApiAuthenticationToken(savedMemberEntity.getLoginId(), null, memberContext.getAuthorities());
//
//        String name = authentication.getName();
//        String authorities = authentication.getAuthorities()
//                .stream()
//                .filter((authority) -> authority.getAuthority().startsWith("ROLE_"))
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
//
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        Key key = Keys.hmacShaKeyFor(keyBytes);
//        String accessToken = Jwts.builder() // expired
//                .setSubject(name)
//                .claim("auth", authorities)
//                .claim("type", "access")
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime()))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//        String refreshToken = Jwts.builder() // not expired
//                .claim("type", "refresh")
//                .setExpiration(new Date(now.getTime() + 30 * 60 * 1000L))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//        oAuthTokenInfoService.saveOAuthTokenInfo(accessToken, refreshToken);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/test")
//                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        // then
//        MockHttpServletResponse response = perform.andReturn().getResponse();
//        log.info("[response] {}", response.getContentAsString());
//    }
//
//    @Test
//    @DisplayName("[Authorization Fail] expired AccessToken & expired RefreshToken")
//    void fail1() throws Exception {
//        // given
//        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
//                .loginId("test3")
//                .password("1234")
//                .authType("NORMAL")
//                .build();
//        MemberEntity savedMemberEntity = memberService.createMember(memberRequestDto);
//        MemberContext memberContext = (MemberContext)memberService.loadMemberByLoginId(savedMemberEntity.getLoginId());
//        ApiAuthenticationToken authentication = new ApiAuthenticationToken(savedMemberEntity.getLoginId(), null, memberContext.getAuthorities());
//
//        String name = authentication.getName();
//        String authorities = authentication.getAuthorities()
//                .stream()
//                .filter((authority) -> authority.getAuthority().startsWith("ROLE_"))
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
//
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        Key key = Keys.hmacShaKeyFor(keyBytes);
//        String accessToken = Jwts.builder() // expired
//                .setSubject(name)
//                .claim("auth", authorities)
//                .claim("type", "access")
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime()))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//        String refreshToken = Jwts.builder() // expired
//                .claim("type", "refresh")
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime()))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//        oAuthTokenInfoService.saveOAuthTokenInfo(accessToken, refreshToken);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/test")
//                        .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        // then
//        MockHttpServletResponse response = perform.andReturn().getResponse();
//        log.info("[response] {}", response.getContentAsString());

//    }
}
