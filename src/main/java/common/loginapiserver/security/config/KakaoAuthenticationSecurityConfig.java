package common.loginapiserver.security.config;

import common.loginapiserver.security.filter.ApiLoginProcessingFilter;
import common.loginapiserver.security.filter.JwtAuthorizationFilter;
import common.loginapiserver.security.handler.ApiLogoutHandler;
import common.loginapiserver.security.handler.ApiLogoutSuccessHandler;
import common.loginapiserver.security.handler.OAuth2AuthenticationFailureHandler;
import common.loginapiserver.security.handler.OAuth2AuthenticationSuccessHandler;
import common.loginapiserver.security.oauth2.*;
import common.loginapiserver.service.MemberService;
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
public class KakaoAuthenticationSecurityConfig {
    @Value("${oauth2.redirect-url.success}")
    private String loginSuccessUri;
    @Value("{oauth2.redirect-url.failure}")
    private String loginFailureUri;
    @Value("{oauth2.base.auth-endpoint}")
    private String baseAuthorizationEndPoint;
    @Value("{oauth2.redirect.auth-endpoint.kakao}")
    private String kakaoRedirectEndpoint;
    private final MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
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
        ApiLoginProcessingFilter apiLoginProcessingFilter = new ApiLoginProcessingFilter();
        apiLoginProcessingFilter.setAuthenticationManager(apiAuthenticationManager());
        apiLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        apiLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return apiLoginProcessingFilter;
    }
    @Bean
    public SecurityFilterChain kakaoAuthFilterChain(HttpSecurity http) throws Exception {
        //httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http.cors()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .rememberMe().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //요청에 대한 권한 설정
        http.authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/login/register").permitAll()
                .antMatchers("/login/success").permitAll()
                .antMatchers("/login/failure").permitAll()
                .antMatchers("/test").hasRole("USER")
                .anyRequest().authenticated();

        http.oauth2Login()
                // 로그인 인증 요청 url: localhost:8080/oauth2/authorize/kakao로 로그인 요청
                .authorizationEndpoint().baseUri("/oauth2/authorize/")
                // 인증 요청을 어디에 저장하고 있을지 결정
                .authorizationRequestRepository(cookieAuthorizationRequestRepository) // 로그인 인증 요청 결과 저장소
                .and()
                // 인증이 완료되면 인가코드를 전달받는데, 어느쪽으로 보낼지를 지정함
                .redirectionEndpoint().baseUri("/login/oauth2/kakao")
                .and()
                // redirectEndpoint URI로 redirect 이후, 인가코드 <-> accessToken 교환 -> 실행할 service 설정
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);


        http.exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        return;
                    }
                })
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        return;
                    }
                });

        http.addFilterBefore(jwtAuthorizationFilter, OAuth2AuthorizationRequestRedirectFilter.class);
        http.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
