package chess.domain.entity;

public class Result {
    private final int id;
    private final int roomId;
    private final int winnerId;
    private final int loserId;

    public Result(final int id, final int roomId, final int winnerId, final int loserId) {
        this.id = id;
        this.roomId = roomId;
        this.winnerId = winnerId;
        this.loserId = loserId;
    }
}