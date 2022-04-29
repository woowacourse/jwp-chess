package chess.dao;

import chess.domain.position.Position;
import chess.dto.PieceDto;

import java.util.List;

public interface PieceDao {

    void removeByPosition(Long gameId, Position position);

    void removeAll();

    void save(PieceDto pieceDto);

    void saveAll(List<PieceDto> pieceDtos);

    List<PieceDto> findPiecesByGameId(Long gameId);

    void updatePosition(Long gameId, Position position, Position updatedPosition);
}
