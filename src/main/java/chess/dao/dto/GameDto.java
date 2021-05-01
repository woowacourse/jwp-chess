package chess.dao.dto;

import java.time.LocalDateTime;

public class GameDto {

    private final long id;
    private final String turn;
    private final boolean isFinished;
    private final LocalDateTime createdTime;

    private GameDto(final long id, final String turn, final boolean isFinished,
        final LocalDateTime createdTime) {

        this.id = id;
        this.turn = turn;
        this.isFinished = isFinished;
        this.createdTime = createdTime;
    }

    public static GameDto of(final long id, final String turn,
        final boolean isFinished, final LocalDateTime createdTime) {

        return new GameDto(id, turn, isFinished, createdTime);
    }

    public long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

}
