package common.loginapiserver.security.authentication.oauth.utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class OAuth2RequestUtilsImpl implements OAuth2RequestUtils {
    // 스프링 시큐리티 DefaultOAuth2UserService 컴포넌트에서
    // OAuth2UserRequest를 처리하는 부분만 가져와서 컴포넌트화함(테스트 X)
    private final RestTemplate restOperations;

    private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    public OAuth2RequestUtilsImpl() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    @Override
    public OAuth2User getOAuthUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error("missing_user_info_uri", "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String)null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        } else {
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            if (!StringUtils.hasText(userNameAttributeName)) {
                OAuth2Error oauth2Error = new OAuth2Error("missing_user_name_attribute", "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String)null);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            } else {
                RequestEntity<?> request = (RequestEntity)this.requestEntityConverter.convert(userRequest);
                ResponseEntity<Map<String, Object>> response = this.getResponse(userRequest, request);
                Map<String, Object> userAttributes = (Map)response.getBody();
                Set<GrantedAuthority> authorities = new LinkedHashSet();
                authorities.add(new OAuth2UserAuthority(userAttributes));
                OAuth2AccessToken token = userRequest.getAccessToken();
                Iterator var8 = token.getScopes().iterator();

                while(var8.hasNext()) {
                    String authority = (String)var8.next();
                    authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
                }

                return new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);
            }
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
        OAuth2Error oauth2Error;
        try {
            return this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        } catch (OAuth2AuthorizationException var6) {
            oauth2Error = var6.getError();
            StringBuilder errorDetails = new StringBuilder();
            errorDetails.append("Error details: [");
            errorDetails.append("UserInfo Uri: ").append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
            errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
            if (oauth2Error.getDescription() != null) {
                errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
            }

            errorDetails.append("]");
            oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), (String)null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var6);
        } catch (UnknownContentTypeException var7) {
            String errorMessage = "An error occurred while attempting to retrieve the UserInfo Resource from '" + userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri() + "': response contains invalid content type '" + var7.getContentType().toString() + "'. The UserInfo Response should return a JSON object (content type 'application/json') that contains a collection of name and value pairs of the claims about the authenticated End-User. Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '" + userRequest.getClientRegistration().getRegistrationId() + "' conforms to the UserInfo Endpoint, as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'";
            oauth2Error = new OAuth2Error("invalid_user_info_response", errorMessage, (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var7);
        } catch (RestClientException var8) {
            oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + var8.getMessage(), (String)null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var8);
        }
    }
}
