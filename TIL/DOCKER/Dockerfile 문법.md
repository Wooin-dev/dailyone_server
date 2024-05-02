# Dockerfile 문법

## 기본 파라미터
- FROM 
  - 베이스가 되는 이미지
- WORKDIR
  - 폴더를 생성하고 이후 명령어가 실행되는 위치로 이동한다.
  - mkdir aaa & cd /aaa 과 비슷한 동작
- COPY
  - 복사하기
- ADD
  - COPY와 비슷. 알집파일의 경우 압축을 해제하여 붙여넣는다.
- RUN
  - 이미지를 만들때 실행되는 명령어
- ENTRYPOINT
  - 컨테이너가 실행되고 동작할 명령어
- CMD
  - 컨테이너가 실행되고 ENTRYPOINT가 실행된 후 추가로 실행될 명령어
  - ENTRYPOINT와 비슷하지만 이는 컨테이너가 실행될 때, 다른 명령어로 대체될 수 있다.



### 같은 명령어는 두줄보단 && 로 합쳐주기
- RUN 명령어 두줄보다는 한줄로
  - 한줄마다 레이어가 생기는데 가급적 레이어를 줄여주자
    ```
    RUN aaa
    RUN bbb     ->    RUN aaa && bbb
    ```

#### 참고링크
- https://coding789.tistory.com/190