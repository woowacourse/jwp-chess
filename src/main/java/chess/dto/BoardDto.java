package chess.dto;

import chess.domain.board.Position;
import chess.domain.board.PositionFactory;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import wooteco.chess.entity.PieceEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardDto {
    private Map<String, String> board;

    public BoardDto(List<PieceEntity> pieceEntities) {
        Map<String, String> board = new HashMap<>();
        for (PieceEntity pieceEntity : pieceEntities) {
            board.put(pieceEntity.getPosition(), pieceEntity.getPieceName());
        }

        this.board = board;
    }

    public Map<Position, Piece> createBoard() {
        Map<Position, Piece> boardData = new HashMap<>();

        for (String positionName : this.board.keySet()) {
            Position position = PositionFactory.of(positionName);
            String pieceName = this.board.get(positionName);
            Piece piece = PieceType.of(pieceName).createPiece(position);

            boardData.put(position, piece);
        }

        return boardData;
    }

    public Map<String, String> getBoard() {
        return this.board;
    }
}
