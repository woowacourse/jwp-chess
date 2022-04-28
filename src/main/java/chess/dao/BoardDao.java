package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.PieceDto;

import java.util.List;
import java.util.Map;

public interface BoardDao {

    void savePieces(Map<Position, Piece> board, long roomId);

    List<PieceDto> findAllPiece(long roomId);

    void updatePosition(String symbol, String destination, long roomId);

    void deleteBoard();
}
