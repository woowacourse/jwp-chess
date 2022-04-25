package chess.web.dto;

import chess.domain.Board;
import chess.domain.Color;
import chess.domain.generator.EmptyBoardGenerator;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import java.util.List;

public class PiecesDto {

    private final List<PieceDto> pieces;

    public PiecesDto(List<PieceDto> pieces) {
        this.pieces = pieces;
    }

    public Board toBoard() {
        Board board = new Board(new EmptyBoardGenerator().generate().getBoard());
        for (PieceDto pieceDto : pieces) {
            Position position = new Position(pieceDto.getPosition());
            Color color = Color.from(pieceDto.getColor());
            Piece piece = PieceType.from(pieceDto.getPieceType()).newPiece(color);
            board.place(position, piece);
        }
        return board;
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }
}
