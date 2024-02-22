package common.loginapiserver.common;


import common.loginapiserver.common.exception.ResourceNotFoundException;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.entity.enumerate.AuthType;
import common.loginapiserver.member.service.port.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MockMemberRepository implements MemberRepository {
    private static AtomicLong atomicLong = new AtomicLong();
    private List<Member> datas = new ArrayList<>();

    @Override
    public Member save(Member member) {
        if(datas.size() > 0) {
            Optional<Member> any = datas.stream().filter(m -> m.getId() == member.getId()).findAny();
            if(!any.isEmpty()) {
                datas.remove(any.get());
            }
        }
        Member m = Member.builder()
                .id(atomicLong.getAndIncrement())
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .authType(member.getAuthType())
                .build();
        datas.add(m);
        return m;
    }

    @Override
    public Member getByLoginId(String loginId) {
        Member member = datas.stream().filter(m -> m.getLoginId().equals(loginId))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("member", loginId));
        return member;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        Optional<Member> any = datas.stream().filter(m -> m.getLoginId().equals(loginId))
                .findAny();
        return any;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Optional<Member> any = datas.stream().filter(m -> m.getId() == id)
                .findAny();
        return any;
    }
}
