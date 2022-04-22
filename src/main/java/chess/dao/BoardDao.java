package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.PieceDto;

import java.util.List;
import java.util.Map;

public interface BoardDao {
    List<PieceDto> findAll(long roomId);
    void saveAll(Map<Position, Piece> board, long roomId);

    void delete(long roomId);

    void updatePosition(String source, String destination);
}
