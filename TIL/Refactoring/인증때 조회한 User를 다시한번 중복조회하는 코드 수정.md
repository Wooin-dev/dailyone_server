# 인증때 조회한 User를 다시한번 중복조회하는 코드 수정

###### DONE을 Count하는 요청을 예시로 들어보겠다.

## AS-IS
> 요약하자면 매 인증때 token정보를 통해 User객체를 가져오는데, 거기에 담긴 email을 가지고 굳이 또 다시 User를 찾는다.
### JwtTokenFilter
- 인증을 필요로한 요청에서는 해당 JwtTokenFilter를 통해 유저정보를 가져오게 된다.
- 여기서 가져온 유저정보를 통해 Authentication객체를 만들고, 해당 객체를 SecurityContextHolder의 컨텍스트 내에 담아주게 되는 것.
- 이후 Controller단에서 해당 객체를 Authentication을 통해서 가져오게 된다.
```java
public class JwtTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ...
        try {
            ...
            //토큰 유효성 검사
            //토큰으로부터 email가져오기
            String email = JwtTokenUtils.getEmailFromToken(token, key);
            //email유효성 검사
            UserDto userDto = userService.loadUserByEmail(email); <=== 여기서 유저 조회가 발생!!
            //이 모든 과정이 통과되면 requset내의 컨텍스트에 넣어줌
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDto, null, userDto.getAuthorities()); <== 찾은 유저정보로 authentication 객체를 만들고
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication); <== 만든 객체를 set해줌. (컨트롤러단에서 Authentication 을 통해 가져오게 되는 객체가 전역으로 선언된 해당 SecurityContextHolder객체)
        } catch (RuntimeException e) {
            ...
        }
    }
}
```

### Controller
- 컨트롤러 단에서 promiseGoalId와 함께 인증단계에서 생성된 인증정보(authentication)을 가져온다.
- 그 후 인증정보에 getName을 통해 email정보를 가져와 service단으로 넘겨준다.

```java
@GetMapping("/count/{promiseGoalId}")
public Response<DoneCountResponse> doneCount(@PathVariable Long promiseGoalId, Authentication authentication) {
    Integer doneCount = doneService.countDoneByEmailAndGoalId(authentication.getName(), promiseGoalId);
    return Response.success(new DoneCountResponse(doneCount));
}
```

### Service
- 이메일을 정보를 가지고 다시 User객체를 조회한다. (이미 찾은 User객체)
- 그렇게 찾은 유저 객체로 
```java
public int countDoneByEmailAndGoalId(String email, Long promiseGoalId) {
    // User Exist
    User user = findUserByEmail(email);
    // Goal Exist
    Goal goal = findGoalById(goalId);
    // Count DONE
    return doneRepository.countByPromiseGoal_UserAndPromiseGoal_Goal(user, goal);
}
```


## TO-BE
> User 객체를 통해 userId값을 바로 전달 받아서, Service단에서 User객체 조회 없이 바로 countByUserId...() 할 수 있게 변경.

### Controller
-  UserDto userDto = (UserDto) authentication.getPrincipal(); 을 통해 바로 userDto.id()로 Service단에 userId전달
   
```java
@GetMapping("/count/{promiseGoalId}")
public Response<DoneCountResponse> doneCount(@PathVariable Long promiseGoalId, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        Integer doneCount = doneService.countDoneByEmailAndGoalId(userDto.id(), promiseGoalId);
        ...
}
```


```java
public int countDoneByEmailAndGoalId(Long userId, Long promiseGoalId) {
    // ##User 조회 생략##
    // Goal Exist
    Goal goal = findGoalById(goalId);
    // Count DONE
    return doneRepository.countByPromiseGoal_User_UserIdAndPromiseGoal_Goal(userId, goal);
}
```
