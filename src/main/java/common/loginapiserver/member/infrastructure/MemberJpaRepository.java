package common.loginapiserver.member.infrastructure;

import common.loginapiserver.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    @Query(value = "SELECT m FROM MemberEntity m " +
                    "WHERE m.loginId = :loginId")
    Optional<MemberEntity> findByLoginId(@Param("loginId") String loginId);
}
