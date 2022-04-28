package chess.dto;

public class GameDeleteResponseDto {
    private boolean isDelete;
    private String message;

    public GameDeleteResponseDto() {
    }

    public GameDeleteResponseDto(boolean isDelete, String message) {
        this.isDelete = isDelete;
        this.message = message;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public String getMessage() {
        return message;
    }
}
