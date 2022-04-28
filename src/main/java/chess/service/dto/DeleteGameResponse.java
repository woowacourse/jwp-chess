package chess.service.dto;

public class DeleteGameResponse {
    private int gameId;
    private boolean success;

    public DeleteGameResponse() {
    }

    public DeleteGameResponse(int gameId, boolean success) {
        this.gameId = gameId;
        this.success = success;
    }
//왜 게터가 있어야 하나? 잭슨 라이브러리가 구동하는 방식이라 그럼 -> 리플렉션 API를 통해 함(리플렉션은 메서드 정보만 가져올 수 있음)
    //게터 이름을 통해서 필드 이름을 유추!!!
    public int getGameId() {
        return gameId;
    }

    public boolean isSuccess() {
        return success;
    }
}
