import spring.chess.board.ChessBoardCreater;
import spring.chess.game.ChessSet;
import spring.chess.board.ChessBoard;
import spring.chess.score.Score;
import spring.chess.team.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChessSetTest {
    @DisplayName("스코어 계산")
    @Test
    void calculateScore() {
        ChessBoard chessBoard = ChessBoardCreater.create();
        ChessSet chessSet = new ChessSet(chessBoard.giveMyPieces(Team.BLACK));

        Score result = chessSet.calculateScoreExceptPawnReduce();
        assertThat(result).isEqualTo(new Score(38));
    }
}