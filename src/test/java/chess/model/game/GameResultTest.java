package chess.model.game;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.Color;
import chess.model.board.Score;
import chess.model.board.Square;
import chess.model.piece.Bishop;
import chess.model.piece.King;
import chess.model.piece.Piece;
import chess.model.piece.Queen;
import chess.model.piece.pawn.Pawn;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameResultTest {

    @Test
    @DisplayName("일반적인 상황 점수로 결과 내기")
    void from() {
        Map<Square, Piece> board = Map.of(
                Square.of("a1"), Pawn.of(Color.WHITE),
                Square.of("a2"), Pawn.of(Color.BLACK),
                Square.of("a4"), new Queen(Color.WHITE),
                Square.of("a5"), new Bishop(Color.BLACK),
                Square.of("a6"), new King(Color.WHITE),
                Square.of("a7"), new King(Color.BLACK)
        );
        GameResult gameResult = GameResult.from(board, Score.of(board));
        assertThat(gameResult.getWinnerColor()).isEqualTo(Color.WHITE);
    }

    @Test
    @DisplayName("폰이 세로줄에 겹치는 상황 점수로 결과 내기")
    void fromWhiteHasDuplicatedPawnInFile() {
        Map<Square, Piece> board = Map.of(
                Square.of("a1"), Pawn.of(Color.WHITE),
                Square.of("b1"), Pawn.of(Color.BLACK),
                Square.of("a2"), Pawn.of(Color.WHITE),
                Square.of("c3"), Pawn.of(Color.BLACK),
                Square.of("a6"), new King(Color.WHITE),
                Square.of("a7"), new King(Color.BLACK)
        );
        GameResult gameResult = GameResult.from(board, Score.of(board));
        assertThat(gameResult.getWinnerColor()).isEqualTo(Color.BLACK);
    }

    @Test
    @DisplayName("한 쪽이 왕이 없는 경우 결과 내기")
    void fromOnlyWhiteKing() {
        Map<Square, Piece> board = Map.of(
                Square.of("b1"), Pawn.of(Color.BLACK),
                Square.of("c3"), Pawn.of(Color.BLACK),
                Square.of("a6"), new King(Color.WHITE)
        );
        GameResult gameResult = GameResult.from(board, Score.of(board));
        assertThat(gameResult.getWinnerColor()).isEqualTo(Color.WHITE);
    }
}
