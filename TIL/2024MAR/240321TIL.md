# 240321

## New
- MySQL 초기세팅
  - 권한설정 코드
    ~~~
    create user 'wooin'@'localhost' identified by 'password';
    select `user` from `mysql`.`user`;
    show grants for 'wooin'@'localhost'; //권한확인
    grant all on `dailyone`.* to 'wooin'@'localhost' with grant option; //계정에 권한 부여
    
    flush privileges; //권한 적용
- yaml 확장자로 세팅
  - application.yaml 파일에 세팅함
- jpa 문법
  - @Table 여기서 인덱스를 걸기
    - 그럼 여기서 걸린 인덱스는 어떤종류일지..?