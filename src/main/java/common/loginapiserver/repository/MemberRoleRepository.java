package common.loginapiserver.repository;

import common.loginapiserver.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRoleRepository extends JpaRepository<MemberRole, MemberRole.JoinTableId> {
    @Query(value = "SELECT mr FROM MemberRole mr " +
                    "JOIN FETCH mr.role " +
                    "WHERE mr.member.loginId = :loginId")
    List<MemberRole> findByLoginId(@Param("loginId") String loginId);
}
