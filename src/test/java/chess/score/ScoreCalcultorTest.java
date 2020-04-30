package chess.score;

import chess.board.ChessBoard;
import chess.board.ChessBoardCreater;
import chess.player.ChessSet;
import org.junit.jupiter.api.Test;

import static chess.team.Team.WHITE;
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