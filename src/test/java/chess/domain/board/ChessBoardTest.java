package chess.domain.board;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.exception.NullObjectSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ChessBoardTest {
    private ChessBoard chessBoard;

    @BeforeEach
    void setUp() {
        this.chessBoard = BoardFactory.createBoard();
    }

    @DisplayName("포지션을 받아 해당 위치의 Piece를 리턴한다.")
    @Test
    void findByPositionTest() {
        assertThat(chessBoard.getPieceByPosition(Position.of("a1"))).isInstanceOf(Piece.class);
    }

    @DisplayName("이동할 때 해당 위치에 말이 없으면 예외")
    @Test
    void throwExceptionWhenSquareHasNotPiece() {
        assertThatThrownBy(() -> chessBoard.move(Position.of("a3"), Position.of("b3")))
                .isInstanceOf(NullObjectSelectionException.class)
                .hasMessage("해당 위치에는 말이 없습니다.");
    }

    @DisplayName("말을 움직인다.")
    @Test
    void movePiece() {
        Piece piece = chessBoard.getPieceByPosition(Position.of("b2"));
        chessBoard.move(Position.of("b2"), Position.of("b3"));

        assertThat(chessBoard.getPieceByPosition(Position.of("b3"))).isEqualTo(piece);
    }
}
