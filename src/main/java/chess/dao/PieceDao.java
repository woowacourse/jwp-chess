package chess.dao;

import chess.domain.position.Position;
import chess.service.dto.PieceDto;

import java.util.List;

public interface PieceDao {

    void remove(int id, Position position);

    void removeAll(int id);

    void saveAll(int id, List<PieceDto> pieceDtos);

    void save(int id, PieceDto pieceDto);

    List<PieceDto> findAll(int id);

    void modifyPosition(int id, Position source, Position target);
}
