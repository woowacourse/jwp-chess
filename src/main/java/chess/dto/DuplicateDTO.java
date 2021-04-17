package chess.dto;

public class DuplicateDTO {
    private boolean isDuplicate;

    public DuplicateDTO(boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }
}
