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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final TokenService oAuthTokenService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> cookie = CookieUtils.readServletCookie(request, JwtUtils.ACCESS_TOKEN_KEY);
        if(!cookie.isEmpty()) { // AccessToken exists
            String accessToken = cookie.get();
            if(isExpired(accessToken)) {
                try {
                    accessToken = renewJwtToken(accessToken);
                    CookieUtils.addCookie(response, JwtUtils.ACCESS_TOKEN_KEY, accessToken, 3600);
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
    private String renewJwtToken(String accessToken) throws InterruptedException {
        OAuthToken oAuthToken = oAuthTokenService.findOAuthToken(accessToken);
        oAuthTokenService.deleteOAuthToken(oAuthToken);
        if(isExpired(oAuthToken.getRefreshToken())) {
            throw new RuntimeException("REFRESH TOKEN EXPIRED");
        }
        Authentication authentication = jwtUtils.getAuthentication(accessToken);
        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(authentication);
        OAuthToken newOAuthToken = oAuthTokenService.saveOAuthToken(
                memberJwtDetails.getAccessToken(), memberJwtDetails.getRefreshToken());
        return newOAuthToken.getAccessToken();
    }
}
