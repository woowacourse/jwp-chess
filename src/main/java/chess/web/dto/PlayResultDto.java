package chess.web.dto;

import chess.domain.piece.Piece;
import java.util.HashMap;
import java.util.Map;

public class PlayResultDto {
    private Map<String, Piece> board = new HashMap<>();
    private String turn;

    public PlayResultDto(Map<String, Piece> board, String turn) {
        this.board = board;
        this.turn = turn;
    }

    public Map<String, Piece> getBoard() {
        return board;
    }

    public String getTurn() {
        return turn;
    }

    public void setBoard(Map<String, Piece> board) {
        this.board = board;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }
}
