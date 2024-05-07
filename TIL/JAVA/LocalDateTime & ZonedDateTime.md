# LocalDateTime & ZonedDateTime

### Review
```
UTC개념을 확실히 알게 됐다.
리액트(클라이언트)에서 서버로 Date를 보내주는데 UTC로 보내주었다.
이 Date값은 String형태로 JSON의 값으로 전달받기에 이를 @DateTimeFormat을 통해 파싱하려 했다.

이 과정에서 UTC로 인식하여 자동으로 시스템상의 타임존으로 변환되지 않게 됐다.

클라이언트)
2024-05-05T15:00:00.000Z

서버)
2024-05-05T15:00  -> 2024-05-06T00:00으로 변환되길 기대했으나 변환되지 않음

이러한 상황에서 UTC로 받은 임의의 시간날짜 데이터를 이용해
해당 날짜에 속하는 엔티티를 조회하기 위해, 하루의 시작과 끝을 구했음
```

## toLocalDateTime( ) 메소드에 대한 오해풀기

`ZonedDateTime zdt = 2024-05-05T16:00:00.123123+09:00[Asia/Seoul]`\
위와 같은 zdt가 있다.

``zdt.toLocalDateTime();``\
이를 실행하면 시차를 보정해준 UTC로 변경이 될까?

---

#### ZonedDateTime.class 들여다보면
```
public final class ZonedDateTime implements ... {
    ...
    private final LocalDateTime dateTime;
    private final ZoneOffset offset;
    private final ZoneId zone;
    ...
    @Override
    public LocalDateTime toLocalDateTime() {
        return dateTime;
    }
```
- toLocalDateTime() 메소드는 필드로 가지고 있던 dateTime을 그대로 반환할 뿐이다.


## withZoneSameInstant() 메소드 활용
```
ZonedDateTime zdt = createdAt.atZone(ZoneId.of("UTC"));
ZonedDateTime zdtKST = zdt.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
```
- 위와 비슷한 방식으로 변환했다.
- UTC임을 지정한 후에 이와 같은 Instant의 KST로 가져다준다.
  - Instant는 절대시간이라고 이해하면 편하다.

#### withZoneSameInstant( ) 메소드 살펴보기
```
@Override
public ZonedDateTime withZoneSameInstant(ZoneId zone) {
    Objects.requireNonNull(zone, "zone");
    return this.zone.equals(zone) ? this :
        create(dateTime.toEpochSecond(offset), dateTime.getNano(), zone);
}
```
- 별도의 ZoneId가 들어오면 create(dateTime.toEpochSecond(offset), dateTime.getNano(), zone); 이 실행된다.
  <br><br>
#### create( ) 메소드 살펴보기
```
private static ZonedDateTime create(long epochSecond, int nanoOfSecond, ZoneId zone) {
      ZoneRules rules = zone.getRules();
      Instant instant = Instant.ofEpochSecond(epochSecond, nanoOfSecond);  // TODO: rules should be queryable by epochSeconds
      ZoneOffset offset = rules.getOffset(instant);
      LocalDateTime ldt = LocalDateTime.ofEpochSecond(epochSecond, nanoOfSecond, offset);
      return new ZonedDateTime(ldt, offset, zone);
}
```
- ZonedDateTime을 생성해주는 static 메소드이다.
- zone에서 ZoneRules를 가져와 ZoneOffset을 만들어 이를 통해 생성한다.

### 결과
```
전) 2024-05-05T15:00:00.000000Z
후) 2024-05-06T00:00:00.000000+09:00[Asia/Seoul]
```
- 위처럼 같은 Instant를 시차에 맞게 표기하게 되었다.

[참고링크](https://www.daleseo.com/java8-zoned-date-time/)