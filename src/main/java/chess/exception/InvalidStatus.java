package chess.exception;

import org.springframework.http.HttpStatus;

public enum InvalidStatus {

    COOKIE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_COOKIE(HttpStatus.FORBIDDEN, "해당 게임의 플레이어가 아닙니다."),
    INVALID_TURN(HttpStatus.FORBIDDEN, "상대방이 움직일 차례입니다!"),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게임입니다."),
    GAME_NOT_OVER(HttpStatus.BAD_REQUEST, "아직 게임 결과가 산출되지 않았습니다."),
    NOT_ENOUGH_INPUT(HttpStatus.BAD_REQUEST, "필요한 정보가 입력되지 않았습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호를 입력하였습니다."),
    ;

    private final HttpStatus code;
    private final String message;

    InvalidStatus(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getCode() {
        return code;
    }
}
