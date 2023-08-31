package common.loginapiserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TmpController {
    @GetMapping("/")
    public String success(@RequestParam("token")String token) {
        return token;
    }
}
