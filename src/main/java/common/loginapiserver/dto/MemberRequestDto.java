package common.loginapiserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberRequestDto {
    private final String loginId;
    private final String password;
    private final String nickname;
    private final String email;
    private final String phoneNumber;
    private final String authType;
}
