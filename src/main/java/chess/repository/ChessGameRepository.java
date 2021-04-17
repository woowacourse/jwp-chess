package chess.repository;

import chess.domain.ChessGame;
import java.util.List;

public interface ChessGameRepository {
    Long save(ChessGame chessGame);

    List<ChessGame> findAll();

    ChessGame findById(Long gameId);

    void deleteById(Long gameId);

    void update(ChessGame chessGame);
}
