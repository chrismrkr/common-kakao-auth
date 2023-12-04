package common.loginapiserver.controller;

import common.loginapiserver.domain.dto.MemberRequestDto;
import common.loginapiserver.domain.entity.Member;
import common.loginapiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/login/register")
    public ResponseEntity<Member> handlePostMember(@Valid @RequestBody MemberRequestDto memberDto) {
        try {
            Member member = memberService.createMember(memberDto);
            return new ResponseEntity<>(member, HttpStatus.CREATED);
        } catch(Exception e) {
            log.error("[error] {}", e.getMessage());
            MultiValueMap<String, String> exceptionMsg = new LinkedMultiValueMap<>();
            exceptionMsg.add("error", e.getMessage());
            return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
        }
    }
}
