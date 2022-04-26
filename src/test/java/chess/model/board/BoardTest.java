package chess.model.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.model.Color;
import chess.model.File;
import chess.model.Rank;
import chess.model.piece.Piece;
import chess.model.piece.Rook;
import chess.model.piece.pawn.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(new ChessInitializer());
    }

    @Test
    void createTest() {
        assertThat(board).isInstanceOf(Board.class);
    }

    @Test
    void findPiece() {
        Piece a2 = board.findPieceBySquare(Square.of("a2"));
        assertThat(a2).isEqualTo(Pawn.of(Color.WHITE));
    }

    @Test
    void getTest() {
        Piece a1Piece = board.findPieceBySquare(Square.of(File.A, Rank.ONE));
        Piece h8Piece = board.findPieceBySquare(Square.of(File.H, Rank.EIGHT));

        assertAll(
                () -> assertThat(a1Piece.getClass()).isEqualTo(Rook.class),
                () -> assertThat(h8Piece.getClass()).isEqualTo(Rook.class)
        );
    }

    @Test
    void move() {
        board.move(Square.of(File.A, Rank.TWO), Square.of(File.A, Rank.THREE));
        Piece a3Piece = board.findPieceBySquare(Square.of(File.A, Rank.THREE));
        board.move(Square.of("e2"), Square.of("e3"));
        Piece e3 = board.findPieceBySquare(Square.of("e3"));
        assertThat(a3Piece).isEqualTo(Pawn.of(Color.WHITE));
        assertThat(e3).isEqualTo(Pawn.of(Color.WHITE));
    }


}
