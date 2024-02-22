package common.loginapiserver.member.service.port;

import common.loginapiserver.member.domain.Member;
import common.loginapiserver.member.infrastructure.MemberJpaRepository;
import common.loginapiserver.member.infrastructure.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Member getByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findById(Long id);
}
