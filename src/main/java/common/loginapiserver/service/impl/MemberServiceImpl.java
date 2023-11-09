package common.loginapiserver.service.impl;

import common.loginapiserver.dto.MemberRequestDto;
import common.loginapiserver.entity.Member;
import common.loginapiserver.repository.MemberRepository;
import common.loginapiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    public void save(MemberRequestDto memberRequestDto) {
        try {
//            validateMemberDto(memberRequestDto);
//            Member newMember = Member.builder()
//                    .loginId()
//                    .password()
//                    .email()
//                    .nickname()
//                    .phoneNumber()
//                    .oAuth2Info()
//                    .build();
//            Member savedMember = memberRepository.save(newMember);

        } catch(Exception e) {
            throw e;
        }
    }
    private void validateMemberDto(MemberRequestDto memberDto) {

    }
}
