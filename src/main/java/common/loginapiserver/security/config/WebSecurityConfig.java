package common.loginapiserver.security.config;

import common.loginapiserver.security.oauth2.JwtAuthorizationFilter;
import common.loginapiserver.security.oauth2.OAuth2AuthenticationSuccessHandler;
import common.loginapiserver.security.oauth2.CookieAuthorizationRequestRepository;
import common.loginapiserver.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http.cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //요청에 대한 권한 설정
        http.authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/data").hasRole("USER")
                .anyRequest().authenticated();

        http.oauth2Login()
                // 로그인 인증 요청 url: localhost:8080/oauth2/authorize/kakao로 로그인 요청
                .authorizationEndpoint().baseUri("/oauth2/authorize")
                // 인증 요청을 어디에 저장하고 있을지 결정
                .authorizationRequestRepository(cookieAuthorizationRequestRepository) // 로그인 인증 요청 결과 저장소
                .and()
                // 인증이 완료되면 인가코드를 전달받는데, 어느쪽으로 보낼지를 지정함
                .redirectionEndpoint().baseUri("/login/oauth2/kakao")
                .and()
                // redirectEndpoint URI로 redirect 이후, 인가코드 <-> accessToken 교환 -> 실행할 service 설정
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(null);

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
