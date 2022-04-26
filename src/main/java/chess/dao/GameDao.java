package chess.dao;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;

public interface GameDao {


    void removeAll();

    void saveGame(GameDto gameDto);

    void updateGame(GameDto gameDto);

    void updateStatus(GameStatusDto statusDto);

    GameDto findGame();
}
