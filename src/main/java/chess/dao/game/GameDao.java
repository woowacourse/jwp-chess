package chess.dao.game;

import java.util.List;
import java.util.Optional;

import chess.domain.ChessGame;
import chess.dto.CreateGameRequestDto;

public interface GameDao {

    Long save(final CreateGameRequestDto createGameRequestDto);

    Optional<ChessGame> findById(final Long id);

    List<ChessGame> findAll();

    List<ChessGame> findHistoriesByMemberId(final Long memberId);

    void move(final long gameId, final ChessGame game, final String rawFrom, final String rawTo);

    void terminate(final Long id);
}
