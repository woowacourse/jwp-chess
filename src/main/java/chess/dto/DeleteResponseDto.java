package chess.dto;

public class DeleteResponseDto {
    private boolean isDeleted;

    public DeleteResponseDto(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public static DeleteResponseDto success() {
        return new DeleteResponseDto(true);
    }

    public static DeleteResponseDto fail() {
        return new DeleteResponseDto(false);
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }
}
