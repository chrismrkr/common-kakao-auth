package common.loginapiserver.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // SecurityContext 초기화
        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            SecurityContextHolder.clearContext();
        }
        // RefreshToken 삭제
    }
}
