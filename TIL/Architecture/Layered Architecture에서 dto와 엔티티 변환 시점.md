# Layered Architecture에서 dto와 엔티티 변환 시점

```
Entity를 받아온 후엔 계층간의 이동에서는 Dto를 활용한다.  
```

## Controller에서 변경 vs Service변경
- 크게 위 두가지의 시점에서 Entity <-> Dto로의 변환이 이루어진다.
- 어느하나가 맞다라기 보단 각각의 장단점이 있다.

## Service단에서 변경하는 방식을 적용
- 우선 나는 Service단에서 Entity를 Dto로 변환하였다.
  ### 선택이유
  #### 컨트롤러에 들어온 요청을 처리할 서비스 메소드는 하나로 하고 싶었다.
  - 각각의 엔티티를 받아와서 컨트롤러단에서 여러 데이터를 처리하는 비즈니스 로직이 등장하게 됐을때, 이후 비슷한 기능의 메소드를 다른 컨트롤러에서 활용하기 어려웠던 과거의 경험
    - -> 이 말은 곧 Service코드의 재사용성이 떨어진다는 의미가 된다.
  - 컨트롤러단의 가독성을 높이고 싶었다.
  #### 도메인 변경이 잦은 시기
  - 현재 개발 초기로 도메인의 변경이 많아질 수 있다.
  - 만약 엔티티(도메인)이 Controller단까지 가져오게 되면 이에 따라 컨트롤러단까지 영향을 줄 수 있다.
    - -> 컨트롤러단이 엔티티에 의존성을 갖는다는 의미
    - 의존성을 낮게 가져가고 싶다.









#### 참고자료
- https://everenew.tistory.com/280
- https://velog.io/@jsb100800/Spring-boot-DTO-Entity-간-변환-어느-Layer에서-하는게-좋을까
- https://velog.io/@dbtlwns/Spring-DTO의-변환위치
- https://tecoble.techcourse.co.kr/post/2021-04-25-dto-layer-scope/
- https://www.slipp.net/questions/93
- https://dont-be-evil.tistory.com/314
- https://www.inflearn.com/questions/248322/dto-%EB%B3%80%ED%99%98-%EB%A1%9C%EC%A7%81%EC%9D%98-%EC%9C%84%EC%B9%98
- https://www.inflearn.com/questions/139564
- https://velog.io/@smc2315/DTO-Entity-변환-메서드-구현-위치는-어디가-좋을까
- https://quokkaman.medium.com/layered-architecture에서-데이터-클래스-분류-dto-entity-vo-적용과-dto와-request-response-객체-분리-ff2d0ea925d6
- https://ksh-coding.tistory.com/102
