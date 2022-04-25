package chess.dao;

import chess.model.game.ChessGame;
import chess.model.gamestatus.StatusType;

public class GameEntity {
    private final String status;
    private final String turn;
    private final int id;
    private String name;

    public GameEntity(final int id, final String name,
                      final String status, final String turn) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.turn = turn;
    }

    public GameEntity(final int id, final ChessGame chessGame) {
        this.id = id;
        this.status = StatusType.findByStatus(chessGame.getStatus()).name();
        this.turn = chessGame.getTurn().name();
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status.toUpperCase();
    }

    public String getTurn() {
        return turn.toUpperCase();
    }

    public int getId() {
        return id;
    }
}
