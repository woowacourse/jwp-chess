package chess.domain.board;

import chess.domain.board.piece.EmptyPiece;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.bishop.Bishop;
import chess.domain.board.piece.king.King;
import chess.domain.board.piece.knight.Knight;
import chess.domain.board.piece.pawn.Pawn;
import chess.domain.board.piece.queen.Queen;
import chess.domain.board.piece.rook.Rook;
import chess.domain.board.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardInitializerTest {

    private static final int BOARD_LENGTH = 8;

    private Board board;

    @BeforeEach
    void setUp() {
        board = BoardInitializer.initiateBoard();
    }

    private static Piece[] getPiecesOfFirstLine(final Owner owner) {
        return new Piece[]{
                Rook.getInstanceOf(owner),
                Knight.getInstanceOf(owner),
                Bishop.getInstanceOf(owner),
                Queen.getInstanceOf(owner),
                King.getInstanceOf(owner),
                Bishop.getInstanceOf(owner),
                Knight.getInstanceOf(owner),
                Rook.getInstanceOf(owner)
        };
    }

    private static Piece[] getPiecesOfSecondLine(final Owner owner) {
        final Piece[] pieces = new Pawn[BOARD_LENGTH];
        Arrays.fill(pieces, Pawn.getInstanceOf(owner));
        return pieces;
    }

    private static Piece[] getEmptyLine() {
        final Piece[] pieces = new EmptyPiece[BOARD_LENGTH];
        Arrays.fill(pieces, EmptyPiece.getInstance());
        return pieces;
    }

    @Test
    @DisplayName("보드 64개의 칸이 만들어진다.")
    void initiateBoardTest() {
        assertThat(board.getBoard().size()).isEqualTo(64);
        for (Position position : board.getBoard().keySet()) {
            assertThat(board.getBoard().containsKey(position)).isTrue();
        }
    }

    @Test
    @DisplayName("보드안에 체스말 제대로 세팅된다.")
    void initiateBoardPiecesTest() {
        List<Piece> pieces = new ArrayList<>();

        pieces.addAll(Arrays.asList(getPiecesOfFirstLine(Owner.BLACK)));
        pieces.addAll(Arrays.asList(getPiecesOfSecondLine(Owner.BLACK)));
        for (int i = 0; i < 4; i++) {
            pieces.addAll(Arrays.asList(getEmptyLine()));
        }
        pieces.addAll(Arrays.asList(getPiecesOfSecondLine(Owner.WHITE)));
        pieces.addAll(Arrays.asList(getPiecesOfFirstLine(Owner.WHITE)));

        List<Piece> boardPieces = board.pieces();

        for (int i = 0; i < 64; i++) {
            assertThat(boardPieces.get(i)).isEqualTo(pieces.get(i));
        }
    }
}