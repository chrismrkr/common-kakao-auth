# Login API Server

Authentication & Authorization Server using JWT

- 단순 회원 가입 및 로그인 기능 지원
- Kakao OAuth2.0을 통한 로그인 기능 지원
- JWT를 활용한 인가 기능 지원

## 간단하게 Docker-compose로 실행하는 방법
#### Step 1. 프로젝트 pull
#### Step 2. docker image build
```shell
# 프로젝트 루트 디렉토리에서 실행
docker build -t login-api-was ./
```
#### Step 3. REST API 키 발급 및 Redirect URI 설정

https://developers.kakao.com 카카오 개발자 홈페이지에 접속하여 '내 애플리케이션'을 생성하여 REST API 키를 발급하고 카카오 로그인에서 사용할 OAuth Redirect URI를 설정한다.

OAuth Redirect URI는 http://localhost:80/api/login/oauth2/kakao로 설정한다.

#### Step 4. compose.yaml 수정

발급받은 REST API Key를 SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT-ID에 설정한다.

```yaml
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT-ID: [나의 REST API KEY]
```

#### Step 5. docker compose로 실행
```shell
docker compose up
```
컨테이너 실행 후, http 80 포트로 통신할 수 있음

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

## Access Token, Refresh Token 관리 방식

+ Access Token: httpOnly=true Cookie에 담아서 클라이언트에 전달함.
+ Refresh Token: 서버 DB에 저장함. 만약 Access Token이 만료되면, DB의 Refresh Token을 확인하여 신규 Access Token 발급 여부를 결정함.

## 보안성 진단

+ XSS: 안전함

JWT를 HttpOnly=true Cookie로 전달하므로 XSS 공격을 피할 수 있음

+ CSRF: 대응방안 필요

CSRF 공격을 피하기 위해서 Domain Check, Cookie Same Site 등의 추가적인 설정이 필요함.

