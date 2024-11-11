package common.loginapiserver.medium.member.infrastructure;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.MemberJpaRepository;
import common.loginapiserver.member.infrastructure.MemberRoleJpaRepository;
import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import common.loginapiserver.member.service.port.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.Optional;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void Member를_생성할_수_있다() {
        // given
        Member member = Member.builder()
                .loginId("kangok")
                .password("1234")
                .nickname("303")
                .email("email@email.com")
                .phoneNumber("010000000000")
                .build();
        // when
        Member save = memberRepository.save(member);

        // then
        Member find = memberRepository.findById(save.getId()).get();
        Assertions.assertEquals(save.getId(), find.getId());
        Assertions.assertEquals(save.getId(), memberJpaRepository.findById(save.getId()).get().getId());
    }

    @Test
    void Member를_loginId로_조회할_수_있다() {
        // given
        Member member = Member.builder()
                .loginId("kangok2")
                .password("1234")
                .nickname("303")
                .email("email@email.com")
                .phoneNumber("010000000000")
                .build();
        Member save = memberRepository.save(member);

        // when
        Member find = memberRepository.getByLoginId("kangok2");

        // then
        Assertions.assertEquals(find.getLoginId(), "kangok2");

    }

    @Test
    void Member를_PK로_조회할_수_있다() {
        // given
        Member member = Member.builder()
                .loginId("kangok")
                .password("1234")
                .nickname("303")
                .email("email@email.com")
                .phoneNumber("010000000000")
                .build();
        Member save = memberRepository.save(member);

        // when
        Long pk = save.getId();
        Member find = memberRepository.findById(pk).get();

        // then
        Assertions.assertEquals(pk, find.getId());
    }
}
