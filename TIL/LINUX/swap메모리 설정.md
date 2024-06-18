# SWAP 메모리 설정

[참고링크](https://ittrue.tistory.com/297)

### SWAP 설정의 이유 : RAM이 부족해서.! (근데 RAM을 더 늘리기엔 애매할때)
우선, Swap 메모기 개념을 말하자면 가상메모리 같은 개념이다. 디스크의 일부를 메모리처럼 활용한다.
속도차이가 나긴하지만 RAM의 용량이 모자를 때와 같은 경우에 위기를 모면할 정도는 된다. 말그대로 모면하는 것.

```shell
#swap 파일 생성
sudo apt update
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile

#swap파일 실행 및 확인
sudo swapon /swapfile
sudo swapon --show
free -h

#재부팅시 자동실행설정
sudo vi /etc/fstab

LABEL=cloudimg-rootfs   /        ext4   discard,errors=remount-ro       0 1
LABEL=UEFI      /boot/efi       vfat    umask=0077      0 1
/swapfile swap swap defaults 0 0 # <- 이 부분 추가
```
