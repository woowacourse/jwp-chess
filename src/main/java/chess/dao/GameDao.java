package chess.dao;

import chess.dto.GameDto;
import chess.dto.GameStatusDto;

public interface GameDao {

    void removeAll();

    void save(GameDto gameDto);

    void modify(GameDto gameDto);

    void modifyStatus(GameStatusDto statusDto);

    GameDto find();
}
