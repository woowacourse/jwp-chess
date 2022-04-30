package chess.dto;

public class MessageDto {

    private String message;

    private MessageDto() {
    }

    public MessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
