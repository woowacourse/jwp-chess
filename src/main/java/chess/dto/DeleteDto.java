package chess.dto;

public class DeleteDto {
    private final long id;
    private final boolean success;

    public DeleteDto(long id, boolean success) {
        this.id = id;
        this.success = success;
    }
}
