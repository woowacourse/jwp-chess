package chess.dto;

public class GameDeleteResponseDto {
    private static final String SUCCESS = "삭제에 성공했습니다.";

    private boolean isDeleted;
    private String message;

    public GameDeleteResponseDto() {
    }

    public GameDeleteResponseDto(boolean isDeleted, String message) {
        this.isDeleted = isDeleted;
        this.message = message;
    }

    public static GameDeleteResponseDto success() {
        return new GameDeleteResponseDto(true, SUCCESS);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getMessage() {
        return message;
    }
}
