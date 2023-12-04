package common.loginapiserver.security.filter;

import common.loginapiserver.security.oauth2.OAuthTokenInfoService;
import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.oauth2.jwt.MemberJwtTokenInfo;
import common.loginapiserver.security.oauth2.jwt.OAuthTokenInfo;
import common.loginapiserver.security.utils.CookieUtils;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthTokenInfoService oAuthTokenInfoService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> cookie = CookieUtils.readServletCookie(request, JwtTokenProvider.ACCESS_TOKEN_KEY);
        if(!cookie.isEmpty()) { // AccessToken exists
            String accessToken = cookie.get();
            if(isExpired(accessToken)) {
                try {
                    accessToken = renewJwtToken(accessToken);
                } catch(Exception e) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    private boolean isExpired(String token) {
        return jwtTokenProvider.isExpired(token);
    }

    @Transactional
    private String renewJwtToken(String accessToken) {
        OAuthTokenInfo oAuthTokenInfo = oAuthTokenInfoService.findOAuthTokenInfo(accessToken);
        if(isExpired(oAuthTokenInfo.getRefreshToken())) {
            oAuthTokenInfoService.deleteOAuthTokenInfo(oAuthTokenInfo);
            throw new RuntimeException("REFRESH TOKEN EXPIRED");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        MemberJwtTokenInfo memberJwtTokenInfo = jwtTokenProvider.generateJwtToken(authentication);
        oAuthTokenInfo = oAuthTokenInfoService.updateAccessToken(oAuthTokenInfo,
                                                                memberJwtTokenInfo.getAccessToken());
        return oAuthTokenInfo.getAccessToken();
    }
}
