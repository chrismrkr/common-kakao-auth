package common.loginapiserver.security.provider;

import common.loginapiserver.security.token.ApiAuthenticationToken;
import common.loginapiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "apiAuthenticationProvider")
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final MemberService memberService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();

        MemberContext memberContext = (MemberContext)memberService.loadMemberByLoginId(loginId);
        if(!passwordEncoder.matches(password, memberContext.getMember().getPassword())) {
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
