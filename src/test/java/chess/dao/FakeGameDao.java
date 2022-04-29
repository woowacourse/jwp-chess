package chess.dao;


import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;

public class FakeGameDao implements GameDao {

    private GameDto gameDto;

    @Override
    public void removeAll() {
        gameDto = null;
    }

    @Override
    public void removeAll(int id) {
        gameDto = null;
    }

    @Override
    public void save(GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void save(int id, GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void modify(GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void modify(int id, GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void modifyStatus(GameStatusDto statusDto) {
        this.gameDto = GameDto.of(this.gameDto.getTurn(), statusDto.getName());
    }

    @Override
    public void modifyStatus(int id, GameStatusDto statusDto) {
        this.gameDto = GameDto.of(this.gameDto.getTurn(), statusDto.getName());
    }

    @Override
    public GameDto find() {
        return gameDto;
    }

    @Override
    public GameDto find(int id) {
        return gameDto;
    }

    @Override
    public Integer count() {
        return 1;
    }
}
