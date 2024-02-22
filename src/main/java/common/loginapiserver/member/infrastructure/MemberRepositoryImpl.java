package common.loginapiserver.member.infrastructure;

import common.loginapiserver.common.exception.ResourceNotFoundException;
import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import common.loginapiserver.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        Member save = memberJpaRepository.save(MemberEntity.from(member)).to();
        return save;
    }

    @Override
    public Member getByLoginId(String loginId) {
        Member member = memberJpaRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("member", loginId))
                .to();
        return member;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        Optional<Member> member = memberJpaRepository.findByLoginId(loginId)
                .map(memberEntity -> memberEntity.to());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Optional<Member> member = memberJpaRepository.findById(id).map(MemberEntity::to);
        return member;
    }
}
