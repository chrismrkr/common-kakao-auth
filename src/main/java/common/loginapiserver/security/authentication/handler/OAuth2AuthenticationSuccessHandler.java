package common.loginapiserver.security.authentication.handler;

import common.loginapiserver.security.authentication.oauth.infrastructure.CookieAuthorizationRequestRepository;
import common.loginapiserver.security.authorization.jwt.service.OAuthTokenService;
import common.loginapiserver.security.authorization.jwt.utils.JwtUtils;
import common.loginapiserver.security.authorization.jwt.domain.MemberJwtDetails;
import common.loginapiserver.common.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static common.loginapiserver.security.authentication.oauth.infrastructure.CookieAuthorizationRequestRepository.*;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${oauth2.redirect-url.success}")
    private String defaultRedirectUrl;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final JwtUtils jwtUtils;
    private final OAuthTokenService oAuthTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUri = null;
        try {
            targetUri = getTargetUriWithJwt(request, response, authentication);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }
    protected String getTargetUriWithJwt(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws InterruptedException {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(cookie -> cookie.getValue());
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("REDIRECT URIS NOT MATCHED");
        }
        String targetUri = redirectUri.orElse(defaultRedirectUrl);
        // memberJwtTokenInfo: AccessToken(cookie), RefreshToken
        MemberJwtDetails memberJwtDetails = jwtUtils.generateJwtToken(authentication);
        oAuthTokenService.saveOAuthToken(memberJwtDetails.getAccessToken(),
                                             memberJwtDetails.getRefreshToken());

        String resultUri = UriComponentsBuilder
                .fromUriString(targetUri)
                .queryParam(JwtUtils.ACCESS_TOKEN_KEY, memberJwtDetails.getAccessToken())
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
