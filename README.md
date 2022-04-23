## Spring Web Chess

- Spring MVC, Spring JDBC

## 기능 요구사항

체스 게임을 진행할 수 있는 방을 만들어서 동시에 여러 게임이 가능하도록 하기

### 홈 화면 "/"

- [x] 새로운 게임 버튼과 기존 게임들의 목록을 조회하는 버튼이 제공된다.
    - [x] "새로운 게임 시작" 버튼을 누르면 "/game" 경로로 이동한다.
    - [x] "기존 게임 재개" 버튼을 누르면 "/games" 경로로 이동한다.
- [x] 기본적으로 현재 생성된 게임의 개수와 현재 진행 중인 게임의 개수가 조회된다.

### 게임 생성 화면 "/game"

- [x] 방 이름과 비밀번호를 입력하면 새로운 체스 게임이 생성된다.
    - [x] 개별 체스 게임에는 고유 식별값 id가 부여된다.
    - [x] 데이터베이스에는 비밀번호의 해쉬 값이 저장된다.
- [x] 생성하려는 방 이름과 중복되는 게임이 이미 존재하는 경우, 예외가 발생하며 알람이 뜬다.

### 방 목록 조회 화면 "/games"

- [x] 체스방 제목이 표시된 버튼들로 구성된 목록이 제시된다.
    - [x] 각 버튼을 클릭하면 해당되는 게임으로 이동한다.
- [x] id를 검색하여 해당되는 게임으로 이동하는 폼도 제공된다.
    - [x] id에 해당되는 게임이 존재하지 않는 경우 알람이 뜬다.
- [x] 기본적으로 현재 생성된 게임의 개수와 현재 진행 중인 게임의 개수가 조회된다.

### 게임 플레이 화면 "/game/:id"

- [x] id에 대응되는 체스게임의 현재 상태가 화면에 제공된다.
- [x] 체스말을 클릭하고, 특정 체스칸을 클릭하면 해당 위치로 이동시키고자 시도한다.
    - [x] 선택된 체스말을 다시 클릭하면 선택이 해제된다.
    - [x] 해당 체스말이 이동할 수 없는 위치거나, 현재 움직일 수 있는 상태인 경우 예외가 발생한다.
    - [x] 이동에 실패하는 경우 알람에 그 이유가 제시된다.
- [x] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.
- [x] 게임이 종료된 경우 결과 조회 화면으로 이동하는 버튼이 제공된다.

### 결과 조회 화면 "/result/:id"

- [x] id에 대응되는 체스게임의 마지막 보드 상태와 점수 및 승자 정보가 화면에 제공된다.
- [ ] 체스방 생성시 설정한 비밀번호를 활용하여 체스 게임을 삭제할 수 있다.
    - [x] 입력된 비밀번호의 해쉬값은 DB에 저장된 해쉬값과 대조되어 삭제 가능 여부가 판단된다.
- [x] 진행중인 게암의 삭제는 불가능하다.
    - [x] 서버에서도 종료된 게임에 대해서만 제거를 시도하며, 실패한 경우 예외를 발생시킨다.

### 프로그래밍 요구사항

- [x] 예외가 발생했을 때 사용자가 이해할 수 있는 명시적인 메시지를 응답한다