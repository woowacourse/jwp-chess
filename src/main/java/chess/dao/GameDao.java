package chess.dao;

import chess.dto.GameStatusDto;
import chess.domain.GameStatus;

public interface GameDao {

    void removeAll();

    void saveGame(GameStatusDto gameDto);

    void updateGame(GameStatusDto gameDto);

    void updateStatus(GameStatus statusDto);

    GameStatusDto findGame();
}
