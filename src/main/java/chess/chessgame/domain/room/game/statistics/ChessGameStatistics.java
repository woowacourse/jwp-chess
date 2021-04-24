package chess.chessgame.domain.room.game.statistics;

import chess.chessgame.domain.room.game.board.Board;
import chess.chessgame.domain.room.game.board.InitBoardInitializer;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;

import java.util.Collections;
import java.util.Map;

public class ChessGameStatistics {
    private final Map<Color, Double> colorsScore;
    private final MatchResult matchResult;

    public ChessGameStatistics(Map<Color, Double> colorsScore, MatchResult matchResult) {
        this.colorsScore = colorsScore;
        this.matchResult = matchResult;
    }

    public static ChessGameStatistics createNotStartGameResult() {
        Board defaultBoard = InitBoardInitializer.getBoard();
        return new ChessGameStatistics(defaultBoard.getScoreMap(), MatchResult.DRAW);
    }

    public Map<Color, Double> getColorsScore() {
        return Collections.unmodifiableMap(colorsScore);
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public String getResultText() {
        return this.matchResult.getText();
    }
}
