package chess.dto;

public class DuplicateDto {
    private final boolean isDuplicate;

    public DuplicateDto(final boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }
}
