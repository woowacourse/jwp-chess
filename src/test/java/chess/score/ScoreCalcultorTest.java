package chess.score;

import spring.chess.board.ChessBoard;
import spring.chess.board.ChessBoardCreater;
import spring.chess.game.ChessSet;
import org.junit.jupiter.api.Test;
import spring.chess.score.Calculatable;
import spring.chess.score.Score;
import spring.chess.score.ScoreCalculator;

import static spring.chess.team.Team.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

class ScoreCalcultorTest {
    @Test
    void calculate() {
        ChessBoard chessBoard = ChessBoardCreater.create();
        Calculatable scoreCalculator = new ScoreCalculator();
        ChessSet chessSet = new ChessSet(chessBoard.giveMyPieces(WHITE));
        Score result = scoreCalculator.calculate(chessSet);
        assertThat(result).isEqualTo(new Score(38));
    }
}