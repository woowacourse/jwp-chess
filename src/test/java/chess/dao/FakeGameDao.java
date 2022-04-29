package chess.dao;


import chess.dto.GameStatusDto;
import chess.domain.GameStatus;

public class FakeGameDao implements GameDao {

    private GameStatusDto gameDto;

    @Override
    public void removeAll() {
        gameDto = null;
    }

    @Override
    public void saveGame(GameStatusDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void updateGame(GameStatusDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void updateStatus(GameStatus statusDto) {
        this.gameDto = new GameStatusDto(this.gameDto.getTurn(), statusDto.getName());
    }

    @Override
    public GameStatusDto findGame() {
        return gameDto;
    }
}
