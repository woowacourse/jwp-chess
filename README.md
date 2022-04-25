# **기능 목록**

- 체스판을 초기화한다.
    - 가로 위치는 왼쪽부터 a ~ h이고, 세로는 아래부터 위로 1 ~ 8로 구분한다.
- 흰팀, 검은팀은 소문자, 대문자로 구분한다.
- [Error] 잘못된 명령을 입력하면 예외를 발생시킨다.
- [Error] 기물이 없는 위치에서 기물을 찾으려하면 예외를 발생시킨다.
- [Error] 다른 팀 기물을 이동하려고 하면 예외를 발생시킨다.

## **움직임 전략**

### **공통**

- [Error] 현재 위치에서 이동할 수 없는 곳으로 이동하려고 하면 예외를 발생시킨다.
- [Error] 움직이려는 위치에 같은 팀 기물이 있으면 예외를 발생시킨다.
- 이동한다.
    - 이동하려는 위치에 상대 기물이 있으면 상대 기물을 삭제하고 이동한다.
        - 폰은 대각선 이동만으로 상대 기물을 잡을 수 있다.

### **King**

- 상하좌우, 대각선 방향으로 각각 1칸씩만 움직일 수 있다.

### **Queen**

- 상하좌우, 대각선 방향으로 기물이 없는 칸에 한해서 칸수의 제한 없이 움직일 수 있다.

### **Look**

- 상하좌우 방향으로 기물이 없는 칸에 한해서 칸수의 제한 없이 움직일 수 있다.

### **Bishop**

- 대각선 방향으로 기물이 없는 칸에 한해서 칸수의 제한 없이 움직일 수 있다.

### **Knight**

- 수직 방향으로 한칸 움직인 후 수평 방향으로 두칸 움직이거나 수직 방향으로 두칸 움직인 후 수평 방향으로 한칸 움직인다.
- 다른 기물을 넘어다닐 수 있다.

### **Pawn**

- 바로 앞의 칸이 비어 있다면 앞으로 한 칸 전진할 수 있다.
- 경기중 단 한번도 움직이지 않은 폰은 바로 앞의 두칸이 비어 있을 때 두칸 전진할 수 있다.(한칸만 전진해도 된다.)
- [Error] 바로 앞에 상대 기물 혹은 첫번째 전진에서 두 칸 앞의 상대 기물을 잡으려하면 예외를 발생시킨다.
- [Error] 상대 기물이 대각선에 존재하지 않는데, 대각선 전진을 하려고 하면 예외를 발생시킨다.
- [Error] 폰이 뒤로 이동하려고 하면 예외를 발생시킨다.
- 폰은 상대 출발한 지점에서 가장 먼 rank에 도착했을 때, 킹을 제외한 다른 기물로 바뀔 수 있다.

## **팀 점수**

- 각 말의 점수는 king은 0점, queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점이다.
- pawn의 기본 점수는 1점이다. 하지만 같은 세로줄에 같은 색의 폰이 있는 경우 1점이 아닌 0.5점을 준다.

## **방**
- 체스방 제목과 비밀번호를 입력하면 새로운 체스판(체스방)을 만들 수 있다.
    - 만드려는 체스방의 제목이 이미 존재한다면 사용자에게 알려준다.
- 체스방에는 고유 식별 값이 부여된다.
- 체스방 목록에서, 체스방 삭제 버튼을 누르면 체스방을 삭제할 수 있다.
    - 비밀번호가 틀리다면 사용자에게 비밀번호가 틀리다고 알려준다.
    - 진행중인 체스방을 삭제하려고 하면 사용자에게 진행중인 체스방이라고 알려준다.
- 체스방 목록에서, 체스방 제목을 입력하면 게임을 이어서 진행할 수 있다.