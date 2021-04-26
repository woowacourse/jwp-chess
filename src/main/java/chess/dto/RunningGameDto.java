package chess.dto;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;

import java.util.Map;

public class RunningGameDto {
    private final Map<String, PieceDto> chessBoard;
    private final Color currentTurnColor;
    private final Map<Color, Double> colorsScore;
    private final boolean isEnd;

    public RunningGameDto(Map<String, PieceDto> chessBoard, Color currentTurnColor, Map<Color, Double> colorsScore, boolean isEnd) {
        this.chessBoard = chessBoard;
        this.currentTurnColor = currentTurnColor;
        this.colorsScore = colorsScore;
        this.isEnd = isEnd;
    }

    public static RunningGameDto from(ChessGameManager chessGameManager) {
        if (chessGameManager.isEnd()) {
            return EndGameDto.from(chessGameManager);
        }
        return new RunningGameDto(
                ChessBoardDto.from(chessGameManager.getBoard()).board(),
                chessGameManager.getCurrentTurnColor(),
                chessGameManager.getStatistics().getColorsScore(),
                chessGameManager.isEnd());
    }

    public Map<String, PieceDto> getChessBoard() {
        return chessBoard;
    }

    public Color getCurrentTurnColor() {
        return currentTurnColor;
    }

    public Map<Color, Double> getColorsScore() {
        return colorsScore;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
