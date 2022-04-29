package chess.dao;

import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;

public interface GameDao {

    void removeAll();

    void removeAll(int id);

    void save(GameDto gameDto);

    void save(int id, GameDto gameDto);

    void modify(GameDto gameDto);

    void modify(int id, GameDto gameDto);

    void modifyStatus(GameStatusDto statusDto);

    void modifyStatus(int id, GameStatusDto statusDto);

    GameDto find();

    GameDto find(int id);

    Integer count();
}
