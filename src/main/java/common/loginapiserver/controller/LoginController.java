package common.loginapiserver.controller;

import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.utils.CookieUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginController {
    @GetMapping("/login-success")
    public void success(HttpServletResponse response,
                        @RequestParam(JwtTokenProvider.ACCESS_TOKEN_KEY) String accessToken) throws IOException {
        CookieUtils.addCookie(response, JwtTokenProvider.ACCESS_TOKEN_KEY, accessToken, 3600);
        response.sendRedirect("http://localhost:3000/login-success");
    }

    @GetMapping("/login-failure")
    public void failure(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/login-failure");
    }

    @GetMapping("/data")
    public TmpResponseBody getTmpResponseData() {
        TmpResponseBody thisIsTmpData = new TmpResponseBody("this is tmp data");
        return thisIsTmpData;
    }

    @Data
    @AllArgsConstructor
    static class TmpResponseBody {
        private String data;
    }
}
