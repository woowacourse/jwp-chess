package chess.dao.game;

import chess.domain.ChessGame;
import java.util.List;
import java.util.Optional;

public interface GameDao {

    Long save(final ChessGame game);

    Optional<ChessGame> findById(final Long id);

    List<ChessGame> findAll();

    List<ChessGame> findHistoriesByMemberId(final Long memberId);

    void move(final ChessGame game, final String rawFrom, final String rawTo);

    void terminate(final Long id);
}
