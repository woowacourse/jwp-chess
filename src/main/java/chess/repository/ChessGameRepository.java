package chess.repository;

import chess.domain.game.ChessGame;
import java.util.List;
import java.util.Optional;

public interface ChessGameRepository {
    Long save(ChessGame chessGame);

    Optional<ChessGame> findById(Long gameId);

    List<ChessGame> findAll();

    List<ChessGame> findAllBlackPlayerPasswordIsNull();

    void update(ChessGame chessGame);

    void deleteById(Long gameId);

    void deleteAll();
}
