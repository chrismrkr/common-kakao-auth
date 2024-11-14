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
    private final Map<String, Object> datas = new HashMap<>();
    private final AtomicBoolean semaphore = new AtomicBoolean(false);

    @Override
    public Optional<OAuthToken> findByRefreshToken(String refreshToken) throws InterruptedException {
        waitingForUnlock(50);
        semaphore.set(true);
        Object principal = datas.get(refreshToken);
        semaphore.set(false);
        if(refreshToken == null) {
            return Optional.empty();
        }

        log.info("[SUCCESS] FOUND REFRESH TOKEN: {}", refreshToken);
        return Optional.of(
                OAuthToken.builder().refreshToken(refreshToken).principal(principal).build()
        );
    }

    @Override
    public OAuthToken save(OAuthToken oAuthToken, long minuteDuration) throws InterruptedException {
        waitingForUnlock(50);
        semaphore.set(true);

        Random random = new Random();
        int waitTime = random.nextInt(100) % 30;
        Thread.sleep(waitTime);

        datas.put(oAuthToken.getRefreshToken(), oAuthToken.getPrincipal());
        semaphore.set(false);
        log.info("[SUCCESS] SAVE REFRESH TOKEN: {}", oAuthToken.getRefreshToken());
        return OAuthToken.builder().refreshToken(oAuthToken.getRefreshToken())
                .principal(oAuthToken.getPrincipal())
                .build();
    }

    @Override
    public void delete(OAuthToken oAuthToken) throws InterruptedException {
        waitingForUnlock(50);
        semaphore.set(true);
        if(datas.containsKey(oAuthToken.getRefreshToken())) {
            log.info("[SUCCESS] DELETE TOKEN: {}", oAuthToken.getRefreshToken());
            datas.remove(oAuthToken.getRefreshToken());
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
