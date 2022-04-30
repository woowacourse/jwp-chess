package chess.dao;

import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;

import java.util.List;

public class FakeGameDao implements GameDao {

    private GameDto gameDto;

    @Override
    public void removeAll(int id) {
        gameDto = null;
    }

    @Override
    public void save(int id, GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void modify(int id, GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void modifyStatus(int id, GameStatusDto statusDto) {
        this.gameDto = GameDto.of(this.gameDto.getTurn(), statusDto.getName());
    }

    @Override
    public GameDto find(int id) {
        return gameDto;
    }

    @Override
    public List<GameDto> findAll() {
        return List.of(gameDto);
    }

    @Override
    public Integer findLastGameId() {
        return 1;
    }
}
