package chess.exception;

public enum ErrorInformation {
    INVALID_MOVE(400, "이동할 수 없습니다."),
    ALREADY_FINISHED(400, "이미 끝난 상태입니다."),
    INVALID_TURN(400, "한 턴이 끝난 경우에는 기물을 움직일 수 없습니다."),
    INVALID_PIECE(400, "옳지 않은 기물입니다!"),
    INVALID_TARGET(400, "같은 색깔의 기물 위치로는 이동할 수 없습니다."),
    SAME_LOCATION(400, "source위치와 같은 위치로는 이동할 수 없습니다.");

    private int statusCode;
    private String errorMessage;

    ErrorInformation(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
