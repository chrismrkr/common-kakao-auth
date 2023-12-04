package common.loginapiserver.repository.member;

import common.loginapiserver.domain.entity.Member;
import common.loginapiserver.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
public class CreateTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("1. 회원 생성")
    void createMember() {
        Member member = Member.builder()
                .loginId("kangok")
                .password("1234")
                .nickname("303")
                .email("email@email.com")
                .phoneNumber("010000000000")
                .build();
        memberRepository.save(member);

        Member find = memberRepository.findById(member.getId()).get();
        Assertions.assertEquals(member.getId(), find.getId());
    }
}
