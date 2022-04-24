package chess.service.dto;

import chess.model.game.ChessGame;
import chess.model.gamestatus.StatusType;

public class GameEntity {
    private String status;
    private String turn;
    private int id;
    private String name;

    public GameEntity() {
    }

    public GameEntity(int id, String name, String status, String turn) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.turn = turn;
    }

    public GameEntity(int id, ChessGame chessGame) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }
}
