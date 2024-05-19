# TIP

## format
- 변수를 직접 넣을 수 있어서 가독성도 좋고, 확장성도 좋아보인다.
```java
String.format("%s. %s", errorCode.getMessage(), message);
```


## Map 에서 cast메소드 사용하기
```java
.map(UserDto.class::cast) // ::은 메소드 참조 문법 java 8
.map((dto) -> UserDto.class.cast(dto)) //같은 표현. 임의의 변수를 지정해줘야 한다.'dto'
```
```java
(UserDto) dto -> 이렇게 캐스팅해주는 방식을 메소드로 지원
        //결과적으로 동일하다
        .map(UserDto.class::cast)
        .map((dto) -> (UserDto)dto)
```
### 메소드로 캐스팅할때 차이?
- #### 결과적으로 같은 결과다.

### 추가, Null 캐스팅이 가능할까?
`String nullStr = (String)null;`
 - null을 캐스팅 하는것이 자바에서 가능하다.
```
@IntrinsicCandidate
    public T cast(Object obj) {
        if (obj != null && !isInstance(obj))
            throw new ClassCastException(cannotCastMsg(obj));
        return (T) obj;
    }
```
- cast메소드를 보면 null이어도 예외를 발생시키진 않는다.
- null이 아닐때, 객체가 들어왔는지만 예외를 던지고 결과적으로 직접 형변환하는 캐스팅형태를 return해준다.