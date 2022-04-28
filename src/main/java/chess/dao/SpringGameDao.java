package chess.dao;

import chess.dao.dto.GameDto;
import chess.dto.GameStatusDto;

import java.util.List;

public interface SpringGameDao {

    long save(GameDto gameDto);

    void remove(GameDto gameDto);

    GameDto findById(Long id);

    List<GameDto> findAll();

    void updateGame(GameDto gameDto);

    void updateStatus(Long gameId, GameStatusDto statusDto);
}