package chess.score;

import spring.chess.board.ChessBoard;
import spring.chess.board.ChessBoardCreater;
import spring.chess.game.ChessSet;
import spring.chess.location.Location;
import spring.chess.piece.type.Piece;
import spring.chess.score.Score;
import spring.chess.team.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.chess.score.ScoreCalculator;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreCalculatorTest {
    @Test
    @DisplayName("점수 계산")
    void calculateScore() {
        ChessBoard chessBoard = ChessBoardCreater.create();
        Map<Location, Piece> chessBoardPieces = chessBoard.giveMyPieces(Team.WHITE);
        ScoreCalculator scoreCalculator = new ScoreCalculator();
        ChessSet chessSet = new ChessSet(chessBoardPieces);
        assertThat(scoreCalculator.calculate(chessSet))
                .isEqualTo(new Score(38));
    }
}