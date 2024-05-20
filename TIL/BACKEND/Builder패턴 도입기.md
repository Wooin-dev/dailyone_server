# 빌더패턴 도입기
```
불변객체인 DTO를 Record 타입으로 만들고 그 생성자에 Builder패턴을 추가해,
toEntity 등 다양한 방식의 생성패턴을 빌더패턴으로 모두 대응함. 
```
- 다양한 생성패턴을 모두 생성자를 만드는 것의 한계
- 임의로 설정하기에 위험한 필드(Id 등)를 제외하기 위해 생성자에 @Builder 추가

#### 참고링크
- https://velog.io/@mooh2jj/%EC%98%AC%EB%B0%94%EB%A5%B8-%EC%97%94%ED%8B%B0%ED%8B%B0-Builder-%EC%82%AC%EC%9A%A9%EB%B2%95