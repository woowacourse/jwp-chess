package chess.dao;

import chess.domain.ChessGame;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

public interface GameDao {

    Long save(final ChessGame game);

    Optional<ChessGame> findById(final Long id);

    List<ChessGame> findAll();

    List<ChessGame> findHistoriesByMemberId(final Long memberId);

    void update(final ChessGame game);
}
