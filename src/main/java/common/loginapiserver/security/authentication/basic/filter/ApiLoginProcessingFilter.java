package common.loginapiserver.security.authentication.basic.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import common.loginapiserver.member.controller.dto.LoginRequestDto;
import common.loginapiserver.security.authentication.basic.provider.token.ApiAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ApiLoginProcessingFilter(String entryUrl) {
        super(new AntPathRequestMatcher(entryUrl));
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequestDto loginRequestDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);
        String loginId = loginRequestDto.getLoginId();
        String password = loginRequestDto.getPassword();
        if(StringUtils.isBlank(loginId) || StringUtils.isBlank(password)) {
            throw new BadCredentialsException("LoginId or password should not be blank.");
        }
        ApiAuthenticationToken authenticationToken = new ApiAuthenticationToken(loginId, password);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}