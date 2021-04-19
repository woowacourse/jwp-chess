package chess.repository;

import chess.domain.game.ChessGame;
import java.util.List;

public interface ChessGameRepository {
    Long save(ChessGame chessGame);

    ChessGame findById(Long gameId);

    List<ChessGame> findAll();

    List<ChessGame> findAllBlackPlayerPasswordIsNull();

    void update(ChessGame chessGame);

    void deleteById(Long gameId);

    void deleteAll();
}
