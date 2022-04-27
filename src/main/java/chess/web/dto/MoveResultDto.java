package chess.web.dto;

import chess.domain.piece.Piece;
import java.util.Map;

public class MoveResultDto {
    private boolean isMovable;
    private boolean isGameOver;
    private String winner;
    private Map<String, Piece> board;

    public MoveResultDto(Map<String, Piece> board) {
        isMovable = true;
        isGameOver = false;
        this.board = board;
    }

    public boolean getIsMovable() {
        return isMovable;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public String getWinner() {
        return winner;
    }

    public Map<String, Piece> getBoard() {
        return board;
    }

    public void setIsMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }

    public void setIsGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
