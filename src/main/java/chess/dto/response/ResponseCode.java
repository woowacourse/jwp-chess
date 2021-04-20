package chess.dto.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    NOT_EXISTING_ROW(HttpStatus.BAD_REQUEST.value(), "해당하는 이름의 Row가 존재하지 않습니다."),
    NOT_EXISTING_PIECE(HttpStatus.BAD_REQUEST.value(), "입력 값에 해당하는 말이 없습니다."),
    PAWN_TWO_STEP_MOVE(HttpStatus.BAD_REQUEST.value(), "폰은 초기 자리에서만 두칸 이동 가능합니다."),
    PAWN_DIAGONAL_MOVE(HttpStatus.BAD_REQUEST.value(), "폰은 상대 말을 먹을 때만 대각선으로 이동이 가능합니다."),
    PAWN_FORWARD_MOVE(HttpStatus.BAD_REQUEST.value(), "폰은 한칸 앞 말이 있으면 가지 못합니다."),
    EMPTY_CANNOT_MOVE(HttpStatus.BAD_REQUEST.value(), "빈 공간을 옮길 수 없습니다."),
    CANNOT_MOVE_POSITION(HttpStatus.BAD_REQUEST.value(), "이동할 수 없는 위치입니다."),
    MY_PIECE_MOVE(HttpStatus.BAD_REQUEST.value(), "자신의 말만 옮길 수 있습니다."),
    GAME_OVER(HttpStatus.BAD_REQUEST.value(), "이미 게임이 끝났습니다."),
    GAME_START(HttpStatus.BAD_REQUEST.value(), "이미 게임이 시작했습니다."),
    GAME_NOT_START(HttpStatus.BAD_REQUEST.value(), "아직 게임이 시작되지 않았습니다.");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;

    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
