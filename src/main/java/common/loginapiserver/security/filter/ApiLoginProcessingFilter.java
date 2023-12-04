package common.loginapiserver.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import common.loginapiserver.domain.dto.LoginRequestDto;
import common.loginapiserver.security.token.ApiAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ApiLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper objectMapper = new ObjectMapper(); // thread safe
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/login");
    public ApiLoginProcessingFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequestDto loginRequestDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);
        String loginId = loginRequestDto.getLoginId();
        String password = loginRequestDto.getPassword();
        if(StringUtils.isBlank(loginId) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("LoginId or password should not be blank.");
        }
        ApiAuthenticationToken authenticationToken = new ApiAuthenticationToken(loginId, password);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
