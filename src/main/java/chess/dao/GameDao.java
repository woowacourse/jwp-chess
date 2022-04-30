package chess.dao;

import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;

public interface GameDao {

    void removeAll(int id);

    void save(int id, GameDto gameDto);

    void modify(int id, GameDto gameDto);

    void modifyStatus(int id, GameStatusDto statusDto);

    GameDto find(int id);

    Integer findLastGameId();
}
