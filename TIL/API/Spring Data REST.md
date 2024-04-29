# Spring Data REST + HAL
###### API 빠르게 구성하기
###### 공식문서 https://docs.spring.io/spring-data/rest/reference/

## 설정 방법

### 1. 의존성 추가
- https://start.spring.io/ 홈페이지에서 의존성 검색
- build.gradle 추가 후 리프레시
### 2. application.yaml 설정 추가
- base-path 
- detection-strategy: annotated 설정의 경우에 @RepositoryRestResource 어노테이션이 붙은 곳만 불러온다.

### 3. HAL Explorer
- 의존성을 추가했다면, 실행 뒤 localhost:8080/{base-path} 입력시 해당 페이지에 진입가능