package chess.dao;

import chess.dao.dto.GameDto;
import chess.domain.GameStatus;

import java.util.List;

public interface GameDao {

    Long save(GameDto gameDto);

    void removeById(Long gameId);

    GameDto findGameById(Long id);

    String findPasswordById(Long id);

    GameStatus findStatusById(Long id);

    List<GameDto> findAll();

    void updateGame(Long gameId, String turn, String status);

    void updateStatus(Long gameId, GameStatus statusDto);
}