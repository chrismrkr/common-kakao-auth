package common.loginapiserver.medium.auth.api;

import common.loginapiserver.member.controller.dto.MemberRequestDto;
import common.loginapiserver.member.controller.port.MemberService;
import common.loginapiserver.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthenticationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;
    @Test
    @DisplayName("[Authentication Success] return AccessToken in cookies")
    void loginSuccess() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("test1")
                .password("1234")
                .authType("NORMAL")
                .build();
        memberService.createMember(memberRequestDto.toMember());

        // when
        String requestBody = "{" +
                "\"loginId\": \"" + "test1" + "\", " +
                "\"password\": \"" + "1234" + "\"" +
                "}";
        ResultActions perform = mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[redirect URL] {}", response.getRedirectedUrl());
        Assertions.assertEquals(302, response.getStatus());
    }
    @Test
    @DisplayName("[Authentication Fail] INCORRECT PASSWORD")
    @Rollback(value = true)
    void loginFailure() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("test2")
                .password("1234")
                .authType("NORMAL")
                .build();
        memberService.createMember(memberRequestDto.toMember());

        // when
        String requestBody = "{" +
                "\"loginId\": \"" + "test2" + "\", " +
                "\"password\": \"" + "1111" + "\"" +
                "}";
        ResultActions perform = mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[redirect URL] {}", response.getRedirectedUrl());
        Assertions.assertEquals(302, response.getStatus());
    }

    @Test
    @DisplayName("[Authentication Fail] INCORRECT LOGINID")
    @Rollback(value = true)
    void loginFailure2() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("test3")
                .password("1234")
                .authType("NORMAL")
                .build();
        memberService.createMember(memberRequestDto.toMember());

        // when
        String requestBody = "{" +
                "\"loginId\": \"" + "testunknown" + "\", " +
                "\"password\": \"" + "1234" + "\"" +
                "}";
        ResultActions perform = mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[redirect URL] {}", response.getRedirectedUrl());
        Assertions.assertEquals(302, response.getStatus());
    }

    @Test
    @DisplayName("[Authentication Fail] LOGINID OR PASSWORD IS BLANK")
    void loginFailure3() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("test4")
                .password("1234")
                .authType("NORMAL")
                .build();
        memberService.createMember(memberRequestDto.toMember());

        // when
        String requestBody = "{" +
                "\"loginId\": \"" + " " + "\", " +
                "\"password\": \"" + " " + "\"" +
                "}";
        ResultActions perform = mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[redirect URL] {}", response.getRedirectedUrl());
        Assertions.assertEquals(302, response.getStatus());
    }
}
