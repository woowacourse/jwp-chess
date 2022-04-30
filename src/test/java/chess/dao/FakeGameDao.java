package chess.dao;

import chess.dto.GameDto;
import chess.domain.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameDao {

    Long id = 0L;
    private Map<Long, GameDto> games = new HashMap<>();

    @Override
    public Long save(GameDto gameDto) {
        games.put(id, gameDto);
        return id++;
    }

    @Override
    public void removeById(Long gameId) {
        games.remove(gameId);
    }

    @Override
    public GameDto findGameById(Long id) {
        return games.get(id);
    }

    @Override
    public String findPasswordById(Long id) {
        return games.get(id).getPassword();
    }

    @Override
    public GameStatus findStatusById(Long id) {
        return GameStatus.find(games.get(id).getStatus());
    }

    @Override
    public List<GameDto> findAll() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(Long gameId, String turn, String status) {
        GameDto gameDto = games.get(gameId);
        games.replace(gameId, new GameDto(gameId, gameDto.getTitle(), gameDto.getPassword(), turn, status));
    }

    @Override
    public void updateStatus(Long gameId, GameStatus status) {
        GameDto game = games.get(gameId);
        GameDto updatedGame = new GameDto(game.getId(), game.getTitle(), game.getPassword(), game.getTurn(), status.getName());
        games.replace(gameId, updatedGame);
    }
}
