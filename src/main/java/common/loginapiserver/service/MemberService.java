package common.loginapiserver.service;

import common.loginapiserver.dto.MemberRequestDto;

public interface MemberService {
    void save(MemberRequestDto memberRequestDto);
}
