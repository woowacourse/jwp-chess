package chess.dto;

public class ResponseDto {

    private final String code;
    private final String message;
    private final String turn;

    public ResponseDto(String code, String message, String turn) {
        this.code = code;
        this.message = message;
        this.turn = turn;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getTurn() {
        return turn;
    }
}
