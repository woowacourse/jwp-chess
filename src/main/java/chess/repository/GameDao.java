package chess.repository;

import java.util.List;

import chess.repository.dto.game.GameDto;
import chess.repository.dto.game.GameFinishedDto;
import chess.repository.dto.game.GameUpdateDto;

public interface GameDao {

    Long save(final GameDto gameDto);

    GameDto findById(final Long id);

    List<GameFinishedDto> findIdAndFinished();

    void update(final GameUpdateDto gameUpdateDto);

    void remove(final Long id);

}
