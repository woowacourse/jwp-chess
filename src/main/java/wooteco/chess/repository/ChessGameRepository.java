package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.domain.game.ChessGame;

public interface ChessGameRepository extends CrudRepository<ChessGame, Long> {
}
