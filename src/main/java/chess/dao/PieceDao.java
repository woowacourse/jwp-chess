package chess.dao;

import chess.controller.dto.response.PieceResponse;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Optional;

public interface PieceDao {

    void save(Long gameId, Position position, Piece piece);

    List<PieceResponse> findAll(Long gameId);

    Optional<Piece> find(Long gameId, Position position);

    void updatePosition(Long gameId, Position start, Position target);

    void delete(Long gameId, Position position);
}
