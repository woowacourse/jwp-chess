package chess.dao;

import chess.dao.dto.GameDto;
import chess.domain.GameStatus;

import java.util.List;

public interface SpringGameDao {

    long save(GameDto gameDto);

    void removeById(Long gameId);

    GameDto findById(Long id);

    String findPasswordById(Long id);

    GameStatus findStatusById(Long id);

    List<GameDto> findAll();

    void updateGame(GameDto gameDto);

    void updateStatus(Long gameId, GameStatus statusDto);
}