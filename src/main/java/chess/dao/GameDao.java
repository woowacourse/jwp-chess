package chess.dao;

import chess.dao.entity.Game;
import chess.domain.GameStatus;

import java.util.List;

public interface GameDao {

    Long save(Game game);

    void removeById(Long id);

    Game findGameById(Long id);

    String findPasswordById(Long id);

    GameStatus findStatusById(Long id);

    List<Game> findAll();

    void updateGame(Long id, String turn, String status);

    void updateStatus(Long id, GameStatus statusDto);
}