package chess.dao;

import chess.domain.board.Board;
import chess.domain.game.room.RoomId;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public interface BoardDao {
    Board getBoard(RoomId roomId);

    void createPiece(RoomId roomId, Position position, Piece piece);

    void deletePiece(RoomId roomId, Position position);

    void updatePiecePosition(RoomId roomId, Position from, Position to);
}
