package chess.dao;

import chess.dao.dto.GameDto;

import java.util.List;

public interface SpringGameDao {

    long save(GameDto gameDto);

    void remove(GameDto gameDto);

    GameDto find(Long id, String password);

    List<GameDto> findAll();
}
