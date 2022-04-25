package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import chess.domain.piece.Piece;
import chess.domain.piece.detail.PieceType;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;

class BoardInitializerTest {

    @Test
    void initializeBoard() {
        Map<Square, PieceType> board = new HashMap<>();

        board.put(Square.from("e1"), PieceType.KING);
        board.put(Square.from("e8"), PieceType.KING);

        board.put(Square.from("d1"), PieceType.QUEEN);
        board.put(Square.from("d8"), PieceType.QUEEN);

        board.put(Square.from("c1"), PieceType.BISHOP);
        board.put(Square.from("f1"), PieceType.BISHOP);
        board.put(Square.from("c8"), PieceType.BISHOP);
        board.put(Square.from("f8"), PieceType.BISHOP);

        board.put(Square.from("b1"), PieceType.KNIGHT);
        board.put(Square.from("g1"), PieceType.KNIGHT);
        board.put(Square.from("b8"), PieceType.KNIGHT);
        board.put(Square.from("g8"), PieceType.KNIGHT);

        board.put(Square.from("a1"), PieceType.ROOK);
        board.put(Square.from("h1"), PieceType.ROOK);
        board.put(Square.from("a8"), PieceType.ROOK);
        board.put(Square.from("h8"), PieceType.ROOK);

        for (final File file : File.values()) {
            board.put(Square.of(file, Rank.from(2)), PieceType.PAWN);
            board.put(Square.of(file, Rank.from(7)), PieceType.PAWN);
        }

        for (final File file : File.values()) {
            board.put(Square.of(file, Rank.from(3)), PieceType.BLANK);
            board.put(Square.of(file, Rank.from(4)), PieceType.BLANK);
            board.put(Square.of(file, Rank.from(5)), PieceType.BLANK);
            board.put(Square.of(file, Rank.from(6)), PieceType.BLANK);
        }

        Map<Square, Piece> board2 = BoardInitializer.create();
        Map<Square, PieceType> temp = board2.keySet()
                .stream()
                .collect(Collectors.toMap(key -> key,
                        key -> board2.get(key).getPieceType(),
                        (piece, pieceType) -> pieceType));

        assertThat(temp).isEqualTo(board);
    }
}
