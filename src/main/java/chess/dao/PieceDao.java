package chess.dao;

import chess.domain.position.Position;
import chess.dto.PieceDto;

import java.util.List;

public interface PieceDao {

    void removeByPosition(Position position);

    void removeAll();

    void save(PieceDto pieceDto);

    void saveAll(List<PieceDto> pieceDtos);

    List<PieceDto> findPiecesByGameId(Long gameId);

    List<PieceDto> findAll();

    void updatePosition(Position position, Position updatedPosition);

    void updatePosition(Long gameId, Position position, Position updatedPosition);
}
