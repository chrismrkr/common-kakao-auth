package common.loginapiserver.member.controller;

import common.loginapiserver.member.controller.dto.MemberRequestDto;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import common.loginapiserver.member.controller.port.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    public ResponseEntity<MemberRole> handleMemberRegister(@Valid @RequestBody MemberRequestDto memberDto) {
        try {
            MemberRole member = memberService.createMember(memberDto.toMember());
            return new ResponseEntity<>(member, HttpStatus.CREATED);
        } catch(Exception e) {
            log.error("[error] {}", e.getMessage());
            MultiValueMap<String, String> exceptionMsg = new LinkedMultiValueMap<>();
            exceptionMsg.add("error", e.getMessage());
            return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
        }
    }
}
