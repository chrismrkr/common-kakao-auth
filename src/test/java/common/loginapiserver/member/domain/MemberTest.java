package common.loginapiserver.member.domain;


import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @Test
    void Member를_MemberEntity로_변경할_수_있다() {
        // given
        Member member = Member.builder()
                .id(1L)
                .loginId("kang00")
                .password("1234")
                .nickname("ok")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        // when
        MemberEntity memberEntity = MemberEntity.from(member);
        // then
        Assertions.assertEquals(memberEntity.getId(), member.getId());
        Assertions.assertEquals(memberEntity.getLoginId(), member.getLoginId());
        Assertions.assertEquals(memberEntity.getPassword(), member.getPassword());
        Assertions.assertEquals(memberEntity.getNickname(), member.getNickname());
        Assertions.assertEquals(memberEntity.getEmail(), member.getEmail());
        Assertions.assertEquals(memberEntity.getPhoneNumber(), member.getPhoneNumber());
        Assertions.assertEquals(memberEntity.getAuthType(), member.getAuthType());
    }

    @Test
    void MemberEntity를_Member로_변경할_수_있다() {
        // given
        MemberEntity memberEntity = MemberEntity.builder()
                .id(1L)
                .loginId("kang00")
                .password("1234")
                .nickname("ok")
                .email("email@email.com")
                .authType(AuthType.NORMAL)
                .build();
        // when
        Member member = memberEntity.to();
        // then
        Assertions.assertEquals(memberEntity.getId(), member.getId());
        Assertions.assertEquals(memberEntity.getLoginId(), member.getLoginId());
        Assertions.assertEquals(memberEntity.getPassword(), member.getPassword());
        Assertions.assertEquals(memberEntity.getNickname(), member.getNickname());
        Assertions.assertEquals(memberEntity.getEmail(), member.getEmail());
        Assertions.assertEquals(memberEntity.getPhoneNumber(), member.getPhoneNumber());
        Assertions.assertEquals(memberEntity.getAuthType(), member.getAuthType());
    }
}
