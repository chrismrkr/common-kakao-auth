package common.loginapiserver.security.authorization.jwt.infrastructure;

import common.loginapiserver.security.authorization.jwt.infrastructure.entity.OAuthTokenDatabaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthTokenJpaRepository extends JpaRepository<OAuthTokenDatabaseEntity, String> {
    Optional<OAuthTokenDatabaseEntity> findByAccessToken(String accessToken);
}
