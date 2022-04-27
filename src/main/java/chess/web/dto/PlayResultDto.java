package chess.web.dto;

import chess.domain.piece.Piece;
import java.util.HashMap;
import java.util.Map;

public class PlayResultDto {
    private Map<String, Piece> board = new HashMap<>();
    private String turn;

    private PlayResultDto(Map<String, Piece> board, String turn) {
        this.board = board;
        this.turn = turn;
    }

    public static PlayResultDto of(Map<String, Piece> board, String turn) {
        return new PlayResultDto(board, turn);
    }

    public Map<String, Piece> getBoard() {
        return board;
    }

    public String getTurn() {
        return turn;
    }
}
