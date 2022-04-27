package chess.dao;

import chess.domain.ChessGame;
import java.util.List;
import java.util.Optional;

public interface GameDao {

    Long save(final ChessGame game);

    Optional<ChessGame> findById(final Long id);

    List<ChessGame> findAll();

    List<ChessGame> findHistoriesByMemberId(final Long memberId);

    void terminate(final ChessGame game);

    void updateByMove(final ChessGame chessGame, final String rawFrom, final String rawTo);

    void deleteGameById(final Long id);
}
