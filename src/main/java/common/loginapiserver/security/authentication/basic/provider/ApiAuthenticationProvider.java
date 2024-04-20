package common.loginapiserver.security.authentication.basic.provider;

import common.loginapiserver.common.utils.PasswordEncoderUtils;
import common.loginapiserver.member.domain.MemberContext;
import common.loginapiserver.security.authentication.basic.provider.token.ApiAuthenticationToken;
import common.loginapiserver.member.controller.port.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component(value = "apiAuthenticationProvider")
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoderUtils passwordEncoderUtils;
    private final MemberService memberService;
    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();

        MemberContext memberContext = null;
        try {
            memberContext = (MemberContext)memberService.loadMemberByLoginId(loginId);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
        if(!passwordEncoderUtils.getPasswordEncoder().matches(password, memberContext.getMember().getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }
        ApiAuthenticationToken authenticationToken = new ApiAuthenticationToken(loginId, null, memberContext.getAuthorities());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(ApiAuthenticationToken.class);
    }
}
