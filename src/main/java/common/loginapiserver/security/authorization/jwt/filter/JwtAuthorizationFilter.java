package common.loginapiserver.security.authorization.jwt.filter;

import common.loginapiserver.security.authorization.jwt.filter.port.TokenService;
import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.utils.JwtUtils;
import common.loginapiserver.security.authorization.jwt.domain.MemberJwtDetails;
import common.loginapiserver.common.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final TokenService oAuthTokenService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> cookie = CookieUtils.readServletCookie(request, JwtUtils.AUTHORIZATION_HEADER);
        if(!cookie.isEmpty()) { // AccessToken exists
            MemberJwtDetails memberJwtDetails = null;
            try {
                String urlDecoded = URLDecoder.decode(cookie.get(), StandardCharsets.UTF_8.toString());
                memberJwtDetails = MemberJwtDetails
                        .deserializeFromString(urlDecoded);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            String accessToken = memberJwtDetails.getAccessToken();
            String refreshToken = memberJwtDetails.getRefreshToken();
            if(isExpired(accessToken)) {
                try {
                    memberJwtDetails = renewJwtToken(accessToken, refreshToken);
                    CookieUtils.addCookie(response, JwtUtils.AUTHORIZATION_HEADER, memberJwtDetails.serializeToString(), 3600);
                } catch(Exception e) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            Authentication authentication = jwtUtils.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    private boolean isExpired(String token) {
        return jwtUtils.isExpired(token);
    }

    @Transactional
    private MemberJwtDetails renewJwtToken(String accessToken, String refreshToken) throws InterruptedException {
        OAuthToken oAuthToken = oAuthTokenService.findOAuthToken(refreshToken);
        oAuthTokenService.deleteOAuthToken(oAuthToken);
        if(isExpired(oAuthToken.getRefreshToken())) {
            throw new RuntimeException("REFRESH TOKEN EXPIRED");
        }
        Authentication authentication = jwtUtils.getAuthentication(accessToken);
        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(authentication);
        OAuthToken newOAuthToken = oAuthTokenService.saveOAuthToken(
                memberJwtDetails.getRefreshToken(), authentication.getPrincipal());
        return memberJwtDetails;
    }
}
