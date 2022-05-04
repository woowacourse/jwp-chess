package chess.dto;

import chess.domain.Color;
import chess.domain.board.Board;

public class GameStateDto {

    private final boolean end;
    private final String turn;

    private GameStateDto(boolean end, Color turn) {
        this.end = end;
        this.turn = turn.name().toLowerCase();
    }

    public static GameStateDto from(Board board) {
        return new GameStateDto(board.isEnd(), board.getTurn());
    }

    public boolean getEnd() {
        return end;
    }

    public String getTurn() {
        return turn;
    }
}
