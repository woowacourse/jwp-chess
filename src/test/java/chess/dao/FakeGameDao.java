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
    public void save(GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void modify(GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void modifyStatus(GameStatusDto statusDto) {
        this.gameDto = new GameDto(this.gameDto.getTurn(), statusDto.getName());
    }

    @Override
    public GameDto find() {
        return gameDto;
    }
}
