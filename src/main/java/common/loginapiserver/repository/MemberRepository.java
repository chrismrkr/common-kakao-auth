package common.loginapiserver.repository;

import common.loginapiserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT m FROM Member m " +
                    "WHERE m.oAuth2Info.oAuth2Id = :oAuth2Id")
    Optional<Member> findByOAuth2Id(@Param("oAuth2Id") Long oAuth2Id);
}
