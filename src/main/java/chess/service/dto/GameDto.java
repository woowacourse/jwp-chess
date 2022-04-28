package chess.service.dto;

import chess.domain.game.Color;
import chess.domain.game.status.End;
import chess.domain.game.status.GameStatus;
import chess.domain.game.status.Playing;

public class GameDto {
    private final int id;
    private final String title;
    private final int password;
    private final boolean status;
    private final String turn;

    public GameDto(int id, String title, int password, boolean status, String turn) {
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

    public GameStatus getStatus() {
        if (status) {
            return new Playing();
        }
        return new End();
    }

    public Color getTurn() {
        return Color.from(turn);
    }

    public int getPassword() {
        return password;
    }
}
