# 도커로 ec2에 배포하며

## <전체적인 프로세스>
```text
- 개발환경에서 이미지를 생성 후 도커허브로 푸시
- ec2에서 도커허브에 푸시된 이미지를 PULL하여 RUN
```
## TODO
- 배포자동화
- 도커허브에 푸시한 이미지에 대한 보안사항 공부

## 발생했던 이슈들
### 맥북 M1칩 이슈 (amd64로 설정)
```text
docker build --platform linux/amd64 -t donedev/dailyone-web .

 -> 옵션값으로 --platform linux/amd64 추가해주기
 ec2의 리눅스는 amd64.
```
#### 에러메시지
`
The requested image's platform (linux/arm64/v8) does not match the detected host platform (linux/amd64) 
and no specific platform was requested
`

### 컨테이너에서 호스트로 접근하기 (도커를 켠 로컬)
- 컨테이너 실행할 때 옵션값을 추가해줘야한다. 
```
ex)
docker run -d -p 8080:8080 --name {컨테이너 이름} 
--add-host host.docker.internal:host-gateway {image 이름}
```
### 트러블 : ec2인스턴스의 극심한 퍼포먼스 저하
- 스프링 부트를 실행한 컨테이너에서 호스트의 mysql로 접속하게 되어있는데, 극심한 퍼포먼스 저하가 발생하고 컨테이너가 다운된다.
- 이미지 생성시 아키텍쳐를 변경하는 이슈가 있는데, 임의로 변경해준 부분이 문제가 있을지.?
- 호스트의 DB를 바라보는게 문제가 있을지?


### MySQL 연동시
- bind-address 를 0.0.0.0으로 변경하여 접근가능해짐
- 참고로, ec2의 DB에 접근하려고 했을때 적용했으나 이제는 구조가 변경되어 적용되지 않은 사항들이다.
- 참고자료
  - https://blogshine.tistory.com/322 