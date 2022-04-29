package chess.dao;

import chess.domain.position.Position;
import chess.service.dto.PieceDto;

import java.util.List;

public interface PieceDao {

    void remove(Position position);

    void remove(int id, Position position);

    void removeAll();

    void removeAll(int id);

    void saveAll(List<PieceDto> pieceDtos);

    void saveAll(int id, List<PieceDto> pieceDtos);

    void save(PieceDto pieceDto);

    void save(int id, PieceDto pieceDto);

    List<PieceDto> findAll();

    List<PieceDto> findAll(int id);

    void modifyPosition(Position source, Position target);

    void modifyPosition(int id, Position source, Position target);
}
