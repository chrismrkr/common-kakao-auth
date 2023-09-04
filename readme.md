# Login API Server

목표: 인증 및 인가(Token 방식) 기능 제공 API 서버 구현 : OAuth2.0 활용

Client 웹 페이지에서 서버로 인증(Authentication) 요청을 하면, 리소스 인가 요청을 위한 JWT 토큰을 반환하는 API 서버를 제공함.

## 동작 과정

### Authentication(인증)

클라이언트는 아래의 과정을 통해 AccessToken을 전달 받는다.

AccessToken에는 리소스 접근을 위한 권한이 존재하고, 클라이언트는 이를 Cookie에 저장하여 사용한다.

![oauth2-flow](https://github.com/chrismrkr/common-kakao-auth/assets/62477958/54815ca1-b80e-4937-a0aa-e5383b2b1dae)

### Authorization(인가)

클라이언트가 서버에 Request를 보내면 아래의 과정으로 Response를 받는다.

+ 첫째, JwtAuthorizationFilter에서 Access Token의 권한 정보를 추출한 후, 이를 Security Context에 저장한다.

만약 Access Token이 만료되었다면, 적절한 과정을 통해 Access Token을 새로 발급한다.

+ 둘째, FilterSecurityInterceptor에서 Security Context에 저장된 권한 정보를 확인한다.

만약, 적절한 권한이라면 클라이언트에 리소스를 response하고, 그렇지 않은 경우는 AccessDeniedHandler에 따라 처리된다.

FilterSecurityInterceptor 참고 링크: https://github.com/chrismrkr/WIL/blob/main/Spring/springSecurity/springSecuritySummary.md#27-filtersecurityinterceptor-authorization

![jwt-auth-flow](https://github.com/chrismrkr/common-kakao-auth/assets/62477958/b5ebe6c2-c81a-4e19-81ca-34b9338b626c)



