package common.loginapiserver.member.infrastructure;

import common.loginapiserver.member.infrastructure.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRoleJpaRepository extends JpaRepository<MemberRoleEntity, MemberRoleEntity.JoinTableId> {
    @Query(value = "SELECT mr FROM MemberRoleEntity mr " +
                    "JOIN FETCH mr.roleEntity " +
                    "WHERE mr.memberEntity.loginId = :loginId")
    List<MemberRoleEntity> findByLoginId(@Param("loginId") String loginId);
}
