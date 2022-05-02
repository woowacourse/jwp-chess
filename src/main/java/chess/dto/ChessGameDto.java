package chess.dto;

import chess.domain.ChessGame;
import chess.domain.Room;

public class ChessGameDto {

    private Long id;
    private final Room room;
    private final boolean running;

    private ChessGameDto(final Long id, final Room room, final boolean running) {
        this.id = id;
        this.room = room;
        this.running = running;
    }

    public static ChessGameDto from(final ChessGame game) {
        return new ChessGameDto(game.getId(), game.getRoom(), game.isInProgress());
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public boolean isRunning() {
        return running;
    }
}
