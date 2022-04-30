package chess.dto;

import chess.domain.board.ChessGame;

public final class GameRoomResponse {

    private final String id;
    private final String name;
    private final String password;

    public GameRoomResponse(final String id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static GameRoomResponse from(final ChessGame chessGame) {
        return new GameRoomResponse(chessGame.getId(), chessGame.getName(), chessGame.getPassword());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ChessGameDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
