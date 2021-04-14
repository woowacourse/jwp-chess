### 기능 구현 목록
- 기존 체스 도메인 코드 합치기
- 체스 게임 api 목록
    - GET /api/room : 체스 게임 방 목록을 받아온다.
    - POST /api/pieces : 체스 방에 대한 기물 위치 정보를 받아온다.
    - PUT /api/board
      - 요청 : source, target 
      - 응답 : 기물 위치 정보, 게임 진행 여부, 승자 
    - GET /api/score : 색상에 따른 점수 정보를 받아온다.
    