# API 문서

## Room 추가

POST /rooms

```json
{
  "requestBody": {
    "name": "string",
    "white": "string",
    "black": "string"
  },
  "response": {
    "id": "string",
    "name": "string",
    "white": "string",
    "black": "string"
  }
}
```

## Room 닫기

DELETE /rooms/:roomId

```json
{
}
```

## Room 안에 있는 User들의 전적 불러오기

GET /rooms/:roomId/statistics

```json
{
  "response": {
    "whiteName": "string",
    "whiteWin": "string",
    "whiteLose": "string",
    "blackName": "string",
    "blackWin": "string",
    "blackLose": "string"
  }
}
```

## Room 안에 있는 게임의 상태 불러오기

GET /rooms/:roomId/game-status

```json
{
  "response": {
    "gameState": "string",
    "turn": "string",
    "winner": "string"
  }
}
```

## Room 안에 있는 게임 시작하기, 종료하기

PUT /rooms/:roomId/game-status

```json
{
  "requestBody": {
    "gameState": "string"
  },
  "response": {
    "board": [
      {
        "team": "string",
        "piece": "string",
        "x": "string",
        "y": "string"
      },
      {
        "team": "string",
        "piece": "string",
        "x": "string",
        "y": "string"
      },
      ...
    ]
  }
}
```



## Room 안에 있는 게임의 선택된 Point에서 이동할 수 있는 Point 불러오기

GET /rooms/:roomId/points/:point/movable-points

```json
{
  "response": {
    "points": 
      [
        {
          "x": "string",
          "y": "string"
        },
        {
          "x": "string",
          "y": "string"
        }
        ...
      ]
    }
  }
}
```

## Room 안에 있는 게임에서 말의 이동

POST /rooms/:roomId/movement

```json
{
  "requestBody": {
    "source": "string",
    "destination": "string"
  },
  "response": {
    "board": [
      {
        "team": "string",
        "piece": "string",
        "x": "string",
        "y": "string"
      },
      {
        "team": "string",
        "piece": "string",
        "x": "string",
        "y": "string"
      },
      ...
    ]
  }
}
```