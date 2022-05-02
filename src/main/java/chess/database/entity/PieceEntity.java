package chess.database.entity;

import java.util.Map;

import chess.domain.board.Point;
import chess.domain.piece.Piece;

public class PieceEntity {
    // CREATE TABLE piece (
    //     piece_type          VARCHAR(10) NOT NULL,
    // piece_color         VARCHAR(10) NOT NULL,
    // vertical_index      INT         NOT NULL,
    // horizontal_index    INT         NOT NULL,
    // game_id             BIGINT      NOT NULL,
    // FOREIGN KEY(game_id) REFERENCES game (id)
    //     );
    private final String pieceType;
    private final String pieceColor;
    private final Integer verticalIndex;
    private final Integer horizontalIndex;
    private final Long gameId;

    public PieceEntity(String pieceType, String pieceColor, Integer verticalIndex, Integer horizontalIndex,
        Long gameId) {
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        this.verticalIndex = verticalIndex;
        this.horizontalIndex = horizontalIndex;
        this.gameId = gameId;
    }

    public static PieceEntity from(Map.Entry<Point, Piece> pointPieces, Long gameId) {
        final Point point = pointPieces.getKey();
        final Piece piece = pointPieces.getValue();
        return new PieceEntity(piece.getType(), piece.getColor(), point.getVertical(), point.getHorizontal(), gameId);
    }

    public String getPieceType() {
        return pieceType;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public Integer getVerticalIndex() {
        return verticalIndex;
    }

    public Integer getHorizontalIndex() {
        return horizontalIndex;
    }

    public Long getGameId() {
        return gameId;
    }
}
