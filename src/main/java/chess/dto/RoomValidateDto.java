package chess.dto;

public class RoomValidateDto {
    private String code;
    private String message;

    public RoomValidateDto() {
    }

    public RoomValidateDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
