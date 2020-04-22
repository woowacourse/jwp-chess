package wooteco.chess.domain.piece.pieces;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.PositionFactory;
import wooteco.chess.domain.position.Row;

import java.util.ArrayList;
import java.util.List;

public class PiecesInitializer {
    public static Pieces operate() {
        return makeInitialPieces();
    }

    private static Pieces makeInitialPieces() {
        List<Piece> pieces = new ArrayList<>();

        for (Column column : Column.getInitialColumns()) {
            addPieceBy(column, pieces);
        }

        return new Pieces(pieces);
    }

    private static void addPieceBy(Column column, List<Piece> pieces) {
        for (Row row : Row.values()) {
            pieces.add(create(row, column));
        }
    }

    private static Piece create(Row row, Column column) {
        Position position = PositionFactory.of(row, column);
        Color color = column.getColor();

        if (column.isPawnInitial()) {
            return new Piece(position, column.getPawnType(), color);
        }

        return new Piece(position, row.getPieceType(), color);
    }
}
