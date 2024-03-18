package common.loginapiserver.common;

import common.loginapiserver.security.authorization.jwt.domain.OAuthToken;
import common.loginapiserver.security.authorization.jwt.service.port.OAuthTokenRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class MockOAuthTokenRedisRepository implements OAuthTokenRepository {
    private final Map<String, String> datas = new HashMap<>();
    private final AtomicBoolean semaphore = new AtomicBoolean(false);

    @Override
    public Optional<OAuthToken> findByAccessToken(String accessToken) throws InterruptedException {
        waitingForUnlock(50);
        semaphore.set(true);
        String refreshToken = datas.get(accessToken);
        semaphore.set(false);
        if(refreshToken == null) {
            return Optional.empty();
        }

        log.info("[SUCCESS] FOUND TOKEN: {}", accessToken);
        return Optional.of(
                OAuthToken.builder().accessToken(accessToken).refreshToken(refreshToken).build()
        );
    }

    @Override
    public OAuthToken save(OAuthToken oAuthToken) throws InterruptedException {
        waitingForUnlock(50);
        semaphore.set(true);

        Random random = new Random();
        int waitTime = random.nextInt(100) % 30;
        Thread.sleep(waitTime);

        datas.put(oAuthToken.getAccessToken(), oAuthToken.getRefreshToken());
        semaphore.set(false);
        log.info("[SUCCESS] SAVE TOKEN: {}", oAuthToken.getAccessToken());
        return OAuthToken.builder().accessToken(oAuthToken.getAccessToken())
                .refreshToken(oAuthToken.getRefreshToken())
                .build();
    }

    @Override
    public void delete(OAuthToken oAuthToken) throws InterruptedException {
        waitingForUnlock(50);
        semaphore.set(true);
        if(datas.containsKey(oAuthToken.getAccessToken())) {
            log.info("[SUCCESS] DELETE TOKEN: {}", oAuthToken.getAccessToken());
            datas.remove(oAuthToken.getAccessToken());
        }
        semaphore.set(false);
    }
    private boolean isLock() {
        return semaphore.get();
    }
    private void waitingForUnlock(int millis) throws InterruptedException {
        try {
            while(isLock()) {
                log.info("waiting for lock");
                Thread.sleep(millis);
            }
        } catch(InterruptedException e) {
            throw e;
        }
    }
}
