package chess.dto;

import chess.domain.ChessGame;
import chess.domain.Room;

public class ChessGameDto {

    private Long id;
    private final Room room;
    private final String gameProgress;
    private final String gameProgressBadge;

    public ChessGameDto(final Long id, final Room room, final String gameProgress, final String gameProgressBadge) {
        this.id = id;
        this.room = room;
        this.gameProgress = gameProgress;
        this.gameProgressBadge = gameProgressBadge;
    }

    public static ChessGameDto toDto(final ChessGame game) {
        return new ChessGameDto(game.getId(), game.getRoom(),
                createGameProgress(game.isInProgress()), createGameProgressBadge(game.isInProgress()) );
    }

    private static String createGameProgressBadge(final boolean inProgress) {
        if (inProgress) {
            return "badge bg-success";
        }
        return "badge bg-danger";
    }

    private static String createGameProgress(final boolean inProgress) {
        if (inProgress) {
            return "게임 진행 중";
        }
        return "게임 종료";
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public String getGameProgress() {
        return gameProgress;
    }

    public String getGameProgressBadge() {
        return gameProgressBadge;
    }
}
