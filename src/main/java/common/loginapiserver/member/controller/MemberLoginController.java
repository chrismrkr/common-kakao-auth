package common.loginapiserver.member.controller;

import common.loginapiserver.member.controller.dto.LoginResponseDto;
import common.loginapiserver.member.controller.dto.enumerate.LoginResult;
import common.loginapiserver.security.authorization.jwt.utils.JwtUtils;
import common.loginapiserver.common.utils.CookieUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class MemberLoginController {
    @GetMapping("/api/login/success") /* /login/success?JWT=xxxxx */
    public ResponseEntity<LoginResponseDto> successLogin(HttpServletResponse response,
                        @RequestParam(JwtUtils.ACCESS_TOKEN_KEY) String accessToken) throws IOException {
        log.debug("[LOGIN SUCCESS]");
        CookieUtils.addCookie(response, JwtUtils.ACCESS_TOKEN_KEY, accessToken, 3600);
        LoginResponseDto loginSuccess = LoginResponseDto.builder()
                                        .loginResult(LoginResult.SUCCESS)
                                        .responseMessage("LOGIN SUCCESS").build();
        ResponseEntity<LoginResponseDto> responseBody = ResponseEntity.ok()
                                                        .body(loginSuccess);
        return responseBody;
    }

    @GetMapping("/api/login/failure")
    public ResponseEntity<LoginResponseDto> failureLogin(HttpServletResponse response) throws IOException {
        log.debug("[LOGIN FAIL]");
        LoginResponseDto loginFailure = LoginResponseDto.builder()
                                        .loginResult(LoginResult.FAIL)
                                        .responseMessage("INCORRECT ID OR PASSWORD").build();
        ResponseEntity<LoginResponseDto> responseBody = ResponseEntity.badRequest()
                                                        .body(loginFailure);
        return responseBody;
    }
    @GetMapping("/api/logout/success")
    public String successLogout(HttpServletResponse response) throws IOException {
        return "ok";
    }

    @GetMapping("/api/test")
    public String test() {
        return "test token";
    }
}