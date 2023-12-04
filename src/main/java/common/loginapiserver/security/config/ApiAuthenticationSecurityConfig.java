package common.loginapiserver.security.config;

import common.loginapiserver.security.filter.ApiLoginProcessingFilter;
import common.loginapiserver.security.filter.JwtAuthorizationFilter;
import common.loginapiserver.security.handler.ApiLogoutHandler;
import common.loginapiserver.security.handler.ApiLogoutSuccessHandler;
import common.loginapiserver.security.handler.OAuth2AuthenticationFailureHandler;
import common.loginapiserver.security.handler.OAuth2AuthenticationSuccessHandler;
import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class ApiAuthenticationSecurityConfig {
    private final MemberService memberService;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler auth2AuthenticationFailureHandler;
    private final ApiLogoutHandler logoutHandler;
    private final ApiLogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationProvider apiAuthenticationProvider;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

//    @Bean(name = "apiAuthenticationManager")
//    public AuthenticationManager apiAuthenticationManager() {
//        List<AuthenticationProvider> providerList = new ArrayList<>();
//        providerList.add(apiAuthenticationProvider);
//        return new ProviderManager(providerList);
//    }
//
//    @Bean
//    public ApiLoginProcessingFilter apiLoginProcessingFilter() {
//        ApiLoginProcessingFilter apiLoginProcessingFilter = new ApiLoginProcessingFilter();
//        apiLoginProcessingFilter.setAuthenticationManager(apiAuthenticationManager());
//        apiLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
//        apiLoginProcessingFilter.setAuthenticationFailureHandler(auth2AuthenticationFailureHandler);
//        return apiLoginProcessingFilter;
//    }

//    @Bean
//    public SecurityFilterChain apiAuthFilterChain(HttpSecurity http) throws Exception {
//        http.formLogin().disable();
//
//        http
//                .authorizeRequests()
//                .antMatchers("/api/login").permitAll()
//                .antMatchers("/api/login/register").permitAll()
//                .antMatchers("/test").hasRole("USER");
//
//        http.exceptionHandling()
//                .accessDeniedHandler(new AccessDeniedHandler() { // 인가 에러
//                    @Override
//                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//                        return;
//                    }
//                });
//
//        http
//                .logout()
//                .logoutUrl("/api/logout") // POST /api/logout
//                .deleteCookies(JwtTokenProvider.ACCESS_TOKEN_KEY)
//                .addLogoutHandler(logoutHandler)
//                .logoutSuccessHandler(logoutSuccessHandler);
//
//
//
//
//        http.csrf().disable(); // API 통신이므로 세션 정보를 활용하지 않으므로 disable
//
//        http.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
//        //http.addFilterBefore(jwtAuthorizationFilter, ApiLoginProcessingFilter.class);
//        return http.build();
//    }
}
