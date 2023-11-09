package common.loginapiserver.controller;

import common.loginapiserver.dto.MemberRequestDto;
import common.loginapiserver.repository.MemberRepository;
import common.loginapiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public void createNewMember(@RequestBody MemberRequestDto memberDto) {
    }
}
