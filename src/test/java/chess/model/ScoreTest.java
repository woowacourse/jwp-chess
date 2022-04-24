package chess.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import chess.model.board.ChessInitializer;
import chess.model.board.Score;
import chess.model.board.Square;
import chess.model.piece.Piece;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScoreTest {

    @Test
    @DisplayName("점수계산 로직 검사")
    void of() {
        Map<Square, Piece> initPieces = new ChessInitializer().initPieces();
        Score initScores = Score.of(initPieces);
        Map<Color, Double> scoresPerColor = initScores.getScoresPerColor();
        assertAll(() -> {
            assertThat(scoresPerColor.get(Color.WHITE)).isEqualTo(38);
            assertThat(scoresPerColor.get(Color.BLACK)).isEqualTo(38);
        });
    }
}