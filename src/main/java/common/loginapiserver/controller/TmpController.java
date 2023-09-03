package common.loginapiserver.controller;

import common.loginapiserver.security.oauth2.jwt.JwtTokenProvider;
import common.loginapiserver.security.utils.CookieUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;

@RestController
public class TmpController {
    @GetMapping("/")
    public String success(HttpServletResponse response, @RequestParam("token")String token) {
        CookieUtils.addCookie(response, JwtTokenProvider.ACCESS_TOKEN_KEY, token, 3600);
        return token;
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
