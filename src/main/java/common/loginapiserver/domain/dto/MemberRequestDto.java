package common.loginapiserver.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{5,10}+$"
            , message = "영문으로 시작하고, 소문자 영문 숫자 조합 6-11자리만 허용합니다.")
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String authType;
}
