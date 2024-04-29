# Optional 옵셔널


### 옵셔널을 통한 리팩토링
#### 기존코드
```
Optional<User> foundUser = userRepository.findByEmail(email);
if(foundUser.isPresent()) throw new DailyoneException();
```

#### 리팩토링
```

```