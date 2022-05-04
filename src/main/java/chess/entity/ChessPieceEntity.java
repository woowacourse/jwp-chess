package chess.entity;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.dto.ChessPieceMapper;

public class ChessPieceEntity {

    private final int chessPieceId;
    private final int roomId;
    private final String position;
    private final String chessPiece;
    private final String color;

    public ChessPieceEntity(final int chessPieceId, final int roomId, final String position, final String chessPiece,
                            final String color) {
        this.chessPieceId = chessPieceId;
        this.roomId = roomId;
        this.position = position;
        this.chessPiece = chessPiece;
        this.color = color;
    }

    public ChessPiece toChessPiece() {
        return ChessPieceMapper.toChessPiece(chessPiece, color);
    }

    public Position toPosition() {
        return Position.from(position);
    }
}
