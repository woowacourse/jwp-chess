package wooteco.chess.domain.piece.pieces;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.TestPieceFactory;
import wooteco.chess.domain.position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestPiecesFactory {
    public static Pieces of(List<Position> positions) {
        List<Piece> pieces = new ArrayList<>();

        for (Position position : positions) {
            pieces.add(TestPieceFactory.createRook(position, Color.WHITE));
        }
        return new Pieces(pieces);
    }

    public static Pieces of(List<Position> positions, Color color) {
        List<Piece> pieces = new ArrayList<>();

        for (Position position : positions) {
            pieces.add(TestPieceFactory.createRook(position, color));
        }
        return new Pieces(pieces);
    }

    public static Pieces createBy(List<Piece> inputPieces) {
        List<Piece> pieces = new ArrayList<>(inputPieces);
        return new Pieces(pieces);
    }

    public static Pieces createOnlyWhite() {
        List<Piece> pieces = PiecesInitializer.operate().getPieces().stream()
                .filter(piece -> piece.getColor().isWhite())
                .collect(Collectors.toList());

        return new Pieces(pieces);
    }
}
