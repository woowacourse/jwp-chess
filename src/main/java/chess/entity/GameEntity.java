package chess.entity;

import chess.model.game.ChessGame;

public class GameEntity {
    private final String status;
    private final String turn;
    private final int id;
    private String name;
    private String password;

    public GameEntity(final int id, final String name,
                      final String status, final String turn, final String password) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.turn = turn;
        this.password = password;
    }


    public GameEntity(final Integer id, final ChessGame chessGame) {
        this.id = id;
        this.status = chessGame.getStatus().name();
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

    public String getPassword() {
        return password;
    }
}
