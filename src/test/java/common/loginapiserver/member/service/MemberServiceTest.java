package common.loginapiserver.member.service;

import common.loginapiserver.common.MockMemberRepository;
import common.loginapiserver.common.MockMemberRoleRepository;
import common.loginapiserver.common.MockRoleRepository;
import common.loginapiserver.common.TestContainer;
import common.loginapiserver.common.utils.PasswordEncoderUtils;
import common.loginapiserver.member.controller.port.MemberService;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.domain.MemberRole;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

public class MemberServiceTest {
    static TestContainer testContainer;
    MemberService memberService;

    @BeforeAll
    static void init() {
        testContainer = TestContainer.builder()
                .memberRepository(new MockMemberRepository())
                .memberRoleRepository(new MockMemberRoleRepository())
                .roleRepository(new MockRoleRepository())
                        .build();
    }

    @Test
    void Member를_생성할_수_있다() {
        // given
        memberService = new MemberServiceImpl(testContainer.memberRepository,
                testContainer.memberRoleRepository,
                testContainer.roleRepository,
                new PasswordEncoderUtils());
        Member member = Member.builder()
                .loginId("member1")
                .password("1234")
                .nickname("m")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        // when
        MemberRole memberRole = memberService.createMember(member);

        // then
        Assertions.assertEquals(memberRole.getMember().getLoginId(),member.getLoginId());
        Assertions.assertEquals(memberRole.getRole().getRoleName(), "ROLE_USER");
    }

    @Test
    void 일반_사용자를_권한과_함께_조회할_수_있다() {
        // given
        memberService = new MemberServiceImpl(testContainer.memberRepository,
                testContainer.memberRoleRepository,
                testContainer.roleRepository,
                new PasswordEncoderUtils());
        Member member2 = Member.builder()
                .loginId("member2")
                .password("1234")
                .nickname("m2")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        memberService.createMember(member2);
        // when
        UserDetails userDetails = memberService.loadMemberByLoginId("member2");

        // then
        Assertions.assertEquals(userDetails.getUsername(), member2.getLoginId());
        Assertions.assertEquals(userDetails.getPassword(), member2.getPassword());
        Assertions.assertEquals(userDetails.getAuthorities().size(), 1);
        Assertions.assertEquals(userDetails.getAuthorities().stream().findFirst().get().getAuthority(), "ROLE_USER");
    }

    @Test
    void 일반_사용자가_아닌_경우에는_조회가_불가능하다() {
        // given
        memberService = new MemberServiceImpl(testContainer.memberRepository,
                testContainer.memberRoleRepository,
                testContainer.roleRepository,
                new PasswordEncoderUtils());
        Member memberKakao = Member.builder()
                .loginId("member3")
                .password("1234")
                .nickname("m2")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.KAKAO)
                .build();
        memberService.createMember(memberKakao);
        // when
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.loadMemberByLoginId("member3"));
    }

    @Test
    void loginId는_중복될_수_없다() {
        // given
        memberService = new MemberServiceImpl(testContainer.memberRepository,
                testContainer.memberRoleRepository,
                testContainer.roleRepository,
                new PasswordEncoderUtils());
        Member member = Member.builder()
                .loginId("member4")
                .password("1234")
                .nickname("m")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        Member memberDummy = Member.builder()
                .loginId("member4")
                .password("1234")
                .nickname("m")
                .email("email@email.com")
                .phoneNumber("01012341234")
                .authType(AuthType.NORMAL)
                .build();
        memberService.createMember(member);
        // when
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberService.createMember(memberDummy);
        });

    }
}
