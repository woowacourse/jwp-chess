package chess.dto;

import chess.domain.board.ChessGame;

public final class GameRoomResponse {

    private final String id;
    private final String name;

    public GameRoomResponse(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static GameRoomResponse from(final ChessGame chessGame) {
        return new GameRoomResponse(chessGame.getId(), chessGame.getName());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ChessGameDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
