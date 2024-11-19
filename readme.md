# Login API Server

Authentication & Authorization Server using JWT

- 단순 회원 가입 및 로그인 기능 지원
- Kakao OAuth2.0을 통한 로그인 기능 지원
- JWT를 활용한 인가 기능 지원

## Quick Start in Local
### 1. Pull & Create Frontend Image 
- ```git clone https://github.com/chrismrkr/login-test-frontend.git```
- Build Docker Image : ```docker build -t login-app-frontend ./```
### 2. Pull & Create Backend Image
- ```git clone https://github.com/chrismrkr/common-kakao-auth.git```
- Build Jar File : ```./gradlew clend build -x test```
- Build Docker Image : ```docker build -t login-api-was ./```
### 3. Set Configuration in docker.yaml
- 1. GO https://developers.kakao.com > CREATE 'My Application' > GET REST API Key > SET REST API KEY
  - SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT-ID: your-restapi-key
- 2. SET OAuth Redirect URI to http://localhost/api/login/oauth2/kakao in "https://developers.kakao.com > 'My App'"

### 4. Run in Docker Compose
```docker compose up```

## 개발 후기
https://okkkk-aanng.tistory.com/32

## 주요 API

#### 일반 계정 회원 가입 : POST /api/login/register

#### 일반 로그인 : POST /api/login

#### 카카오 로그인 : GET /api/oauth2/authorize/kakao

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


+ CSRF: 대응방안 필요

CSRF 공격을 피하기 위해서 Domain Check, Cookie Same Site 등의 추가적인 설정이 필요함.

