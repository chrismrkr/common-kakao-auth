package common.loginapiserver.common.test;

import common.loginapiserver.common.MockOAuthTokenRedisRepository;
import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MockOAuthTokenRedisRepositoryTest {
    MockOAuthTokenRedisRepository mockOAuthTokenRedisRepository = new MockOAuthTokenRedisRepository();

    @Test
    void 토큰_저장() throws InterruptedException {
        // given
        OAuthToken oAuthToken = OAuthToken.builder()
                .accessToken("my-access")
                .refreshToken("my-refresh")
                .build();
        // when
        OAuthToken save = mockOAuthTokenRedisRepository.save(oAuthToken);
        // then
        Assertions.assertEquals(save.getAccessToken(), oAuthToken.getAccessToken());
        Assertions.assertEquals(save.getRefreshToken(), oAuthToken.getRefreshToken());
    }
    @Test
    void 토큰_저장_멀티스레드환경() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        // when
        for(int i=0; i<threadCount; i++) {
            int finalI = i;
            executor.submit(() -> {
                long startTime = System.currentTimeMillis();
                try {
                    mockOAuthTokenRedisRepository.save(
                            OAuthToken.builder()
                                    .accessToken("my-access")
                                    .refreshToken("my-refresh-" + Integer.toString(finalI))
                                    .build()
                    );
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long endtime = System.currentTimeMillis();
                log.info("Thread {} takes {}", finalI, endtime-startTime);
                latch.countDown();
            });
        }
        latch.await();
        // then
        log.info("my-access : {}", mockOAuthTokenRedisRepository.findByAccessToken("my-access").get().getRefreshToken() );
    }


}
