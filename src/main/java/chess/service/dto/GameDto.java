package chess.service.dto;

import chess.domain.game.Color;
import chess.domain.game.Status;

public class GameDto {
    private final int id;
    private final String title;
    private final boolean status;
    private final String turn;

    public GameDto(int id, String title, boolean status, String turn) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.turn = turn;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        if (status) {
            return Status.PLAYING;
        }
        return Status.END;
    }

    public Color getTurn() {
        return Color.from(turn);
    }
}
