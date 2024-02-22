package common.loginapiserver.security.authorization.jwt.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthTokenJpaRepository extends JpaRepository<OAuthTokenEntity, Long> {
    Optional<OAuthTokenEntity> findByAccessToken(String accessToken);
}
