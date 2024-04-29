# Soft Delete
- 실제 삭제하는 것이 아닌, 삭제표시만 하고 표시된 데이터는 불러오지 않는 방식
- 실제 삭제요청을 해도 관리입장에서는 데이터를 보관해야할 경우가 있으므로 이런 경우 활용 가능.
- 회원 탈퇴 후 1년간 보관 등


### User 엔티티에 deleteAt 추가
```java
@Column(name = "deleted_at")
private Timestamp deletedAt;
```

### DELETE 메소드 시에 추가로 발생시킬 쿼리를 설정
```java
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id=?")
```

### 조회시 삭제시간이 없는 데이터만 조회하게
```java
@SQLRestriction("deleted_at is NULL")
```