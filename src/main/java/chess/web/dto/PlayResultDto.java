package chess.web.dto;

import chess.domain.piece.Piece;
import java.util.Map;

public class PlayResultDto {
    private Map<String, Piece> board;
    private String turn;
    private boolean isFinished;

    private PlayResultDto(Map<String, Piece> board, String turn, boolean isFinished) {
        this.board = board;
        this.turn = turn;
        this.isFinished = isFinished;
    }

    public static PlayResultDto of(Map<String, Piece> board, String turn, boolean isFinished) {
        return new PlayResultDto(board, turn, isFinished);
    }

    public Map<String, Piece> getBoard() {
        return board;
    }

    public String getTurn() {
        return turn;
    }

    public boolean getIsFinished() {
        return isFinished;
    }
}
