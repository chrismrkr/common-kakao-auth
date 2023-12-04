package common.loginapiserver.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{5,10}+$"
            , message = "영문으로 시작하고, 소문자 영문 숫자 조합 6-11자리만 허용합니다.")
    private String loginId;
    @NotBlank
    private String password;
}
