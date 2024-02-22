package common.loginapiserver.member.controller.dto;

import common.loginapiserver.member.controller.dto.enumerate.LoginResult;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LoginResponseDto {
    private LoginResult loginResult;
    private String responseMessage;

}
