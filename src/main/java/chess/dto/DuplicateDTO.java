package chess.dto;

public class DuplicateDTO {
    private final boolean isDuplicate;

    public DuplicateDTO(final boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }
}
