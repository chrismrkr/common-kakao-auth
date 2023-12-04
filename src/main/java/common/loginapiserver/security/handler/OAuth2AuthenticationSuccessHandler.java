package common.loginapiserver.security.handler;

import common.loginapiserver.security.oauth2.CookieAuthorizationRequestRepository;
import common.loginapiserver.security.oauth2.OAuthTokenInfoService;
import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.oauth2.jwt.MemberJwtTokenInfo;
import common.loginapiserver.security.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static common.loginapiserver.security.oauth2.CookieAuthorizationRequestRepository.*;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${oauth2.redirect-url.success}")
    private String defaultRedirectUrl;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthTokenInfoService oAuthTokenInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUri = getTargetUriWithJwt(request, response, authentication);
        if(response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }
    protected String getTargetUriWithJwt(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(cookie -> cookie.getValue());
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("REDIRECT URIS NOT MATCHED");
        }
        String targetUri = redirectUri.orElse(defaultRedirectUrl);
        // memberJwtTokenInfo: AccessToken(cookie), RefreshToken
        MemberJwtTokenInfo memberJwtTokenInfo = jwtTokenProvider.generateJwtToken(authentication);
        oAuthTokenInfoService.saveOAuthTokenInfo(memberJwtTokenInfo.getAccessToken(),
                                             memberJwtTokenInfo.getRefreshToken());

        String resultUri = UriComponentsBuilder
                .fromUriString(targetUri)
                .queryParam(JwtTokenProvider.ACCESS_TOKEN_KEY, memberJwtTokenInfo.getAccessToken())
                .build()
                .toUriString();
        return resultUri;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(defaultRedirectUrl);
        if (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }
}
