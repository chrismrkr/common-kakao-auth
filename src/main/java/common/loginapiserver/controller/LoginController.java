package common.loginapiserver.controller;

import common.loginapiserver.domain.dto.LoginResponseDto;
import common.loginapiserver.domain.entity.enumerate.LoginResult;
import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.utils.CookieUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class LoginController {
    @GetMapping("/login/success") /* /login/success?JWT=xxxxx */
    public ResponseEntity<LoginResponseDto> successLogin(HttpServletResponse response,
                        @RequestParam(JwtTokenProvider.ACCESS_TOKEN_KEY) String accessToken) throws IOException {

        CookieUtils.addCookie(response, JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken, 3600);
        LoginResponseDto loginSuccess = LoginResponseDto.builder()
                                        .loginResult(LoginResult.SUCCESS)
                                        .responseMessage("LOGIN SUCCESS").build();
        ResponseEntity<LoginResponseDto> responseBody = ResponseEntity.ok()
                                                        .body(loginSuccess);
        return responseBody;
    }

    @GetMapping("/login/failure")
    public ResponseEntity<LoginResponseDto> failureLogin(HttpServletResponse response) throws IOException {
        LoginResponseDto loginFailure = LoginResponseDto.builder()
                                        .loginResult(LoginResult.FAIL)
                                        .responseMessage("INCORRECT ID OR PASSWORD").build();
        ResponseEntity<LoginResponseDto> responseBody = ResponseEntity.badRequest()
                                                        .body(loginFailure);
        return responseBody;
    }
    @GetMapping("/logout/success")
    public String successLogout(HttpServletResponse response) throws IOException {
        return "ok";
    }

    @GetMapping("/test")
    public String test() {
        return "ok";
    }
}