package chess.dao;

import chess.service.dto.ChessRequestDto;
import chess.service.dto.GameDto;
import chess.service.dto.GameStatusDto;
import chess.service.dto.RoomResponseDto;

import java.util.List;
import java.util.Optional;

public class FakeGameDao implements GameDao {

    private GameDto gameDto;

    @Override
    public void removeAll(int id) {
        gameDto = null;
    }

    @Override
    public void save(int id, ChessRequestDto chessRequestDto) {
        this.gameDto = GameDto.of(chessRequestDto.getTurn(), chessRequestDto.getStatus());
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
    public List<RoomResponseDto> findAll() {
        return List.of(new RoomResponseDto(1, "title"));
    }

    @Override
    public String findPassword(int id) {
        return "password";
    }

    @Override
    public Optional<Integer> findLastGameId() {
        if (gameDto == null) {
            return Optional.empty();
        }
        return Optional.of(1);
    }
}
