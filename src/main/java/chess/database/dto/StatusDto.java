package chess.database.dto;

import chess.domain.Color;
import chess.domain.game.GameState;
import chess.dto.BoardResponse;
import java.util.HashMap;
import java.util.Map;

public class StatusDto {

    private BoardResponse board;
    private Map<Color, Double> score;

    public StatusDto(GameState gameState) {
        board = BoardResponse.of(gameState.getPointPieces());
        score = new HashMap<>(gameState.getColorScore());
    }

    public void setBoard(BoardResponse board) {
        this.board = board;
    }

    public void setScore(Map<Color, Double> score) {
        this.score = score;
    }

    public BoardResponse getBoard() {
        return board;
    }

    public Map<Color, Double> getScore() {
        return Map.copyOf(score);
    }
}
