# Test 환경설정

Goal : Test 환경에서만 사용할 Database를 설정해줄 수 있다.


## @DataJpaTest
이 어노테이션은 안에 포함된 어노테이션인 @AutoConfigureTestDatabase 가 동작해,
우리가 설정한 테스트DB 설정을 무시하고 테스트용 DB를 띄운다


## @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
해당 어노테이션을 사용하면 테스트시에서의 DB를 설정할 수 있다.
기본적으로 설정된 임시DB를 생성하는데, 이것을 NONE설정으로 막아주면 기본 세팅된 임시DB를 불러오지 않게되어,
우리가 application.yaml 에 설정해둔 db로 생성된다.

### h2 database를 의존성추가에서 제외하니까  @DataJpaTest에 설정된 자동 testDB를 생성해내지 못했다.
 -> 스프링내부에 h2 DB가 있다고한다. 별도의 설치없이 의존성 추가만으로 사용할 수 있다.
   -> 그리고 기본 세팅된 임시 DB는 h2를 활용하는 것으로 보여진다.


### replace=none 설정을 매번 해주기 번거롭다면
testdb 프로필에 이와 같은 기능을 해주는 설정값을 넣어줄 수 있다.
spring.test.database.replace: none (속성값으로 인식못하긴함)


## 접한 어노테이션들
+ @ActiveProfiles
+ @AutoConfigureTestDatabase
