package chess.dto;

public class RoomValidateDTO {
    private String code;
    private String message;

    public RoomValidateDTO(){}

    public RoomValidateDTO(String code, String message) {
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
