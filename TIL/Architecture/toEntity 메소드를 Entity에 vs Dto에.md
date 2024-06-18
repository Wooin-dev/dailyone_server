# toEntity 메소드를 Entity에 vs Dto에

## tl;dr
- dto를 Entity로부터 만들거나 그 반대가 되는 메소드를 흔히 toEntity, fromEntity 등의 이름으로 만든다.
- 이 메소드는 어디에 만드는 것이 좋은가?
- Entity와 Dto의 의존성을 고려하여, Dto에 생성하는 것이 더 유연하다.

## 의존성을 고려

[참고링크 - 인프런 김영한님 답변](https://www.inflearn.com/questions/33616/dto-to-entity-%EC%8B%9C-%EC%8A%A4%ED%83%80%EC%9D%BC-%EC%A7%88%EB%AC%B8)

```
애플리케이션 전체 의존관계 관점으로 볼 때, 보통 여러 외부 서비스나 DTO들이 엔티티에 의존하는 구조가 됩니다.
memberFrom에서 Member 엔티티를 생성하는 것은 의존관계가 DTO -> Entity를 의존하기 때문에 일반적으로 문제가 없습니다. 반대로 Entity에서 DTO를 만들어 내는 부분이라면 의존관계가 Entity -> DTO가 되기 때문에 문제가 될 수 있습니다.
실무에서 form에서 데이터가 모두 완비되는 경우는 toEntity() 같은 식으로 해서 엔티티로 변환하는 것도 종종 사용합니다. 다만 form 데이터 만으로 생성하기 어려운 경우는 form에서 생성하는 기능을 사용하지 않습니다^^
추가로 아키텍처에 따라서 컨트롤러가 있는 프레젠테이션 계층과, 서비스 이후 계층을 완전히 분리하고 엔티티를 서비스 계층 밖으로 노출하지 않는 경우가 있습니다. 이렇게 제약이 있으면 프레젠테이션 계층의 form 객체도 엔티티를 생성(참조)하면 안됩니다.
```

[참고링크 - Spring/ DTO는 어디서, 어떻게 변환해야 할까?](https://velog.io/@jinny-l/Spring-DTO%EB%8A%94-%EC%96%B4%EB%94%94%EC%84%9C-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%B3%80%ED%99%98%ED%95%B4%EC%95%BC-%ED%95%A0%EA%B9%8C)
[[Spring Boot] dto의 toEntity를 어떻게 사용해야할까?](https://hangjastar.tistory.com/246)
[DTO <-> Entity 간 변환, 어느 Layer에서 하는게 좋을까?](https://velog.io/@jsb100800/Spring-boot-DTO-Entity-%EA%B0%84-%EB%B3%80%ED%99%98-%EC%96%B4%EB%8A%90-Layer%EC%97%90%EC%84%9C-%ED%95%98%EB%8A%94%EA%B2%8C-%EC%A2%8B%EC%9D%84%EA%B9%8C)

#### Dto에 생성
- toEntity() 는 매핑관계 있는 객체를 입력 파라미터로 non-static 메소드
- fromEntity() 는 static 메소드. entity를 입력 파라미터로 받음.