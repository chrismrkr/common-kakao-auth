package common.loginapiserver.domain.dto;

import common.loginapiserver.domain.entity.enumerate.LoginResult;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LoginResponseDto {
    private LoginResult loginResult;
    private String responseMessage;

}
