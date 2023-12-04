package common.loginapiserver.repository;

import common.loginapiserver.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT m FROM Member m " +
                    "WHERE m.loginId = :loginId")
    Optional<Member> findByLoginId(@Param("loginId") String loginId);
}
