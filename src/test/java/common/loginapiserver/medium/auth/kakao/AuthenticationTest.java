package common.loginapiserver.medium.auth.kakao;

import common.loginapiserver.security.authentication.oauth.infrastructure.CookieAuthorizationRequestRepository;
import common.loginapiserver.security.authentication.oauth.service.CustomOAuth2UserService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthenticationTest {
    @Autowired
    CustomOAuth2UserService oAuth2UserService;
    @Autowired
    CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("[Success] handle Authentication Request")
    void success() throws Exception {
        // given
        // both uri saved in application-oauth.properties
        String kakaoAuthorizationUri = "https://kauth.kakao.com/oauth/authorize";
        String redirectServerUri = "http://localhost:8080/login/oauth2/kakao";
        Cookie cookie = new Cookie("oauth2_auth_request", "rO0ABXNyAExvcmcuc3ByaW5nZnJhbWV3b3JrLnNlY3VyaXR5Lm9hdXRoMi5jb3JlLmVuZHBvaW50Lk9BdXRoMkF1dGhvcml6YXRpb25SZXF1ZXN0AAAAAAAAAjoCAApMABRhZGRpdGlvbmFsUGFyYW1ldGVyc3QAD0xqYXZhL3V0aWwvTWFwO0wACmF0dHJpYnV0ZXNxAH4AAUwAFmF1dGhvcml6YXRpb25HcmFudFR5cGV0AEFMb3JnL3NwcmluZ2ZyYW1ld29yay9zZWN1cml0eS9vYXV0aDIvY29yZS9BdXRob3JpemF0aW9uR3JhbnRUeXBlO0wAF2F1dGhvcml6YXRpb25SZXF1ZXN0VXJpdAASTGphdmEvbGFuZy9TdHJpbmc7TAAQYXV0aG9yaXphdGlvblVyaXEAfgADTAAIY2xpZW50SWRxAH4AA0wAC3JlZGlyZWN0VXJpcQB-AANMAAxyZXNwb25zZVR5cGV0AFNMb3JnL3NwcmluZ2ZyYW1ld29yay9zZWN1cml0eS9vYXV0aDIvY29yZS9lbmRwb2ludC9PQXV0aDJBdXRob3JpemF0aW9uUmVzcG9uc2VUeXBlO0wABnNjb3Blc3QAD0xqYXZhL3V0aWwvU2V0O0wABXN0YXRlcQB-AAN4cHNyACVqYXZhLnV0aWwuQ29sbGVjdGlvbnMkVW5tb2RpZmlhYmxlTWFw8aWo_nT1B0ICAAFMAAFtcQB-AAF4cHNyABdqYXZhLnV0aWwuTGlua2VkSGFzaE1hcDTATlwQbMD7AgABWgALYWNjZXNzT3JkZXJ4cgARamF2YS51dGlsLkhhc2hNYXAFB9rBwxZg0QMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4cD9AAAAAAAAAdwgAAAAQAAAAAHgAc3EAfgAHc3EAfgAJP0AAAAAAAAx3CAAAABAAAAABdAAPcmVnaXN0cmF0aW9uX2lkdAAFa2FrYW94AHNyAD9vcmcuc3ByaW5nZnJhbWV3b3JrLnNlY3VyaXR5Lm9hdXRoMi5jb3JlLkF1dGhvcml6YXRpb25HcmFudFR5cGUAAAAAAAACOgIAAUwABXZhbHVlcQB-AAN4cHQAEmF1dGhvcml6YXRpb25fY29kZXQA92h0dHBzOi8va2F1dGgua2FrYW8uY29tL29hdXRoL2F1dGhvcml6ZT9yZXNwb25zZV90eXBlPWNvZGUmY2xpZW50X2lkPTdjODMwZDdjMDM0NTkzMmY3YmRlZWM5MzdhOTRiZDM5JnNjb3BlPXByb2ZpbGVfbmlja25hbWUlMjBhY2NvdW50X2VtYWlsJnN0YXRlPU9uN3pPZmx6dmhCVUJQMW50WVBxSjlvOXRTZ201ZExfZ2Z3SmppalZtaDAlM0QmcmVkaXJlY3RfdXJpPWh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9sb2dpbi9vYXV0aDIva2FrYW90ACdodHRwczovL2thdXRoLmtha2FvLmNvbS9vYXV0aC9hdXRob3JpemV0ACA3YzgzMGQ3YzAzNDU5MzJmN2JkZWVjOTM3YTk0YmQzOXQAKGh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9sb2dpbi9vYXV0aDIva2FrYW9zcgBRb3JnLnNwcmluZ2ZyYW1ld29yay5zZWN1cml0eS5vYXV0aDIuY29yZS5lbmRwb2ludC5PQXV0aDJBdXRob3JpemF0aW9uUmVzcG9uc2VUeXBlAAAAAAAAAjoCAAFMAAV2YWx1ZXEAfgADeHB0AARjb2Rlc3IAJWphdmEudXRpbC5Db2xsZWN0aW9ucyRVbm1vZGlmaWFibGVTZXSAHZLRj5uAVQIAAHhyACxqYXZhLnV0aWwuQ29sbGVjdGlvbnMkVW5tb2RpZmlhYmxlQ29sbGVjdGlvbhlCAIDLXvceAgABTAABY3QAFkxqYXZhL3V0aWwvQ29sbGVjdGlvbjt4cHNyABdqYXZhLnV0aWwuTGlua2VkSGFzaFNldNhs11qV3SoeAgAAeHIAEWphdmEudXRpbC5IYXNoU2V0ukSFlZa4tzQDAAB4cHcMAAAAED9AAAAAAAACdAAQcHJvZmlsZV9uaWNrbmFtZXQADWFjY291bnRfZW1haWx4dAAsT243ek9mbHp2aEJVQlAxbnRZUHFKOW85dFNnbTVkTF9nZndKamlqVm1oMD0=");

        // when
        ResultActions perform = mockMvc.perform(get("/api/oauth2/authorize/kakao")
                        .cookie(cookie)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        // then
        MockHttpServletResponse response = perform.andReturn().getResponse();
        log.info("[response] {}", response.getContentAsString());
        Assertions.assertEquals(302, response.getStatus());

        // given
        String code = "BH3BibvU8cLjgaoo5TOD35-F3BiJmhwocz6evFy5uWDQFKYddGJZoPzpXgcKPXWcAAABjBGtZyhONYg--5I0Sw";
        String state = "I5NnaDcEeEuWHiZOJiMQF2_SeZSQ69qZshFO6La2R0Q=";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("code", code);
        queryParams.add("state", state);

        // when
        ResultActions perform2 = mockMvc.perform(get("/api/login/oauth2/kakao")
                        .queryParams(queryParams)
                        .cookie(cookie))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        // then
        MockHttpServletResponse response1 = perform.andReturn().getResponse();
        log.info("[response] {}", response1.getContentLength());
    }
}
