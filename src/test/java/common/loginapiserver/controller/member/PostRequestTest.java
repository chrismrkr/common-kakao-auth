package common.loginapiserver.controller.member;

import common.loginapiserver.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PostRequestTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("1. Success: Member Create")
    void success() throws Exception {
        // given
        String requestBody = "{" +
                "\"loginId\": \"" + "chris961022" + "\", " +
                "\"password\": \"" + "12341234" + "\", " +
                "\"nickname\": \"" + "303" + "\", " +
                "\"email\": \"" + "b635032@naver.com" + "\", " +
                "\"phoneNumber\": \"" + "01096663052" + "\", " +
                "\"email\": \"" + "b635032@naver.com" + "\", " +
                "\"authType\": \"" + "KAKAO" + "\" " +
                "}";
        // when
        MvcResult result = mockMvc.perform(post("/member")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // then
        MockHttpServletResponse response = result.getResponse();
        log.info("[result] : {} ", response.getContentAsString());
        Assertions.assertEquals(1, memberRepository.findAll().size());
    }

    @Test
    @DisplayName("2. Failure: Member Create(Invalid loginId")
    void fail1() throws Exception {
        // given
        String requestBody = "{" +
                "\"loginId\": \"" + "123s112123312" + "\", " +
                "\"password\": \"" + "12341234" + "\", " +
                "\"nickname\": \"" + "303" + "\", " +
                "\"email\": \"" + "b635032@naver.com" + "\", " +
                "\"phoneNumber\": \"" + "01096663052" + "\", " +
                "\"email\": \"" + "b635032@naver.com" + "\", " +
                "\"authType\": \"" + "KAKAO" + "\" " +
                "}";
        // when
        MvcResult result = mockMvc.perform(post("/member")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // then
        MockHttpServletResponse response = result.getResponse();
        log.info("[result] : {} ", response.getContentAsString());
        Assertions.assertEquals(0, memberRepository.findAll().size());
    }
}
