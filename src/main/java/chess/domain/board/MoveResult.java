package chess.domain.board;

public enum MoveResult {
    MOVE_SUCCESS(true, "이동에 성공하였습니다"),
    KILL_ENEMY(true, "이동에 성공하였습니다"),
    KILL_KING(true, "이동에 성공하였습니다"),
    HAS_OBSTACLE(false, "경로에 장애물이 있습니다"),
    EXISTING_SAME_TEAM(false, "같은 팀이 있는 곳은 갈 수 없습니다"),
    INVALID_TURN(false, "다른 팀의 차례입니다"),
    EMPTY_CELL(false, "해당 위치에 기물이 없습니다"),
    INVALID_MOVE_STRATEGY(false, "갈 수 없는 곳 입니다");

    private final boolean moveSuccess;
    private final String message;

    MoveResult(boolean moveSuccess, String message) {
        this.moveSuccess = moveSuccess;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMoveSuccess() {
        return moveSuccess;
    }
}
