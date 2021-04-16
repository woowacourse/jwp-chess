package chess.spring.repository;

import chess.spring.domain.ChessGameNew;
import java.util.List;

public interface ChessGameRepositoryNew {
    Long save(ChessGameNew chessGame);

    List<ChessGameNew> findAll();
}
