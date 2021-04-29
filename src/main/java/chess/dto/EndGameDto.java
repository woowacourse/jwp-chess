package chess.dto;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.domain.statistics.MatchResult;

import java.util.Map;

public class EndGameDto extends RunningGameDto {
    private final MatchResult matchResult;

    public EndGameDto(Map<String, PieceDto> chessBoard, Color currentTurnColor, Map<Color, Double> colorsScore, boolean isEnd, MatchResult matchResult) {
        super(chessBoard, currentTurnColor, colorsScore, isEnd);
        this.matchResult = matchResult;
    }

    public static EndGameDto from(ChessGameManager chessGameManager) {
        return new EndGameDto(
                ChessBoardDto.from(chessGameManager.getBoard()).board(),
                chessGameManager.getCurrentTurnColor(),
                chessGameManager.getStatistics().getColorsScore(),
                chessGameManager.isEnd(),
                chessGameManager.getStatistics().getMatchResult()
        );
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }
}
