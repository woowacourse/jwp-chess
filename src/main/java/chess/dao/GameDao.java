package chess.dao;

import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;

public interface GameDao {

    void removeAll();

    void save(GameDto gameDto);

    void modify(GameDto gameDto);

    void modifyStatus(GameStatusDto statusDto);

    GameDto find();
}
