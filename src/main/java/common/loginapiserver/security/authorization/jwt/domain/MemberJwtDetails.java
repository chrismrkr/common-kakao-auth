package common.loginapiserver.security.authorization.jwt.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.*;
import java.util.Base64;

@Getter
public class MemberJwtDetails implements Serializable{
    private String grantType;
    private String accessToken;
    private Long accessTokenExpirationTime;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
    @Builder
    private MemberJwtDetails(String grantType, String accessToken, Long accessTokenExpirationTime, String refreshToken, Long refreshTokenExpirationTime) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshToken = refreshToken;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }


    public String serializeToString() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(this);
        }
        return Base64.getUrlEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static MemberJwtDetails deserializeFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getUrlDecoder().decode(string);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (MemberJwtDetails) objectInputStream.readObject();
        }
    }
}
