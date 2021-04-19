package chess.domain.board;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.exception.NoSuchPermittedChessPieceException;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTest {

    @DisplayName("자기말 외에는 움직일 수 없다.")
    @Test
    void move() {
        Board board = new Board(Arrays.asList(Piece.createPawn(Color.WHITE, 0, 0)));

        assertThatThrownBy(() -> {
            board.moveAndCatchPiece(Color.BLACK, new Position(0, 0), new Position(1, 0));
        }).isExactlyInstanceOf(NoSuchPermittedChessPieceException.class);
    }
}