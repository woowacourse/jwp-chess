package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.response.PieceDto;

import java.util.List;
import java.util.Map;

public interface BoardDao {

    List<PieceDto> findAll(Long roomId);

    void saveAll(Map<Position, Piece> board, Long roomId);

    void delete(Long roomId);

    void updatePosition(String symbol, String position, Long roomId);
}
