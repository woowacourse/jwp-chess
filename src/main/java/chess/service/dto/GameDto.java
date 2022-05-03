package chess.service.dto;

import chess.domain.game.Color;

public class GameDto {
    private final int id;
    private final String title;
    private final String password;
    private final String status;
    private final String turn;

    public GameDto(int id, String title, String password, String status, String turn) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.status = status;
        this.turn = turn;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Color getTurn() {
        return Color.from(turn);
    }

    public String getPassword() {
        return password;
    }
}
