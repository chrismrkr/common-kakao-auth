package common.loginapiserver.security.config;

import common.loginapiserver.security.authentication.basic.filter.ApiLoginProcessingFilter;
import common.loginapiserver.security.authentication.oauth.infrastructure.CookieAuthorizationRequestRepository;
import common.loginapiserver.security.authentication.oauth.service.CustomOAuth2UserService;
import common.loginapiserver.security.authorization.jwt.filter.JwtAuthorizationFilter;
import common.loginapiserver.security.authentication.handler.ApiLogoutHandler;
import common.loginapiserver.security.authentication.handler.ApiLogoutSuccessHandler;
import common.loginapiserver.security.authentication.handler.OAuth2AuthenticationFailureHandler;
import common.loginapiserver.security.authentication.handler.OAuth2AuthenticationSuccessHandler;
import common.loginapiserver.member.controller.port.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${base.login}")
    String baseLoginUri;
    @Value("${base.login.register}")
    String baseLoginRegisterUri;
    @Value("${oauth2.redirect-url.success}")
    String loginSuccessUri;
    @Value("{oauth2.redirect-url.failure}")
    String loginFailureUri;
    @Value("{oauth2.base.auth-endpoint}")
    String baseAuthorizationEndPoint;
    @Value("{oauth2.redirect.auth-endpoint.kakao}")
    String kakaoRedirectEndpoint;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationEntryPoint oAuth2AuthenticationEntryPoint;
    private final AccessDeniedHandler oAuth2AccessDeniedHandler;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final ApiLogoutHandler logoutHandler;
    private final ApiLogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationProvider apiAuthenticationProvider;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean(name = "apiAuthenticationManager")
    public AuthenticationManager apiAuthenticationManager() {
        List<AuthenticationProvider> providerList = new ArrayList<>();
        providerList.add(apiAuthenticationProvider);
        return new ProviderManager(providerList);
    }
    @Bean
    public ApiLoginProcessingFilter apiLoginProcessingFilter() {
        ApiLoginProcessingFilter apiLoginProcessingFilter = new ApiLoginProcessingFilter(baseLoginUri);
        apiLoginProcessingFilter.setAuthenticationManager(apiAuthenticationManager());
        apiLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        apiLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return apiLoginProcessingFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http.cors()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .rememberMe().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 요청에 대한 권한 설정
        http.authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers(baseLoginUri).permitAll()
                .antMatchers(baseLoginRegisterUri).permitAll()
                .antMatchers(loginSuccessUri).permitAll()
                .antMatchers(loginFailureUri).permitAll()
                .antMatchers("/test").hasRole("USER")
                .anyRequest().authenticated();

        http.oauth2Login()
                // 로그인 인증 요청 url: localhost:8080/oauth2/authorize/kakao로 로그인 요청
                // baseUri는 카카오 개발자 홈페이지에서 등록함
                .authorizationEndpoint().baseUri("/api/oauth2/authorize/")
                // 인증 요청을 어디에 저장하고 있을지 결정
                .authorizationRequestRepository(cookieAuthorizationRequestRepository) // 로그인 인증 요청 결과 저장소
                .and()
                // 인증이 완료되면 인가코드를 전달받는데, 어느쪽으로 보낼지를 지정함
                .redirectionEndpoint().baseUri("/api/login/oauth2/kakao")
                .and()
                // redirectEndpoint URI로 redirect 이후, 인가코드 <-> accessToken 교환 -> 실행할 service 설정
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);


        http.exceptionHandling()
                .accessDeniedHandler(oAuth2AccessDeniedHandler)
                .authenticationEntryPoint(oAuth2AuthenticationEntryPoint);

        http.addFilterBefore(jwtAuthorizationFilter, OAuth2AuthorizationRequestRedirectFilter.class);
        http.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
