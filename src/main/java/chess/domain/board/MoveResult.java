package chess.domain.board;

public enum MoveResult {
    MOVE_SUCCESS(true, "기물이 성공적으로 움직였습니다."),
    KILL_ENEMY(true, "움직인 기물이 상대 기물을 잡았습니다."),
    KILL_KING(true, "움직인 기물이 상대 킹을 잡았습니다."),
    HAS_OBSTACLE(false, "출발 지점과 도착 지점 사이에 장애물이 존재해 움직일 수 없습니다."),
    EXISTING_SAME_TEAM(false, "도착 지점에 같은편 기물이 존재해 움직일 수 없습니다."),
    INVALID_TURN(false, "상대방의 기물을 움직일 수 없습니다."),
    EMPTY_CELL(false, "출발 지점에 아무 기물도 존재하지 않아 움직일 수 없습니다."),
    INVALID_MOVE_STRATEGY(false, "선택된 기물은 해당 위치로 이동할 수 없습니다.");

    private final boolean moveSuccess;
    private final String message;

    MoveResult(boolean moveSuccess, String message) {
        this.moveSuccess = moveSuccess;
        this.message = message;
    }

    public boolean isMoveSuccess() {
        return moveSuccess;
    }

    public String getMessage() {
        return message;
    }
}
