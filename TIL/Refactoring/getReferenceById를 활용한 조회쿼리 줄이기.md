# getReferenceById를 활용한 조회쿼리 줄이기

>> 인증이 필요한 요청시에는 필터에서 User객체를 가져와서 UserDto의 타입으로 SecurityContextHolder에 Principal로 넣어주고 있다.  
>
>> Principal의 User객체의 Id, Email 등을 통해 User객체를 Select해 오는 것은 (인덱스가 있다면) 조회시간이 크진 않지만,  
>> Id값만 필요한데 Id값을 이미 알고 있는 상태에서 또 다시 Select하는 것은 불필요하다.
> 
>> 이럴때 프록시 객체를 통해, select 해오지 않고 연관관계에 있는 객체를 지정해 줄 수 있다.
>
>> 물론, 만약 트랜잭션이 flush되는 시점에서 해당 ID 값이 없다면 예외를 발생시킨다. (DataIntegrityViolationException)


## AS-IS
```java
@Transactional
public void create(GoalCreateRequest request, String email) {
    //user find
    User user = findUserByEmail(email); <- User 조회가 발생
    //goal save
    Goal goal = Goal.builderFromRequest(request).user(user).build();
    Goal savesGoal = goalRepository.save(goal);
    ...
```

## TO-BE
```java
@Transactional
public void create(GoalCreateRequest request, Long userId) { <- UserId를 받아옴
    //user find
    User user = userRepository.getReferenceById(userId); <- 받아온 UserId를 통해 프록시객체를 생성
    //goal save
    Goal goal = Goal.builderFromRequest(request).user(user).build();
    Goal savesGoal = goalRepository.save(goal);
```

## 결과
- 불필요한 User Select쿼리를 발생하지 않는다.
- 만약, User의 다른 정보에 접근해야 한다면 select를 Lazy하게 발생시키겠지만, 해당부분은 그런 경우라도 UserDto에서 바로 데이터를 가져올 수 있기에 이후 수정시에도 유연하다.


### 예외 실험
- 아래와 같이 존재하지 않는 Id값을 프록시 객체로 만들어서 진행했다.
```java
//user find
User user = userRepository.getReferenceById(9999L);
```
- 해당 프록시객체를 통해 외래키 등록하여 Goal을 save하는 내용이 flush 되는 시점에서 예외가 발생한다.
```
# 예외 발생 로그
Completing transaction for [com.wooin.dailyone.service.GoalService.create] 
  after exception: org.springframework.dao.DataIntegrityViolationException: could not execute statement
```
