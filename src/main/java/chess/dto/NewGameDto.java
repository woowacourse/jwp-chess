package chess.dto;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;

import java.util.Map;

public class NewGameDto {
    private final int gameId;
    private final Map<String, PieceDto> chessBoard;
    private final Color currentTurnColor;
    private final Map<Color, Double> colorsScore;

    public NewGameDto(int gameId, Map<String, PieceDto> chessBoard, Color currentTurnColor, Map<Color, Double> colorsScore) {
        this.gameId = gameId;
        this.chessBoard = chessBoard;
        this.currentTurnColor = currentTurnColor;
        this.colorsScore = colorsScore;
    }

    public static NewGameDto from(ChessGameManager chessGameManager, int gameId) {
        return new NewGameDto(
                gameId,
                ChessBoardDto.from(chessGameManager.getBoard()).board(),
                chessGameManager.getCurrentTurnColor(),
                chessGameManager.getStatistics().getColorsScore());
    }

    public int getGameId() {
        return gameId;
    }

    public Color getCurrentTurnColor() {
        return currentTurnColor;
    }

    public Map<Color, Double> getColorsScore() {
        return colorsScore;
    }

    public Map<String, PieceDto> getChessBoard() {
        return chessBoard;
    }
}
