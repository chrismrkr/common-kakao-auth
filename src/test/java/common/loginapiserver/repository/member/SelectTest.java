package common.loginapiserver.repository.member;

import common.loginapiserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
public class SelectTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

//    @Test
//    @DisplayName("SELECT Member WHERE kakao_id matched")
//    void test1() {
//        Map<String, Object> attributes = new LinkedHashMap<>();
//        attributes.put("id", 1L);
//        Map<String, Object> properties = new LinkedHashMap<>();
//        properties.put("nickname", "303호");
//        attributes.put("properties", properties);
//        Map<String, Object> kakao_account = new LinkedHashMap<>();
//        kakao_account.put("email", "test@kakao.com");
//        attributes.put("kakao_account", kakao_account);
//        // given
//        AuthInfo kakaoAuthInfo = new AuthInfo(attributes, "KAKAO");
//        Member save = Member.builder().oAuth2Info(kakaoAuthInfo).build();
//        memberRepository.save(save);
//        em.clear();
//        // when
//        Optional<Member> byKakaoId = memberRepository.findByOAuth2Id(1L);
//        // then
//        Assertions.assertEquals(save.getId(), byKakaoId.get().getId());
//    }
}
