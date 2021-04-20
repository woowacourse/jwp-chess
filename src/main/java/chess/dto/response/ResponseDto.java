package chess.dto.response;

public class ResponseDto {

    private final int code;
    private final String message;
    private final chess.dto.responsedto.ResponseDto data;

    public ResponseDto(ResponseCode responseCode) {
        this(responseCode, null);
    }

    public ResponseDto(ResponseCode responseCode, chess.dto.responsedto.ResponseDto data) {
        this(responseCode.getCode(), responseCode.getMessage(), data);
    }

    public ResponseDto(int code, String message) {
        this(code, message, null);
    }

    public ResponseDto(int code, String message, chess.dto.responsedto.ResponseDto data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public chess.dto.responsedto.ResponseDto getData() {
        return data;
    }
}
