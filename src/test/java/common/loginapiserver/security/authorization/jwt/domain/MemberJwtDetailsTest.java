package common.loginapiserver.security.authorization.jwt.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class MemberJwtDetailsTest {

    @Test
    void MemberJwtDetails를_직렬화_및_역직렬화할_수_있음() throws IOException, ClassNotFoundException {
        // given
        MemberJwtDetails build = MemberJwtDetails.builder()
                .grantType("jwt")
                .accessToken("abc")
                .accessTokenExpirationTime(1L)
                .refreshToken("bcd")
                .refreshTokenExpirationTime(2L)
                .build();
        // when
        String serialized = build.serializeToString();
        MemberJwtDetails deserialized = MemberJwtDetails.deserializeFromString(serialized);
        // then
        Assertions.assertEquals(build.getAccessToken(), deserialized.getAccessToken());
    }
}
