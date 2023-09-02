# Login API Server

목표: 인증 및 인가(Token 방식) 기능 제공 API 서버 구현 : OAuth2.0 활용

Client 웹 페이지에서 서버로 인증(Authentication) 요청을 하면, 리소스 인가 요청을 위한 JWT 토큰을 반환하는 API 서버를 제공함.

## 동작 과정

### Authentication(인증)

클라이언트는 아래의 과정을 통해 AccessToken을 전달받는다. AccessToken에는 리소스 접근을 위한 권한정보가 존재한다.

![oauth2-flow](https://github.com/chrismrkr/common-kakao-auth/assets/62477958/54815ca1-b80e-4937-a0aa-e5383b2b1dae)

### Authorization(인가)
 
