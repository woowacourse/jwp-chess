package chess.exception;

public enum InvalidStatus {

    COOKIE_NOT_FOUND("로그인이 필요합니다."),
    INVALID_COOKIE("해당 게임의 플레이어가 아닙니다."),
    INVALID_TURN("상대방이 움직일 차례입니다!"),
    GAME_NOT_FOUND("존재하지 않는 게임입니다."),
    GAME_NOT_OVER("아직 게임 결과가 산출되지 않았습니다."),
    NOT_ENOUGH_INPUT("필요한 정보가 입력되지 않았습니다."),
    INVALID_PASSWORD("잘못된 비밀번호를 입력하였습니다."),
    ;

    private final String message;

    InvalidStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
