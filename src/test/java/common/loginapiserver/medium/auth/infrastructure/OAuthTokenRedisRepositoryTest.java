package common.loginapiserver.medium.auth.infrastructure;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.infrastructure.OAuthTokenRedisRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
public class OAuthTokenRedisRepositoryTest {
    @Autowired
    OAuthTokenRedisRepositoryImpl oauthTokenRedisRepository;

    @Test
    void OAuthToken을_저장할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .refreshToken("my-refresh")
                .principal("my-principal")
                .build();
        // when
        oauthTokenRedisRepository.save(oAuthToken, 100);
        // then
        OAuthToken tokenFind = oauthTokenRedisRepository.findByRefreshToken("my-refresh").get();
        Assertions.assertEquals(oAuthToken.getRefreshToken(), tokenFind.getRefreshToken());
        oauthTokenRedisRepository.delete(oAuthToken);
    }

    @Test
    void 멀티스레드로_OAuthToken_저장하기() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        // when
        for(int i=0; i<threadCount; i++) {
            int finalI = i;
            executor.submit(() -> {
               oauthTokenRedisRepository.save(
                       OAuthToken.builder()
                               .refreshToken("my-refresh")
                               .principal("my-principal-" + Integer.toString(finalI))
                               .build(), 100
               );
               latch.countDown();
            });
        }

        latch.await();
        // then
        log.info("my-access : {}", oauthTokenRedisRepository.findByRefreshToken("my-refresh").get().getRefreshToken());
    }
    @Test
    void AccessToken을_key로_OAuthToken을_찾을_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .refreshToken("my-refresh")
                .principal("my-principal")
                .build();
        oauthTokenRedisRepository.save(oAuthToken, 100);
        // when
        OAuthToken tokenFind = oauthTokenRedisRepository.findByRefreshToken("my-refresh").get();

        // then
        Assertions.assertEquals(oAuthToken.getRefreshToken(), tokenFind.getRefreshToken());
        oauthTokenRedisRepository.delete(oAuthToken);
    }
    @Test
    void OAuthToken을_key인_AccessToken으로_삭제할_수_있다() {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .refreshToken("my-refresh")
                .principal("my-principal")
                .build();
        oauthTokenRedisRepository.save(oAuthToken, 100);
        // when
        oauthTokenRedisRepository.delete(oAuthToken);
        // then
        Assertions.assertEquals(Optional.empty(), oauthTokenRedisRepository.findByRefreshToken("my-refresh"));
    }
}
