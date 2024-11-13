package common.loginapiserver.security.authorization.jwt.utils;

import common.loginapiserver.security.authentication.oauth.domain.UsernameAuthenticationToken;
import common.loginapiserver.security.authorization.jwt.domain.MemberJwtDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    public static final String ACCESS_TOKEN_KEY = "JWT";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    private final ExpirationUtils expirationUtils;
    private final Key key;
    @Autowired
    public JwtUtils(@Value("${oauth2.jwt.secret}") String secretKey, ExpirationUtils expirationUtils) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationUtils = expirationUtils;
    }
    public MemberJwtDetails generateJwtToken(Authentication authentication) {
        String name = authentication.getName();
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.joining(","));

        long accessTokenExpirationMillis = expirationUtils.getAccessTokenExpirationMillis();
        long refreshTokenExpirationMillis = expirationUtils.getRefreshTokenExpirationMillis();
        Date now = expirationUtils.now();
        String accessToken = Jwts.builder()
                .setHeaderParam("alg", SignatureAlgorithm.HS256)
                .setSubject(name)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        String refreshToken = Jwts.builder()
                .setHeaderParam("alg", SignatureAlgorithm.HS256)
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return MemberJwtDetails.builder()
                .accessToken(accessToken)
                .accessTokenExpirationTime(accessTokenExpirationMillis)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(refreshTokenExpirationMillis)
                .build();
    }
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("AUTHORITIES NOT FOUND IN TOKEN");
        }

        // 클레임에서 권한 정보를 추출하여 RETURN
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map((auth) -> new SimpleGrantedAuthority(auth))
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernameAuthenticationToken(principal, authorities);
    }
    public boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        Date expirationDate = claims.getExpiration();
        if(expirationDate.after(expirationUtils.now())) {
            return false;
        }
        return true;
    }
    private Claims parseClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
