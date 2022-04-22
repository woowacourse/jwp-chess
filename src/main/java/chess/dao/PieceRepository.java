package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.web.dto.PieceDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PieceRepository {
    void save(int boardId, String target, PieceDto pieceDto);

    void saveAll(int boardId, Map<Position, Piece> pieces);

    Optional<PieceDto> findOne(int boardId, String position);

    List<PieceDto> findAll(int boardId);

    void updateOne(int boardId, String position, PieceDto pieceDto);

    void deleteOne(int boardId, String position);

    void deleteAll(int boardId);
}
